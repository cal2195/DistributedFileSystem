package directoryservice.network

import directoryservice.DirectoryService
import directoryservice.filesystem.Hashing
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket
import java.time.Instant

class Request(var clientSocket: Socket) : Runnable {

    private var inputStream = ObjectInputStream(clientSocket.getInputStream())
    private var outputStream = ObjectOutputStream(clientSocket.getOutputStream())

    override fun run() {
        println("Started request for client ${clientSocket.remoteSocketAddress}")
        listen()
    }

    private fun listen() {
        var packet = inputStream.readObject()

        when (packet) {
            is DirJoinRequest -> processJoin(packet)
            is DirAttrRequest -> processAttr(packet)
            is DirReadRequest -> processRead(packet)
            is DirWriteRequest -> processWrite(packet)
            is DirDeleteRequest -> processDelete(packet)
        }
    }

    private fun processAttr(packet: DirAttrRequest) {
        val node = DirectoryService.state.root.maybeFindNode(packet.path)
        println("Found attr node $node for ${packet.path}")
        respond(DirAttrResponse(node?.attr))
    }

    private fun processJoin(packet: DirJoinRequest) {
        println("Processing Join DirPacket")
        DirectoryService.state.servers.put(packet.key, packet.address)
        respond(DirJoinResponse(true))
    }

    private fun processRead(packet: DirReadRequest) {
        if (packet.isDir) {
            val node = DirectoryService.state.root.findNode(packet.path, packet.isDir)
            val list = ArrayList(node.children.keys)
            println("Listing dir ${packet.path}")
            respond(DirListResponse(list))
        } else {
            val key = packet.path.hashCode()
            val servers = Hashing.getClosest(key)
            val node = DirectoryService.state.root.maybeFindNode(packet.path)
            println("Returing servers for ${packet.path}")
            respond(DirReadResponse(key, node!!.attr, servers))
        }
    }

    private fun processWrite(packet: DirWriteRequest) {
        val key = packet.path.hashCode()
        val servers = Hashing.getClosest(key)
        // Create file/dir in tree and update timestamp
        val node = DirectoryService.state.root.findNode(packet.path, packet.isDir)
        node.attr.timestamp = Instant.now()
        node.attr.size = packet.size
        println("Created file/dir for ${packet.path}")
        respond(DirWriteResponse(key, servers))
    }

    private fun processDelete(packet: DirDeleteRequest) {
        val parentPath = packet.path.subSequence(0, packet.path.lastIndexOf("/")) as String
        val child = packet.path.subSequence(packet.path.lastIndexOf("/"), packet.path.length)
        val parent = DirectoryService.state.root.findNode(parentPath, true)
        parent.children.remove(child)

        val key = packet.path.hashCode()
        val servers = Hashing.getClosest(key)

        respond(DirDeleteResponse(key, servers))
    }

    private fun respond(packet: Any) {
        println("Responding...")
        outputStream.writeObject(packet)
        outputStream.flush()
        outputStream.close()
        println("Closed connection!")
    }
}

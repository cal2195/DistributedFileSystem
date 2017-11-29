package directoryservice.network

import directoryservice.DirectoryService
import directoryservice.filesystem.Hashing
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket

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
            is JoinRequest -> processJoin(packet)
            is ReadRequest -> processRead(packet)
            is WriteRequest -> processWrite(packet)
            is DeleteRequest -> processDelete(packet)
        }
    }

    private fun processJoin(packet: JoinRequest) {
        DirectoryService.state.servers.put(packet.key, clientSocket.inetAddress)
        respond(JoinResponse(true))
    }

    private fun processRead(packet: ReadRequest) {
        val key = packet.path.hashCode()
        val servers = Hashing.getClosest(key)
        respond(ReadResponse(key, servers))
    }

    private fun processWrite(packet: WriteRequest) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun processDelete(packet: DeleteRequest) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun respond(packet: Any) {
        outputStream.writeObject(packet)
        outputStream.flush()
        outputStream.close()
    }
}

package fileserver

import fileserver.packets.*
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket

class Request(var clientSocket: Socket) : Runnable {

    private var inputStream = ObjectInputStream(clientSocket.getInputStream())
    private var outputStream = ObjectOutputStream(clientSocket.getOutputStream())

    val data = Data()

    override fun run() {
        println("Started request for client ${clientSocket.remoteSocketAddress}")
        listen()
    }

    private fun listen() {
        var packet = inputStream.readObject()

        when (packet) {
            is NodeReadRequest -> processRead(packet)
            is NodeWriteRequest -> processWrite(packet)
            is NodeDeleteRequest -> processDelete(packet)
        }
    }

    private fun processRead(packet: NodeReadRequest) {
        val response = NodeReadResponse(data.getData(packet.hash))
        respond(response)
    }

    private fun processWrite(packet: NodeWriteRequest) {
        data.writeData(packet.hash, packet.data)
        val response = NodeWriteResponse(true)
        respond(response)
    }

    private fun processDelete(packet: NodeDeleteRequest) {
        val response = NodeDeleteResponse(data.deleteData(packet.hash))
        respond(response)
    }

    private fun respond(packet: Any) {
        outputStream.writeObject(packet)
        outputStream.flush()
        outputStream.close()
    }
}

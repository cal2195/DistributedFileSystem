package fileserver

import SERVER_PORT
import directoryservice.network.ConnectionAddress
import directoryservice.network.JoinRequest
import directoryservice.network.JoinResponse
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.ServerSocket
import java.net.Socket

object Connections {

    lateinit var serverSocket: ServerSocket

    fun listen(port: Int) {
        serverSocket = ServerSocket(port)
        println("Listening on $port")
        while (true) {
            println("Waiting for connection...")
            var clientSocket = serverSocket.accept()
            println("New Connection!")

            Thread(Request(clientSocket)).start()
        }
    }

    fun announce(serverAddress: String, address: String, port: Int, key: Int): Boolean {
        val socket = Socket(serverAddress, SERVER_PORT)
        val output = ObjectOutputStream(socket.getOutputStream())
        println("Announcing to dir server!")
        output.writeObject(JoinRequest(key, ConnectionAddress(address, port)))
        output.flush()
        println("Awaiting response!")
        val input = ObjectInputStream(socket.getInputStream())
        val response = input.readObject() as JoinResponse
        println("Success?: ${response.success}")
        return response.success
    }
}

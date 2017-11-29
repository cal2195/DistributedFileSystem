package fileserver

import java.net.ServerSocket

class Connections(val port: Int) {

    lateinit var serverSocket: ServerSocket

    fun listen() {
        serverSocket = ServerSocket(port)
        println("Listening on $port")
        while (true) {
            println("Waiting for connection...")
            var clientSocket = serverSocket.accept()
            println("New Connection!")


        }
    }
}

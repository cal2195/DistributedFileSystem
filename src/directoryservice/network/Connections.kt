package directoryservice.network

import java.net.ServerSocket

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
}
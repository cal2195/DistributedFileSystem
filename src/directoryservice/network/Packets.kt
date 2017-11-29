package directoryservice.network


import java.io.Serializable
import java.net.InetAddress

data class JoinRequest(val key: Int) : Serializable

data class JoinResponse(val success: Boolean) : Serializable

data class ReadRequest(val path: String) : Serializable

data class ReadResponse(val hash: Int, val servers: Pair<InetAddress?, InetAddress?>) : Serializable

data class WriteRequest(val path: String) : Serializable

data class WriteResponse(val hash: Int, val servers: ArrayList<String>) : Serializable

data class DeleteRequest(val path: String) : Serializable

data class DeleteResponse(val success: Boolean) : Serializable

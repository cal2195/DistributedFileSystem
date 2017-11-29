package directoryservice.network


import java.io.Serializable

data class ConnectionAddress(val address: String, val port: Int) : Serializable

data class JoinRequest(val key: Int, val address: ConnectionAddress) : Serializable

data class JoinResponse(val success: Boolean) : Serializable

data class ReadRequest(val path: String, val isDir: Boolean) : Serializable

data class ReadResponse(val hash: Int, val servers: Pair<ConnectionAddress?, ConnectionAddress?>) : Serializable

data class ListResponse(val files: ArrayList<String>) : Serializable

data class WriteRequest(val path: String, val isDir: Boolean) : Serializable

data class WriteResponse(val hash: Int, val servers: Pair<ConnectionAddress?, ConnectionAddress?>): Serializable

data class DeleteRequest(val path: String) : Serializable

data class DeleteResponse(val hash: Int, val servers: Pair<ConnectionAddress?, ConnectionAddress?>) : Serializable

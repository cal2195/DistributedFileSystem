package directoryservice.network


import directoryservice.filesystem.Attr
import java.io.Serializable

open class DirPacket

data class ConnectionAddress(val address: String, val port: Int) : Serializable, DirPacket()

data class DirJoinRequest(val key: Int, val address: ConnectionAddress) : Serializable, DirPacket()

data class DirJoinResponse(val success: Boolean) : Serializable, DirPacket()

data class DirAttrRequest(val path: String) : Serializable, DirPacket()

data class DirAttrResponse(val attr: Attr?) : Serializable, DirPacket()

data class DirReadRequest(val path: String, val isDir: Boolean) : Serializable, DirPacket()

data class DirReadResponse(val hash: Int, val attr: Attr, val servers: Pair<ConnectionAddress?, ConnectionAddress?>) : Serializable, DirPacket()

data class DirListResponse(val files: ArrayList<String>) : Serializable, DirPacket()

data class DirWriteRequest(val path: String, val isDir: Boolean, val size: Long) : Serializable, DirPacket()

data class DirWriteResponse(val hash: Int, val servers: Pair<ConnectionAddress?, ConnectionAddress?>): Serializable, DirPacket()

data class DirDeleteRequest(val path: String) : Serializable, DirPacket()

data class DirDeleteResponse(val hash: Int, val servers: Pair<ConnectionAddress?, ConnectionAddress?>) : Serializable, DirPacket()

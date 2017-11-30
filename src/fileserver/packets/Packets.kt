package fileserver.packets

import java.io.Serializable

open class NodePacket

data class NodeReadRequest(val hash: Int) : Serializable, NodePacket()

data class NodeReadResponse(val data: ByteArray) : Serializable, NodePacket()

data class NodeWriteRequest(val hash: Int, val data: ByteArray) : Serializable, NodePacket()

data class NodeWriteResponse(val success: Boolean) : Serializable, NodePacket()

data class NodeDeleteRequest(val hash: Int) : Serializable, NodePacket()

data class NodeDeleteResponse(val success: Boolean) : Serializable, NodePacket()

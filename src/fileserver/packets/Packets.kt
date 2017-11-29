package fileserver.packets

import java.io.Serializable

data class ReadRequest(val hash: Int) : Serializable

data class ReadResponse(val data: ByteArray) : Serializable

data class WriteRequest(val hash: Int, val data: ByteArray) : Serializable

data class WriteResponse(val success: Boolean) : Serializable

data class DeleteRequest(val hash: Int) : Serializable

data class DeleteResponse(val success: Boolean) : Serializable

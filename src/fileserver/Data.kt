package fileserver

import java.io.File

class Data {
    fun getData(hash: Int): ByteArray {
        val file = File("data/$hash")
        return file.readBytes()
    }

    fun writeData(hash: Int, data: ByteArray) {
        val file = File("data/$hash")
        return file.writeBytes(data)
    }

    fun deleteData(hash: Int): Boolean {
        val file = File("data/$hash")
        return file.delete()
    }
}
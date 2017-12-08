package fileserver

import java.io.File
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class Node(val serverAddress: String, val address: String, val port: Int) {
    lateinit var state: State

    init {
        loadState()
        Connections.announce(serverAddress, address, port, state.key)
        Connections.listen(port)
    }

    fun saveState() {
        val fileOut = FileOutputStream("state.sav")
        val objOut = ObjectOutputStream(fileOut)
        objOut.writeObject(state)
        objOut.flush()
        objOut.close()
        fileOut.flush()
        fileOut.close()
    }

    fun loadState() {
        val file = File("state.sav")
        state = when {
            file.exists() -> {
                val objIn = ObjectInputStream(file.inputStream())
                objIn.readObject() as State
            }
            else -> State()
        }

        saveState()

        val data = File("data")
        data.mkdir()
    }
}
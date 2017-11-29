package directoryservice

import SERVER_PORT
import directoryservice.network.Connections
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

object DirectoryService {
    lateinit var state: State

    init {
        loadState()
    }

    fun saveState() {
        val fileOut = FileOutputStream("state.sav")
        val objOut = ObjectOutputStream(fileOut)
        objOut.writeObject(state)
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
    }
}
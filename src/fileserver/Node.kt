package fileserver

import java.io.File
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class Node {
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
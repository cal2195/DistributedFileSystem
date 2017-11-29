package fileserver

import java.io.Serializable
import java.util.*

class State: Serializable {
    val key = Random().nextInt()
}
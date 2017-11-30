package directoryservice.filesystem

import java.io.Serializable
import java.time.Instant

class File(val name: String, isDir: Boolean) : Serializable {
    var children = HashMap<String, File>()

    val attr = Attr(isDir, 0)

    fun maybeFindNode(path: String) : File? {
        if (path == "/") return this

        val paths = path.split("/")
        return maybeFindNode(paths.subList(1, paths.size))
    }

    private fun maybeFindNode(path: List<String>) : File? {
        if (path.isEmpty()) return this

        var child = children[path[0]] ?: return null

        return child.maybeFindNode(path.subList(1, path.size))
    }

    fun findNode(path: String, dir: Boolean) : File {
        if (path == "/") return this

        val paths = path.split("/")
        return findNode(paths.subList(1, paths.size), dir)
    }

    private fun findNode(path: List<String>, dir: Boolean) : File {
        if (path.isEmpty()) return this

        var child = children[path[0]]
        if (child == null) {
            child = File(path[0], (path.size > 1) || dir)
            children.put(path[0], child)
        }

        return child.findNode(path.subList(1, path.size), dir)
    }
}

data class Attr(var isDir: Boolean, var size: Long, var timestamp: Instant = Instant.now()) : Serializable

package directoryservice.filesystem

import java.time.Instant

class File(name: String, isDir: Boolean) {
    var children = HashMap<String, File>()

    var timestamp = Instant.now()

    fun findNode(path: String, dir: Boolean) : File {
        val paths = path.split("/")
        return findNode(paths, dir)
    }

    fun findNode(path: List<String>, dir: Boolean) : File {
        if (path.isEmpty()) return this

        var child = children[path[0]]
        if (child == null) {
            child = File(path[0], (path.size > 1) || dir)
        }

        return child.findNode(path.subList(1, path.size), dir)
    }
}
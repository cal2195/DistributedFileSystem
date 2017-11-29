package directoryservice.filesystem

class File(name: String, isDir: Boolean) {
    var children = HashMap<String, File>()
}
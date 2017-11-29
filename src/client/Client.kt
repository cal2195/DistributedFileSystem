package client

import java.io.File


fun main(args: Array<String>) {

    var mountPoint = File("mount")
    mountPoint.mkdir()

    var fuse = Fuse()
    fuse.mount(mountPoint)
}

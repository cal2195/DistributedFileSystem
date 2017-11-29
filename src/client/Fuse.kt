package client

import net.fusejna.DirectoryFiller
import net.fusejna.FuseFilesystem
import net.fusejna.util.FuseFilesystemAdapterFull
import net.fusejna.ErrorCodes
import java.nio.file.Files.isDirectory
import net.fusejna.StructStat.StatWrapper
import java.io.File


class Fuse : FuseFilesystemAdapterFull() {

    init {
        log(true)
    }

    override fun getattr(path: String?, stat: StatWrapper?): Int {
        return 0
    }

    override fun readdir(path: String?, filler: DirectoryFiller?): Int {
        println("path: $path")

        filler?.add("helloOisin")

        return 0
    }
}
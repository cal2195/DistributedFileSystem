package client

import api.FileSystemApi
import net.fusejna.DirectoryFiller
import net.fusejna.ErrorCodes
import net.fusejna.StructFuseFileInfo
import net.fusejna.StructStat.StatWrapper
import net.fusejna.types.TypeMode
import net.fusejna.util.FuseFilesystemAdapterFull
import java.io.File
import java.nio.ByteBuffer


class Fuse(val api: FileSystemApi) : FuseFilesystemAdapterFull() {

    init {
        log(true)
    }

    override fun getattr(path: String, stat: StatWrapper): Int {
        val attr = api.getAttr(path) ?: return -ErrorCodes.ENOENT()

        return if (attr.isDir) {
            stat.setMode(TypeMode.NodeType.DIRECTORY)
            0
        } else {
            stat.setMode(TypeMode.NodeType.FILE, true, true, true, true, true, true, true, true, true)
            stat.size(attr.size)
            stat.atime(attr.timestamp.epochSecond)
            stat.mtime(0)
            stat.nlink(1)
            stat.uid(0)
            stat.gid(0)
            stat.blocks(((attr.size + 511L) / 512L))
            0
        }
    }

    override fun fgetattr(path: String, stat: StatWrapper, info: StructFuseFileInfo.FileInfoWrapper): Int {
        val f = File("cache$path")

        //if current path is of file
        if (f.isFile) {
            stat.setMode(TypeMode.NodeType.FILE, true, true, true, true, true, true, true, true, true)
            stat.size(f.length())
            stat.atime(f.lastModified() / 1000L)
            stat.mtime(0)
            stat.nlink(1)
            stat.uid(0)
            stat.gid(0)
            stat.blocks(((f.length() + 511L) / 512L))
            return 0
        }

        //if current file is of Directory
        else if (f.isDirectory) {
            stat.setMode(TypeMode.NodeType.DIRECTORY);
            return 0;
        }

        return -ErrorCodes.ENOENT();
    }

    override fun read(path: String, buffer: ByteBuffer, size: Long, offset: Long, info: StructFuseFileInfo.FileInfoWrapper): Int {
        val data = api.read(path)
        return if (offset + size > data.size) {
            buffer.put(data, offset.toInt(), data.size - offset.toInt())
            size.toInt()
        } else {
            buffer.put(data, offset.toInt(), size.toInt())
            size.toInt()
        }
    }

    override fun write(path: String, buf: ByteBuffer, bufSize: Long, writeOffset: Long, wrapper: StructFuseFileInfo.FileInfoWrapper): Int {
        val b = ByteArray(bufSize.toInt())
        buf.get(b)

        api.write(path, b)
        return bufSize.toInt()
    }

    override fun unlink(path: String): Int {
        api.rm(path)
        return 0
    }

    override fun rmdir(path: String): Int {
        api.rmdir(path)
        return 0
    }

    override fun opendir(path: String, info: StructFuseFileInfo.FileInfoWrapper): Int {
        return 0
    }

    override fun readdir(path: String, filler: DirectoryFiller): Int {
        println("path: $path")

        filler.add(api.readdir(path))

        return 0
    }

    override fun create(path: String, mode: TypeMode.ModeWrapper, info: StructFuseFileInfo.FileInfoWrapper): Int {
        api.create(path)
        return 0
    }

    override fun mkdir(path: String, mode: TypeMode.ModeWrapper): Int {
        api.mkdir(path)
        return 0
    }

    override fun open(path: String, info: StructFuseFileInfo.FileInfoWrapper): Int {
        return 0
    }

    override fun release(path: String, info: StructFuseFileInfo.FileInfoWrapper): Int {
        if (info.openMode() != StructFuseFileInfo.FileInfoWrapper.OpenMode.READONLY) {
            api.close(path)
        }
        return 0
    }

    override fun truncate(path: String, offset: Long): Int {
        api.truncate(path)
        return 0
    }
}
package api

import directoryservice.network.ConnectionAddress
import directoryservice.network.DirPacket
import fileserver.packets.NodePacket
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket

object Connection {

    fun dirRequest(packet: DirPacket, address: ConnectionAddress): DirPacket {
        val socket = Socket(address.address, address.port)
        val output = ObjectOutputStream(socket.getOutputStream())
        println("Sending request to dir server!")
        output.writeObject(packet)
        output.flush()
        println("Awaiting response!")
        val input = ObjectInputStream(socket.getInputStream())
        return input.readObject() as DirPacket
    }

    fun nodeRequest(packet: NodePacket, address: ConnectionAddress): NodePacket {
        val socket = Socket(address.address, address.port)
        val output = ObjectOutputStream(socket.getOutputStream())
        println("Sending request to node server!")
        output.writeObject(packet)
        output.flush()
        println("Awaiting response!")
        val input = ObjectInputStream(socket.getInputStream())
        return input.readObject() as NodePacket
    }
}
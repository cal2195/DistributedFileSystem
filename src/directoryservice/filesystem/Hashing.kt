package directoryservice.filesystem

import directoryservice.DirectoryService
import directoryservice.network.ConnectionAddress
import java.lang.Math.abs

object Hashing {

    fun getClosest(pathKey: Int): Pair<ConnectionAddress?, ConnectionAddress?> {
        var min1: Pair<Int, ConnectionAddress>? = null
        var min2: Pair<Int, ConnectionAddress>? = null

        for ((key, server) in DirectoryService.state.servers) {
            if (min1 == null) {
                min1 = Pair(abs(pathKey - key), server)
                continue
            }
            if (min1.first > abs(pathKey - key)) {
                min2 = min1
                min1 = Pair(abs(pathKey - key), server)
                continue
            }
            if (min2 == null) {
                min2 = Pair(abs(pathKey - key), server)
                continue
            }
            if (min2.first > abs(pathKey - key)) {
                min2 = Pair(abs(pathKey - key), server)
                continue
            }
        }
        return Pair(min1?.second, min2?.second)
    }
}
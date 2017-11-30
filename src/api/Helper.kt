package api

import directoryservice.network.ConnectionAddress
import java.util.*

object Helper {

    private val rand = Random()

    fun pickAddress(addresses: Pair<ConnectionAddress?, ConnectionAddress?>) : ConnectionAddress? {
        if (addresses.second == null)
            return addresses.first

        return if (rand.nextBoolean())
            addresses.first
        else
            addresses.second
    }
}
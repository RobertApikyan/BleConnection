package com.robertapikyan.bleConnection.packageEmitter

import com.robertapikyan.bleConnection.packageCollector.BleBufferedPacketCollector.Companion.END_OF_PACKAGE
import kotlin.math.ceil

class BleBufferedPacketEmitter(
    private val maxPacketSize: Int = MAX_BYTES_SIZE
) : BlePacketEmitter() {

    companion object {
        private const val MAX_BYTES_SIZE = 20
    }

    override fun emit(
        bytes: ByteArray
    ) {
        val packetsCount = ceil(bytes.size.toDouble() / maxPacketSize.toDouble()).toInt()

        for (packetIndex in 0 until packetsCount) {

            val start = packetIndex * maxPacketSize

            val end = if (start + maxPacketSize < bytes.size) {
                start + maxPacketSize
            } else {
                bytes.size
            }

            val packet = bytes.copyOfRange(start, end)

            getEmitCallback().onEmit(
                packet
            )
            Thread.sleep(200)
        }

        // emit the last byte
        getEmitCallback().onEmit(
            byteArrayOf(END_OF_PACKAGE)
        )
    }
}
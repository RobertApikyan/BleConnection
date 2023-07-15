package com.robertapikyan.bleConnection.packageCollector

class BleBufferedPacketCollector : BlePacketCollector() {

    companion object {
        const val END_OF_PACKAGE: Byte = -1
    }

    private val byteBuffer = mutableListOf<Byte>()

    override fun collect(
        bytes: ByteArray
    ) {
        if (bytes.size == 1 && bytes[0] == END_OF_PACKAGE) {
            getPacketCollectCompleteCallback().onCollectComplete(byteBuffer.toByteArray())
            byteBuffer.clear()
        } else {
            byteBuffer.addAll(bytes.asList())
            getPacketCollectCallback().onCollect(bytes)
        }
    }
}
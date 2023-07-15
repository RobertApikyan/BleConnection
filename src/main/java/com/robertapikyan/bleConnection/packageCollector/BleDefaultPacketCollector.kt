package com.robertapikyan.bleConnection.packageCollector

class BleDefaultPacketCollector: BlePacketCollector() {
    override fun collect(bytes: ByteArray) {
        getPacketCollectCallback().onCollect(bytes)
        getPacketCollectCompleteCallback().onCollectComplete(bytes)
    }
}
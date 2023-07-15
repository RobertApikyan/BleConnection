package com.robertapikyan.bleConnection.packageEmitter

class BleDefaultPacketEmitter: BlePacketEmitter() {
    override fun emit(bytes: ByteArray) {
        getEmitCallback().onEmit(bytes)
    }
}
package com.robertapikyan.bleConnection.dataHandlers.pherifieral.characteristic

import com.robertapikyan.bleConnection.codecs.BleByteArrayCodec
import com.robertapikyan.bleConnection.packageCollector.BleBufferedPacketCollector
import com.robertapikyan.bleConnection.packageCollector.BlePacketCollector
import com.robertapikyan.bleConnection.packageEmitter.BleBufferedPacketEmitter
import com.robertapikyan.bleConnection.packageEmitter.BlePacketEmitter

open class BlePerCharByteArrayDataHandler (
    emitter: BlePacketEmitter = BleBufferedPacketEmitter(),
    collector: BlePacketCollector = BleBufferedPacketCollector()
): BlePerCharDataHandler<ByteArray>(
    codec = BleByteArrayCodec(),
    emitter = emitter,
    collector = collector
)
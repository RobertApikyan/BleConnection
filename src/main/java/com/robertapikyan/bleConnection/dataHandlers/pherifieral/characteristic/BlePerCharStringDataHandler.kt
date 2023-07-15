package com.robertapikyan.bleConnection.dataHandlers.pherifieral.characteristic

import com.robertapikyan.bleConnection.codecs.BleStringCodec
import com.robertapikyan.bleConnection.packageCollector.BleBufferedPacketCollector
import com.robertapikyan.bleConnection.packageCollector.BlePacketCollector
import com.robertapikyan.bleConnection.packageEmitter.BleBufferedPacketEmitter
import com.robertapikyan.bleConnection.packageEmitter.BlePacketEmitter

open class BlePerCharStringDataHandler(
    emitter: BlePacketEmitter = BleBufferedPacketEmitter(),
    collector: BlePacketCollector = BleBufferedPacketCollector()
) : BlePerCharDataHandler<String>(
    codec = BleStringCodec(),
    emitter = emitter,
    collector = collector
)
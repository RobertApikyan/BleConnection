package com.robertapikyan.bleConnection.dataHandlers.client.characteristic

import com.robertapikyan.bleConnection.codecs.BleStringCodec
import com.robertapikyan.bleConnection.packageCollector.BleBufferedPacketCollector
import com.robertapikyan.bleConnection.packageCollector.BlePacketCollector
import com.robertapikyan.bleConnection.packageEmitter.BleBufferedPacketEmitter
import com.robertapikyan.bleConnection.packageEmitter.BlePacketEmitter

open class BleClientCharStringDataHandler(
    collector: BlePacketCollector = BleBufferedPacketCollector(),
    emitter: BlePacketEmitter = BleBufferedPacketEmitter()
) : BleClientCharDataHandler<String>(
    codec = BleStringCodec(),
    collector = collector,
    emitter = emitter
)
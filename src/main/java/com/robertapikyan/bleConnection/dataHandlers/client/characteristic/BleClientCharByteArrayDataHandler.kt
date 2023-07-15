package com.robertapikyan.bleConnection.dataHandlers.client.characteristic

import com.robertapikyan.bleConnection.codecs.BleByteArrayCodec
import com.robertapikyan.bleConnection.packageCollector.BleBufferedPacketCollector
import com.robertapikyan.bleConnection.packageCollector.BlePacketCollector
import com.robertapikyan.bleConnection.packageEmitter.BleBufferedPacketEmitter
import com.robertapikyan.bleConnection.packageEmitter.BlePacketEmitter

open class BleClientCharByteArrayDataHandler(
    collector: BlePacketCollector = BleBufferedPacketCollector(),
    emitter: BlePacketEmitter = BleBufferedPacketEmitter()
) : BleClientCharDataHandler<ByteArray>(
    codec = BleByteArrayCodec(),
    collector = collector,
    emitter = emitter
)
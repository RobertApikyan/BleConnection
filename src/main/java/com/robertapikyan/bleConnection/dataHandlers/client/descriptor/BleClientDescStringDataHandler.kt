package com.robertapikyan.bleConnection.dataHandlers.client.descriptor

import com.robertapikyan.bleConnection.codecs.BleStringCodec
import com.robertapikyan.bleConnection.packageEmitter.BleBufferedPacketEmitter
import com.robertapikyan.bleConnection.packageEmitter.BlePacketEmitter

open class BleClientDescStringDataHandler(
    emitter: BlePacketEmitter = BleBufferedPacketEmitter()
)  : BleClientDescDataHandler<String>(
    codec = BleStringCodec(),
    emitter = emitter
)
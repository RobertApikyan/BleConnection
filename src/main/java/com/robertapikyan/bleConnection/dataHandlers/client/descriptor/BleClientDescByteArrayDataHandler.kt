package com.robertapikyan.bleConnection.dataHandlers.client.descriptor

import com.robertapikyan.bleConnection.codecs.BleByteArrayCodec
import com.robertapikyan.bleConnection.packageEmitter.BleBufferedPacketEmitter
import com.robertapikyan.bleConnection.packageEmitter.BlePacketEmitter

open class BleClientDescByteArrayDataHandler(
    emitter: BlePacketEmitter = BleBufferedPacketEmitter()
) : BleClientDescDataHandler<ByteArray>(
    codec = BleByteArrayCodec(),
    emitter = emitter
)
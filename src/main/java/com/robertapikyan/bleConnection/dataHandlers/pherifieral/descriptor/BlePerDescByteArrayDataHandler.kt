package com.robertapikyan.bleConnection.dataHandlers.pherifieral.descriptor

import com.robertapikyan.bleConnection.codecs.BleByteArrayCodec
import com.robertapikyan.bleConnection.packageCollector.BleBufferedPacketCollector
import com.robertapikyan.bleConnection.packageCollector.BlePacketCollector

open class BlePerDescByteArrayDataHandler(
    collector: BlePacketCollector = BleBufferedPacketCollector()
) : BlePerDescDataHandler<ByteArray>(
        codec = BleByteArrayCodec(),
        collector = collector
    )
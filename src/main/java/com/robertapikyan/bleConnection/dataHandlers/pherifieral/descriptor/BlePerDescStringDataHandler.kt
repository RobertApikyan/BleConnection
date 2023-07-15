package com.robertapikyan.bleConnection.dataHandlers.pherifieral.descriptor

import com.robertapikyan.bleConnection.codecs.BleStringCodec
import com.robertapikyan.bleConnection.packageCollector.BleBufferedPacketCollector
import com.robertapikyan.bleConnection.packageCollector.BlePacketCollector

open class BlePerDescStringDataHandler(
    collector: BlePacketCollector = BleBufferedPacketCollector()
) : BlePerDescDataHandler<String>(
    codec = BleStringCodec(),
    collector = collector
)
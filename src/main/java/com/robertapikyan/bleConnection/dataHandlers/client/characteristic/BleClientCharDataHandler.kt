package com.robertapikyan.bleConnection.dataHandlers.client.characteristic

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import com.robertapikyan.bleConnection.codecs.BleCodec
import com.robertapikyan.bleConnection.gattCallbacks.BluetoothGattCallbacks
import com.robertapikyan.bleConnection.packageCollector.BlePacketCollector
import com.robertapikyan.bleConnection.packageEmitter.BlePacketEmitter

open class BleClientCharDataHandler<D>(
    private val codec: BleCodec<D>,
    private val collector: BlePacketCollector,
    private val emitter: BlePacketEmitter
) : BluetoothGattCallbacks.OnCharacteristicChangedCallback,
    BluetoothGattCallbacks.OnCharacteristicReadCallback,
    BluetoothGattCallbacks.OnCharacteristicWriteCallback {

    protected lateinit var characteristic: BluetoothGattCharacteristic
    protected lateinit var bluetoothGatt: BluetoothGatt

    fun setupWithCharacteristic(characteristic: BluetoothGattCharacteristic) {
        this.characteristic = characteristic
    }

    fun setupWithGatt(bluetoothGatt: BluetoothGatt) {
        this.bluetoothGatt = bluetoothGatt
    }

    final override fun onCharacteristicWrite(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?,
        status: Int
    ) {
        if (this.characteristic.uuid == characteristic?.uuid) {
            onWrite(gatt, characteristic, status)
        }
    }

    final override fun onCharacteristicRead(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?,
        status: Int
    ) {
        if (this.characteristic.uuid == characteristic?.uuid) {
            onRead(gatt, characteristic, status)
        }
    }

    override fun onCharacteristicChanged(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?
    ) {
        if (this.characteristic.uuid == characteristic?.uuid) {
            onChanged(gatt, characteristic)
        }
    }

    fun write(byteArray: ByteArray) = with(bluetoothGatt) {
        characteristic.value = byteArray
        writeCharacteristic(characteristic)
    }

    fun read() {
        bluetoothGatt.readCharacteristic(characteristic)
    }

    open fun onChanged(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?
    ) {
        collector.getCollectCompleteNotifier()
            .clearAndAdd(object : BlePacketCollector.OnPacketCollectCompleteCallback {
                override fun onCollectComplete(bytes: ByteArray) {
                    val data = codec.fromByteArray(bytes)
                    onDataChanged(data)
                }
            })
        if (characteristic?.value != null)
            collector.collect(characteristic.value)
    }

    open fun onRead(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?,
        status: Int
    ) {
        val data = codec.fromByteArray(characteristic?.value ?: "".toByteArray())
        onReadData(gatt, characteristic, status, data)
    }

    open fun onWrite(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?,
        status: Int
    ) {
        val data = codec.fromByteArray(characteristic?.value ?: "".toByteArray())
        onWriteData(gatt, characteristic, status, data)
    }

    open fun onReadData(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?,
        status: Int,
        data: D
    ) {

    }

    open fun onWriteData(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?,
        status: Int,
        data: D
    ) {

    }

    open fun onDataChanged(data: D) {

    }

    fun writeData(data: D) {
        val byteArray = codec.toByteArray(data)
        emitter.getEmitNotifier().clearAndAdd(object : BlePacketEmitter.OnEmitCallback {
            override fun onEmit(bytes: ByteArray) {
                write(bytes)
            }
        })
        emitter.emit(byteArray)
    }
}
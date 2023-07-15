package com.robertapikyan.bleConnection.dataHandlers.client.descriptor

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattDescriptor
import com.robertapikyan.bleConnection.BleStatusCodes
import com.robertapikyan.bleConnection.codecs.BleCodec
import com.robertapikyan.bleConnection.gattCallbacks.BluetoothGattCallbacks
import com.robertapikyan.bleConnection.packageEmitter.BleBufferedPacketEmitter
import com.robertapikyan.bleConnection.packageEmitter.BlePacketEmitter

open class BleClientDescDataHandler<D>(
    private val codec: BleCodec<D>,
    private val emitter: BlePacketEmitter
) :
    BluetoothGattCallbacks.OnDescriptorReadCallback,
    BluetoothGattCallbacks.OnDescriptorWriteCallback {

    protected lateinit var desc: BluetoothGattDescriptor
    protected lateinit var bluetoothGatt: BluetoothGatt

    fun setupWithDescriptor(descriptor: BluetoothGattDescriptor) {
        this.desc = descriptor
    }

    fun setupWithGatt(bluetoothGatt: BluetoothGatt) {
        this.bluetoothGatt = bluetoothGatt
    }

    final override fun onDescriptorRead(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
        if (this.desc.uuid == descriptor?.uuid) {
            onRead(gatt, descriptor, status)
        }
    }

    final override fun onDescriptorWrite(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
        if (this.desc.uuid == descriptor?.uuid) {
            onWrite(gatt, descriptor, status)
        }
    }

    open fun onRead(
        gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int
    ) {
        val data = codec.fromByteArray(descriptor?.value ?: "".toByteArray())
        if (data!= BleStatusCodes.NONE)
            onReadData(gatt, descriptor, status, data)
    }

    open fun onWrite(
        gatt: BluetoothGatt?,
        descriptor: BluetoothGattDescriptor?,
        status: Int
    ) {
        val data = codec.fromByteArray(descriptor?.value ?: "".toByteArray())
        onWriteData(gatt, descriptor, status, data)
    }

    open fun onReadData(
        gatt: BluetoothGatt?,
        descriptor: BluetoothGattDescriptor?,
        status: Int,
        data: D
    ) {

    }

    open fun onWriteData(
        gatt: BluetoothGatt?,
        descriptor: BluetoothGattDescriptor?,
        status: Int,
        data: D
    ) {

    }

    fun write(byteArray: ByteArray) {
        with(bluetoothGatt) {
            desc.value = byteArray
            writeDescriptor(desc)
        }
    }

    fun read() {
        bluetoothGatt.readDescriptor(desc)
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
package com.robertapikyan.bleConnection.dataHandlers.pherifieral.descriptor

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattServer
import android.util.Log
import com.robertapikyan.bleConnection.BleStatusCodes
import com.robertapikyan.bleConnection.codecs.BleCodec
import com.robertapikyan.bleConnection.gattServerCallbacks.BluetoothGattServerCallbacks
import com.robertapikyan.bleConnection.packageCollector.BleBufferedPacketCollector
import com.robertapikyan.bleConnection.packageCollector.BlePacketCollector

open class BlePerDescDataHandler<D>(
    private val codec: BleCodec<D>,
    private val collector: BlePacketCollector
) : BluetoothGattServerCallbacks.OnDescriptorReadRequestCallback,
    BluetoothGattServerCallbacks.OnDescriptorWriteRequestCallback {

    protected lateinit var descriptor: BluetoothGattDescriptor
    protected lateinit var gattServer: BluetoothGattServer

    fun setupWithDescriptor(descriptor: BluetoothGattDescriptor) {
        this.descriptor = descriptor
    }

    fun setupWithGattServer(gattServer: BluetoothGattServer) {
        this.gattServer = gattServer
    }

    final override fun onDescriptorReadRequest(
        device: BluetoothDevice?,
        requestId: Int,
        offset: Int,
        descriptor: BluetoothGattDescriptor?
    ) {
        if (this.descriptor.uuid == descriptor?.uuid) {
            onReadRequest(device, requestId, offset, descriptor)
        }
    }

    final override fun onDescriptorWriteRequest(
        device: BluetoothDevice?,
        requestId: Int,
        descriptor: BluetoothGattDescriptor?,
        preparedWrite: Boolean,
        responseNeeded: Boolean,
        offset: Int,
        value: ByteArray?
    ) {
        if (this.descriptor.uuid == descriptor?.uuid) {
            onWriteRequest(
                device,
                requestId,
                descriptor,
                preparedWrite,
                responseNeeded,
                offset,
                value
            )
        }
    }

    protected open fun onReadRequest(
        device: BluetoothDevice?,
        requestId: Int,
        offset: Int,
        descriptor: BluetoothGattDescriptor?
    ) {

    }

    open fun onWriteRequest(
        device: BluetoothDevice?,
        requestId: Int,
        descriptor: BluetoothGattDescriptor?,
        preparedWrite: Boolean,
        responseNeeded: Boolean,
        offset: Int,
        value: ByteArray?
    ) {
        Log.d("BlePerDesc","onDescriptorWriteRequest")

        if (value != null) with(collector) {
            getCollectCompleteNotifier().clearAndAdd(
                object : BlePacketCollector.OnPacketCollectCompleteCallback {
                    override fun onCollectComplete(bytes: ByteArray) = onWriteDataRequest(
                        device,
                        requestId,
                        descriptor,
                        preparedWrite,
                        responseNeeded,
                        offset,
                        codec.fromByteArray(bytes)
                    )
                }
            )
            getCollectNotifier().clearAndAdd(object : BlePacketCollector.OnPacketCollectCallback {
                override fun onCollect(bytes: ByteArray) {
                    sendResponse(device, requestId, BluetoothGatt.GATT_FAILURE, 0,
                        BleStatusCodes.NONE
                    )
                }
            })
            collect(value)
        }
    }

    protected open fun onWriteDataRequest(
        device: BluetoothDevice?,
        requestId: Int,
        descriptor: BluetoothGattDescriptor?,
        preparedWrite: Boolean,
        responseNeeded: Boolean,
        offset: Int,
        data: D
    ) {

    }

    protected fun sendResponse(
        device: BluetoothDevice?,
        requestId: Int,
        status: Int,
        offset: Int,
        data: D
    ) {
        val byteArray = codec.toByteArray(data)
        sendResponse(device, requestId, status, offset, byteArray)
    }

    open fun sendResponse(
        device: BluetoothDevice?,
        requestId: Int,
        status: Int,
        offset: Int,
        byteArray: ByteArray
    ) {
        gattServer.sendResponse(device, requestId, status, offset, byteArray)
    }
}
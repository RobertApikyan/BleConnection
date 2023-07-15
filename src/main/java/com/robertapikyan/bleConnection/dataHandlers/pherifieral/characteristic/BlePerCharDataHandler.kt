package com.robertapikyan.bleConnection.dataHandlers.pherifieral.characteristic

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattServer
import com.robertapikyan.bleConnection.BleStatusCodes
import com.robertapikyan.bleConnection.codecs.BleCodec
import com.robertapikyan.bleConnection.gattServerCallbacks.BluetoothGattServerCallbacks
import com.robertapikyan.bleConnection.packageCollector.BlePacketCollector
import com.robertapikyan.bleConnection.packageEmitter.BlePacketEmitter

open class BlePerCharDataHandler<D>(
    private val codec: BleCodec<D>,
    private val emitter: BlePacketEmitter,
    private val collector: BlePacketCollector
) : BluetoothGattServerCallbacks.OnCharacteristicWriteRequestCallback,
    BluetoothGattServerCallbacks.OnCharacteristicReadRequestCallback {

    protected lateinit var characteristic: BluetoothGattCharacteristic
    protected lateinit var gattServer: BluetoothGattServer

    fun setupWithCharacteristic(characteristic: BluetoothGattCharacteristic) {
        this.characteristic = characteristic
    }

    fun setupWithGattServer(gattServer: BluetoothGattServer) {
        this.gattServer = gattServer
    }

    final override fun onCharacteristicReadRequest(
        device: BluetoothDevice?,
        requestId: Int,
        offset: Int,
        characteristic: BluetoothGattCharacteristic?
    ) {
        if (this.characteristic.uuid == characteristic?.uuid) {
            onReadRequest(device, requestId, offset, characteristic)
        }
    }

    final override fun onCharacteristicWriteRequest(
        device: BluetoothDevice?,
        requestId: Int,
        characteristic: BluetoothGattCharacteristic?,
        preparedWrite: Boolean,
        responseNeeded: Boolean,
        offset: Int,
        value: ByteArray?
    ) {
        if (this.characteristic.uuid == characteristic?.uuid) {
            onWriteRequest(
                device,
                requestId,
                characteristic,
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
        characteristic: BluetoothGattCharacteristic?
    ) {

    }

    protected open fun onWriteRequest(
        device: BluetoothDevice?,
        requestId: Int,
        characteristic: BluetoothGattCharacteristic?,
        preparedWrite: Boolean,
        responseNeeded: Boolean,
        offset: Int,
        value: ByteArray?
    ) {
        if (value != null) with(collector) {
            getCollectCompleteNotifier().clearAndAdd(
                object : BlePacketCollector.OnPacketCollectCompleteCallback {
                    override fun onCollectComplete(bytes: ByteArray) = onWriteDataRequest(
                        device,
                        requestId,
                        characteristic,
                        preparedWrite,
                        responseNeeded,
                        offset,
                        codec.fromByteArray(bytes)
                    )
                }
            )
            getCollectNotifier().clearAndAdd(object : BlePacketCollector.OnPacketCollectCallback {
                override fun onCollect(bytes: ByteArray) {
                    sendResponse(
                        device, requestId, BluetoothGatt.GATT_FAILURE, offset,
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
        characteristic: BluetoothGattCharacteristic?,
        preparedWrite: Boolean,
        responseNeeded: Boolean,
        offset: Int,
        data: D
    ) {

    }

    fun notifyData(device: BluetoothDevice, data: D) {
        val byteArray = codec.toByteArray(data)
        with(emitter) {
            getEmitNotifier().clearAndAdd(object : BlePacketEmitter.OnEmitCallback {
                override fun onEmit(bytes: ByteArray) {
                    notifyData(device, bytes)
                }
            })
            emit(byteArray)
        }
    }

    fun notifyData(device: BluetoothDevice, byteArray: ByteArray) {
        try {
            characteristic.value = byteArray
            gattServer.notifyCharacteristicChanged(device, characteristic, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendResponse(
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
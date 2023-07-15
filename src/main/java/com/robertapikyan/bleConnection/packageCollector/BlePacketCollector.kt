package com.robertapikyan.bleConnection.packageCollector

import com.robertapikyan.bleConnection.utils.Notifier

abstract class BlePacketCollector {

    interface OnPacketCollectCompleteCallback {
        fun onCollectComplete(
            bytes: ByteArray
        )
    }

    interface OnPacketCollectCallback{
        fun onCollect(bytes: ByteArray)
    }

    private val collectCompleteNotifier =
        object : Notifier<OnPacketCollectCompleteCallback>(),
            OnPacketCollectCompleteCallback {
            override fun onCollectComplete(bytes: ByteArray) {
                for (listener in getListeners()) {
                    listener.onCollectComplete(
                        bytes
                    )
                }
            }
        }

    private val collectNotifier =
        object : Notifier<OnPacketCollectCallback>(),
            OnPacketCollectCallback {
            override fun onCollect(bytes: ByteArray) {
                for (listener in getListeners()) {
                    listener.onCollect(bytes)
                }
            }
        }

    fun getCollectCompleteNotifier(): Notifier<OnPacketCollectCompleteCallback> = collectCompleteNotifier

    fun getCollectNotifier(): Notifier<OnPacketCollectCallback> = collectNotifier

    protected fun getPacketCollectCallback():OnPacketCollectCallback = collectNotifier

    protected fun getPacketCollectCompleteCallback():OnPacketCollectCompleteCallback = collectCompleteNotifier

    abstract fun collect(bytes: ByteArray)
}
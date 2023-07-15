package com.robertapikyan.bleConnection.packageEmitter

import com.robertapikyan.bleConnection.utils.Notifier

abstract class BlePacketEmitter {

    interface OnEmitCallback {
        fun onEmit(
            bytes: ByteArray
        )
    }

    interface OnEmitCompleteCallback {
        fun onEmitComplete()
    }

    abstract fun emit(
        bytes: ByteArray
    )

    private val emitNotifier = object : Notifier<OnEmitCallback>(), OnEmitCallback {
        override fun onEmit(bytes: ByteArray) {
            for (listener in getListeners()) {
                listener.onEmit(bytes)
            }
        }
    }

    private val emitCompleteNotifier = object : Notifier<OnEmitCompleteCallback>(),
        OnEmitCompleteCallback {
        override fun onEmitComplete() {
            for (listener in getListeners()) {
                listener.onEmitComplete()
            }
        }
    }

    fun getEmitNotifier(): Notifier<OnEmitCallback> = emitNotifier

    fun getEmitCompleteNotifier(): Notifier<OnEmitCompleteCallback> = emitCompleteNotifier

    protected fun getEmitCallback(): OnEmitCallback = emitNotifier

    protected fun getEmitCompleteCallback(): OnEmitCompleteCallback = emitCompleteNotifier
}
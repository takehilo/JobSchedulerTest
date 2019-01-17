package com.example.jobschedulertest

import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver

class MyResultReceiver(handler: Handler, private val callback: Callback): ResultReceiver(handler) {
    companion object {
        const val RESULT_CODE_SUCCESS = 0
    }

    override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
        callback.onEvent()
    }

    interface Callback {
        fun onEvent()
    }
}
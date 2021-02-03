package com.czl.lib_base.extension

import android.content.Context
import android.media.MediaScannerConnection
import android.media.MediaScannerConnection.MediaScannerConnectionClient
import android.net.Uri
import java.io.File

class SingleMediaScanner(
    context: Context?,
    private val mFile: File,
    private val listener: ScanListener
) : MediaScannerConnectionClient {
    interface ScanListener {
        fun onScanFinish()
    }

    private val mMs: MediaScannerConnection = MediaScannerConnection(context, this)

    override fun onMediaScannerConnected() {
        mMs.scanFile(mFile.absolutePath, null)
    }

    override fun onScanCompleted(path: String, uri: Uri) {
        mMs.disconnect()
        listener.onScanFinish()
    }

    init {
        mMs.connect()
    }
}
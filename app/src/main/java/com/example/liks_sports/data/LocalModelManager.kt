package com.example.liks_sports.data

import android.content.Context
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/**
 * Downloads (and manages) the on-device Gemma 4 E2B-it LiteRT-LM model file into
 * [Context.filesDir]. The file is roughly 2.58 GB, so the download streams to
 * disk and reports progress. It is cancellable via coroutine cancellation.
 */
class LocalModelManager(private val context: Context) {

    private val filesDir: File get() = context.filesDir

    fun modelFile(): File = File(filesDir, SettingsStore.DEFAULT_LOCAL_MODEL_FILE)

    fun isModelPresent(): Boolean {
        val f = modelFile()
        return f.exists() && f.length() > MIN_VALID_MODEL_BYTES
    }

    fun deleteModel() {
        val f = modelFile()
        if (f.exists()) f.delete()
        File(filesDir, SettingsStore.DEFAULT_LOCAL_MODEL_FILE + TMP_SUFFIX).delete()
    }

    /**
     * Downloads the model into filesDir, returning the absolute path on success.
     * Throws [IOException] on network/HTTP errors; propagates [CancellationException].
     */
    suspend fun download(onProgress: (percent: Int) -> Unit): String = withContext(Dispatchers.IO) {
        val tmp = File(filesDir, SettingsStore.DEFAULT_LOCAL_MODEL_FILE + TMP_SUFFIX)
        val finalFile = modelFile()
        if (finalFile.exists() && finalFile.length() > MIN_VALID_MODEL_BYTES) {
            return@withContext finalFile.absolutePath
        }
        filesDir.mkdirs()
        if (tmp.exists()) tmp.delete()

        val url = URL(SettingsStore.DEFAULT_LOCAL_MODEL_URL)
        val connection = url.openConnection() as HttpURLConnection
        try {
            connection.requestMethod = "GET"
            connection.setRequestProperty("User-Agent", "liks-sports/1.0 (Android)")
            connection.connectTimeout = 30_000
            connection.readTimeout = 60_000
            connection.instanceFollowRedirects = true
            val code = connection.responseCode
            if (code != HttpURLConnection.HTTP_OK) {
                throw IOException("Download failed: HTTP $code")
            }
            val total = connection.contentLengthLong.let { if (it > 0) it else EXPECTED_SIZE }
            var downloaded = 0L
            var lastReported = -1
            connection.inputStream.use { input ->
                FileOutputStream(tmp).use { out ->
                    val buf = ByteArray(BUFFER_SIZE)
                    while (true) {
                        ensureActive()
                        val read = input.read(buf)
                        if (read <= 0) break
                        out.write(buf, 0, read)
                        downloaded += read
                        if (total > 0) {
                            val pct = ((downloaded * 100) / total).toInt().coerceIn(0, 100)
                            if (pct != lastReported) {
                                lastReported = pct
                                withContext(Dispatchers.Main) { onProgress(pct) }
                            }
                        }
                    }
                }
            }
            if (downloaded < MIN_VALID_MODEL_BYTES) {
                tmp.delete()
                throw IOException("Downloaded file too small (${downloaded} bytes); aborting.")
            }
            if (finalFile.exists()) finalFile.delete()
            if (!tmp.renameTo(finalFile)) {
                tmp.copyTo(finalFile, overwrite = true)
                tmp.delete()
            }
            finalFile.absolutePath
        } finally {
            connection.disconnect()
        }
    }

    companion object {
        private const val BUFFER_SIZE = 64 * 1024
        private const val TMP_SUFFIX = ".tmp"
        private const val EXPECTED_SIZE = 2_583_000_000L
        private const val MIN_VALID_MODEL_BYTES = 100L * 1024 * 1024
    }
}

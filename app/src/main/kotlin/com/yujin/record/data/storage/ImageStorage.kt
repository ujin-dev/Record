package com.yujin.record.data.storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.net.toUri
import com.yujin.record.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageStorage @Inject constructor(
    @param:ApplicationContext
    private val context: Context
) {
    private val imageDir: File
        get() = File(context.filesDir, "images").apply {
            mkdirs()
        }

    suspend fun backupImage(sourceUri: Uri): Uri = withContext(Dispatchers.IO) {
        val fileName = "${UUID.randomUUID()}"
        val tempFile = File(imageDir, "$fileName.tmp")
        val targetFile = File(imageDir, "$fileName.jpg")

        val inputStream = when (sourceUri.scheme?.lowercase()) {
            "http", "https" -> {
                val connection = URL(sourceUri.toString()).openConnection() as HttpURLConnection
                connection.connectTimeout = 15_000
                connection.readTimeout = 15_000
                connection.requestMethod = "GET"
                connection.connect()

                if (connection.responseCode !in 200..299) {
                    connection.disconnect()
                    throw IOException(
                        context.getString(
                            R.string.exception_image_download_failed,
                            connection.responseCode
                        )
                    )
                }

                connection.inputStream
            }

            else -> {
                context.contentResolver.openInputStream(sourceUri)
            }
        }

        try {
            requireNotNull(inputStream) {
                context.getString(R.string.exception_image_input_stream_unavailable, sourceUri)
            }
                .use { input ->
                    tempFile.outputStream().use { output -> input.copyTo(output) }
                }
            resizeAndSave(tempFile, targetFile)
        } catch (e: Exception) {
            targetFile.delete()
            throw e
        } finally {
            tempFile.delete()
        }

        targetFile.toUri()
    }

    suspend fun deleteImages(uris: List<Uri>) = withContext(Dispatchers.IO) {
        uris.forEach { uri ->
            File(requireNotNull(uri.path)).delete()
        }
    }

    fun isBackupImage(uri: Uri): Boolean =
        uri.path?.let { path ->
            path.startsWith(imageDir.absolutePath) && File(path).exists()
        } == true

    private fun resizeAndSave(input: File, targetFile: File) {
        val bounds = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        input.inputStream().use { BitmapFactory.decodeStream(it, null, bounds) }

        require(bounds.outWidth > 0 && bounds.outHeight > 0) {
            context.getString(R.string.exception_image_file_unreadable)
        }

        val maxWidth = 600
        val sampleSize = (bounds.outWidth / maxWidth).coerceAtLeast(1)

        val bitmap = BitmapFactory.Options().run {
            inSampleSize = sampleSize
            input.inputStream().use { BitmapFactory.decodeStream(it, null, this) }
        } ?: throw IOException(context.getString(R.string.exception_image_decode_failed))

        try {
            FileOutputStream(targetFile).use { output ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, output)
            }
        } finally {
            bitmap.recycle()
        }
    }
}

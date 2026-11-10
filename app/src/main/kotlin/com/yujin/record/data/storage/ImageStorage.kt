package com.yujin.record.data.storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.net.toUri
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
                    throw IOException("이미지 다운로드 실패: HTTP ${connection.responseCode}")
                }

                connection.inputStream
            }

            else -> {
                context.contentResolver.openInputStream(sourceUri)
            }
        }

        try {
            requireNotNull(inputStream) { "이미지 입력 스트림을 열 수 없습니다: $sourceUri" }
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
            "이미지 파일을 읽을 수 없습니다."
        }

        val maxWidth = 600
        val sampleSize = (bounds.outWidth / maxWidth).coerceAtLeast(1)

        val bitmap = BitmapFactory.Options().run {
            inSampleSize = sampleSize
            input.inputStream().use { BitmapFactory.decodeStream(it, null, this) }
        } ?: throw IOException("이미지 디코딩에 실패했습니다.")

        try {
            FileOutputStream(targetFile).use { output ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, output)
            }
        } finally {
            bitmap.recycle()
        }
    }
}
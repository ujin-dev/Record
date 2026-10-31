package com.yujin.record.core.data

import android.content.Context
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
        val fileName = "record_${System.currentTimeMillis()}.jpg"
        val targetFile = File(imageDir, fileName)

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
                    FileOutputStream(targetFile).use { output ->
                        input.copyTo(output)
                    }
                }
        } catch (e: Exception) {
            targetFile.delete()
            throw e
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
}
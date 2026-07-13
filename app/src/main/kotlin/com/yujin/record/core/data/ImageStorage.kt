package com.yujin.record.core.data

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
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

        context.contentResolver.openInputStream(sourceUri).use { input ->
            FileOutputStream(targetFile).use { output ->
                input?.copyTo(output)
            }
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
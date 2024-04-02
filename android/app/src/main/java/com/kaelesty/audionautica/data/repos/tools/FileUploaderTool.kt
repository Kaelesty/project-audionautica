package com.kaelesty.audionautica.data.repos.tools

import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

class FileUploaderTool @Inject constructor(
	private val application: Application,
	private val contentResolver: ContentResolver,
) {

	fun getMultipartOfFile(uri: Uri, mediaType: String): MultipartBody.Part {
		val tempFile = File(application.cacheDir, "tempfile")
		try {
			val inputstream: InputStream = contentResolver.openInputStream(uri) !!
			val output = FileOutputStream(tempFile)
			val buffer = ByteArray(1024)
			var size: Int
			while (inputstream.read(buffer).also { size = it } != - 1) {
				output.write(buffer, 0, size)
			}
			inputstream.close()
			output.close()
		} catch (e: IOException) {
			Log.e("MYTAG", "COPYCODE ERROR")
		}
		val file = tempFile
		val requestBody = RequestBody.create(
			mediaType.toMediaTypeOrNull(),
			file
		)
		return MultipartBody.Part.create(requestBody)
	}
}
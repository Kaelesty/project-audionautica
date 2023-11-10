package com.kaelesty.audionautica.presentation.activities

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.net.toFile
import com.kaelesty.audionautica.presentation.composables.addtrack.AddTrackScreen
import com.kaelesty.audionautica.presentation.ui.theme.AudionauticaTheme
import com.kaelesty.audionautica.presentation.viewmodels.AddTrackViewModel
import com.kaelesty.audionautica.system.ModifiedApplication
import java.io.File
import javax.inject.Inject


class AddTrackActivity : ComponentActivity() {

	enum class FilesToBrowse(val requestCode: Int) {
		POSTER(2000), MUSIC(3000)
	}

	@Inject
	lateinit var viewModel: AddTrackViewModel

	private val component by lazy {
		(application as ModifiedApplication).component
	}

	@RequiresApi(Build.VERSION_CODES.TIRAMISU)
	override fun onCreate(savedInstanceState: Bundle?) {
		component.inject(this@AddTrackActivity)
		logAllMusicFiles()
		super.onCreate(savedInstanceState)

		viewModel.finish.observe(this) {
			finish()
		}
		setContent {
			AudionauticaTheme {
				AddTrackScreen(
					viewModel = viewModel,
					fileBrowser = {
						askPermissions()
						browseFile(it)
					}
				)
			}
		}
	}

	@RequiresApi(Build.VERSION_CODES.TIRAMISU)
	private fun askPermissions() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			val audioPermission = ActivityCompat.checkSelfPermission(
				this.applicationContext, android.Manifest.permission.READ_MEDIA_AUDIO
			)
			val imagePermission = ActivityCompat.checkSelfPermission(
				this.applicationContext, android.Manifest.permission.READ_MEDIA_AUDIO
			)
			val storagePermission = ActivityCompat.checkSelfPermission(
				this.applicationContext, android.Manifest.permission.READ_EXTERNAL_STORAGE
			)
			if (audioPermission != PackageManager.PERMISSION_GRANTED
				|| imagePermission != PackageManager.PERMISSION_GRANTED
				|| storagePermission != PackageManager.PERMISSION_GRANTED) {
				this.requestPermissions(
					arrayOf(
						android.Manifest.permission.READ_MEDIA_AUDIO,
						android.Manifest.permission.READ_MEDIA_IMAGES,
						android.Manifest.permission.READ_EXTERNAL_STORAGE
					),
					1000
				)
			}
		}
	}

	@RequiresApi(Build.VERSION_CODES.TIRAMISU)
	private fun logAllMusicFiles() {
		askPermissions()
		Log.e("MYTAG", "logAllMusicFiles")
		Log.e("MYTAG", "file://" + Environment.getExternalStorageDirectory())
		//sendBroadcast(Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
				//+ Environment.getExternalStorageDirectory())))
		val collection =
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
				MediaStore.Audio.Media.getContentUri(
					MediaStore.VOLUME_EXTERNAL
				)
			} else {
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
			}
		val projection = arrayOf(
			MediaStore.Audio.Media._ID,
			MediaStore.Audio.Media.DISPLAY_NAME,
			MediaStore.Audio.Media.DURATION,
			MediaStore.Audio.Media.SIZE
		)
		val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"
		val query = contentResolver.query(
			collection,
			projection,
			null,
			null,
			sortOrder
		)
		query?.use { cursor ->
			val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
			val nameColumn =
				cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
			val durationColumn =
				cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
			val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
			while (cursor.moveToNext()) {
				val id = cursor.getLong(idColumn)
				val name = cursor.getString(nameColumn)
				val duration = cursor.getInt(durationColumn)
				val size = cursor.getInt(sizeColumn)

				val contentUri: Uri = ContentUris.withAppendedId(
					MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
					id
				)
				Log.e("MYTAG", name)
				viewModel.musicFileBrowsed(contentUri)
			}
		}


	}

	private fun browseFile(fileType: FilesToBrowse) {
//		val chooseFileIntent = Intent.createChooser(
//			Intent(Intent.ACTION_GET_CONTENT).apply {
//				type = "*/*"
//				addCategory(Intent.CATEGORY_OPENABLE)
//			},
//			"Choose a file"
//		)
//		startActivityForResult(
//			chooseFileIntent,
//			fileType.requestCode
//		)
		val intent = Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
		intent.type = "Audio/*"
		startActivityForResult(
			Intent.createChooser(
				intent,
				"Selecione a foto "
			), FilesToBrowse.MUSIC.requestCode
		)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

		val uri: Uri = data?.data ?: throw IllegalStateException("Illegal!")
		val path = uri.path.toString()
		val file = File(path)
		Log.e("MYTAG1", file.absolutePath)
		Log.e("MYTAG1", file.name)
		Log.e("MYTAG1", file.canonicalPath)
		Log.e("MYTAG1", file.extension)

		super.onActivityResult(requestCode, resultCode, data)
		when (requestCode) {
			FilesToBrowse.MUSIC.requestCode -> { viewModel.musicFileBrowsed(uri) }
			FilesToBrowse.POSTER.requestCode -> { viewModel.posterFileBrowsed(data?.data ?: Uri.EMPTY) }
		}
	}


	companion object {
		fun newIntent(context: Context) = Intent(context, AddTrackActivity::class.java)
	}
}
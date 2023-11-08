package com.kaelesty.audionautica.presentation.activities

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.kaelesty.audionautica.presentation.composables.addtrack.AddTrackScreen
import com.kaelesty.audionautica.presentation.ui.theme.AudionauticaTheme
import com.kaelesty.audionautica.presentation.viewmodels.AddTrackViewModel
import com.kaelesty.audionautica.system.ModifiedApplication
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

		super.onCreate(savedInstanceState)
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
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
			val audioPermission = ActivityCompat.checkSelfPermission(
				this.applicationContext, android.Manifest.permission.READ_MEDIA_AUDIO
			)
			val imagePermission = ActivityCompat.checkSelfPermission(
				this.applicationContext, android.Manifest.permission.READ_MEDIA_AUDIO
			)
			if (audioPermission != PackageManager.PERMISSION_GRANTED
				|| imagePermission != PackageManager.PERMISSION_GRANTED) {
				this.requestPermissions(
					arrayOf(
						android.Manifest.permission.READ_MEDIA_AUDIO,
						android.Manifest.permission.READ_MEDIA_IMAGES
					),
					1000
				)
			}
		}
	}

	private fun browseFile(fileType: FilesToBrowse) {
		val chooseFileIntent = Intent.createChooser(
			Intent(Intent.ACTION_GET_CONTENT).apply {
				type = "*/*"
				addCategory(Intent.CATEGORY_OPENABLE)
			},
			"Choose a file"
		)
		startActivityForResult(
			chooseFileIntent,
			fileType.requestCode
		)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		when (requestCode) {
			FilesToBrowse.MUSIC.requestCode -> { viewModel.musicFileBrowsed(data?.data ?: Uri.EMPTY) }
			FilesToBrowse.POSTER.requestCode -> { viewModel.posterFileBrowsed(data?.data ?: Uri.EMPTY) }
		}
	}


	companion object {
		fun newIntent(context: Context) = Intent(context, AddTrackActivity::class.java)
	}
}
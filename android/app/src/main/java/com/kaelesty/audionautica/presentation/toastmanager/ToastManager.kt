package com.kaelesty.audionautica.presentation.toastmanager

import android.app.Application
import android.widget.Toast
import javax.inject.Inject

class ToastManager @Inject constructor(
	private val application: Application
) {

	fun showToast(message: String) {
		Toast.makeText(application, message, Toast.LENGTH_SHORT).show()
	}
}
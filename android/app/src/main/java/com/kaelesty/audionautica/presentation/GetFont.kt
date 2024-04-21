package com.kaelesty.audionautica.presentation

import android.os.Build
import androidx.compose.ui.text.font.FontFamily
import com.kaelesty.audionautica.presentation.ui.fonts.SpaceGrotesk

fun getFont(): FontFamily {
	return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
		SpaceGrotesk
	}
	else {
		FontFamily.Monospace
	}
}
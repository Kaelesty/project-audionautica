package com.kaelesty.audionautica.presentation.ui.fonts

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.kaelesty.audionautica.R

@RequiresApi(Build.VERSION_CODES.Q)
val SpaceGrotesk = FontFamily(
	Font(R.font.spacegrotesk_bold, FontWeight.Bold, FontStyle.Normal),
	Font(R.font.spacegrotesk_semibold, FontWeight.SemiBold, FontStyle.Normal),
	Font(R.font.spacegrotesk_medium, FontWeight.Medium, FontStyle.Normal),
	Font(R.font.spacegrotesk_regular, FontWeight.Normal, FontStyle.Normal),
	Font(R.font.spacegrotesk_light, FontWeight.Light, FontStyle.Normal),
)
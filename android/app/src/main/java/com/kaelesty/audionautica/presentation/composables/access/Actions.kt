package com.kaelesty.audionautica.presentation.composables.access

import android.net.Uri


data class SimpleNamedAction(
	val name: String,
	val onAction: () -> Unit
)

data class CollectNamedAction(
	val name: String,
	val onCollect: (Map<String, String>, Map<String, Uri>?) -> Unit
)
package com.kaelesty.audionautica.presentation.composables.access


data class SimpleNamedAction(
	val name: String,
	val onAction: () -> Unit
)

data class CollectNamedAction(
	val name: String,
	val onCollect: (List<String>) -> Unit
)
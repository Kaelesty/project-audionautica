package com.kaelesty.audionautica.domain.entities

sealed class PlayerState

class SubmitPlayset(
	val playset: Playset
)

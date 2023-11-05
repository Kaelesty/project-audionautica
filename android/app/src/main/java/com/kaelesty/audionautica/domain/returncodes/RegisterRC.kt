package com.kaelesty.audionautica.domain.returncodes

enum class RegisterRC {
	OK, // Represents LoginRC.OK
	AUTOLOGIN_FAILED, // Represents LoginRC.UNKNOWN
	BAD_EMAIL,
	UNKNOWN,

	AUTOLOGIN_FATAL_ERROR
	// Represents other LoginRC values.
	// This means that the data that was just successfully registered (received code 200 on Register)
	// is incorrect (codes 400 and 401) for entry. The problem is not on our side.
}
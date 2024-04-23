package com.kaelesty.audionautica.presentation.playlists.upload

sealed class PlaylistUploadState {

	object Uploading: PlaylistUploadState()

	class Error(val error: String): PlaylistUploadState()

	object Initial: PlaylistUploadState()

	object Finished: PlaylistUploadState()
}
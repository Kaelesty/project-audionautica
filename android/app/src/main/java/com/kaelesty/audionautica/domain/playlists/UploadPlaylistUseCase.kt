package com.kaelesty.audionautica.domain.playlists

import android.net.Uri
import javax.inject.Inject

class UploadPlaylistUseCase @Inject constructor(
	private val repo: IPlaylistsRepo
) {

	suspend operator fun invoke(playlist: UserPlaylist.Entire, uri: Uri) = repo.uploadPlaylist(playlist, uri)
}
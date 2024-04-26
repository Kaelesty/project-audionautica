import React, { useState } from "react"
import { useQueue } from "../queue/queue.js"
import PlaylistAddMenu from "./playlistaddmenu.js"
import addTrackToPlaylist from "../../requests/addtracktoplaylist.js"
import getUserPlaylists from "../../requests/getuserplaylists.js"
import deleteTrackFromPlaylist from "../../requests/deltrackfromplaylist.js"

const PlaylistTrack = ({id, title, artist, onPlayClick, playlistId, update}) =>{
  const [isOnTrack, setIsOnTrack] = useState(false)
  const {addTrackToQueue} = useQueue()


  const addToFavButtClick = async (id) => {
    const result = await getUserPlaylists()
    let favId = result.created[0]
    await addTrackToPlaylist(id, favId)
    console.log("track added to favorites.")
    if (playlistId === favId){
        update()
    }
  }

  const addQueueButtClick = (id, title, artist) => {
    addTrackToQueue({id, title, artist}) 
  }

  const deleteTrack = async (id) => {
    await deleteTrackFromPlaylist(id, playlistId)
    update()
  }

  const addToPlaylist = (addPlaylistId) => {
    if (addPlaylistId === playlistId){
      update()
    }
    addTrackToPlaylist(id, addPlaylistId)
  } 

  return (
    <>
      <div
        className="result"
        key={id}
        onMouseLeave={() => setIsOnTrack(false)}
        onMouseEnter={() => setIsOnTrack(true)}
      >
        <img
          className="play-butt"
          onClick={() => onPlayClick(id, title, artist)}
          src={require("../icons/play-button.png")}
          alt="Play"
        />
        
        <div className='track-info'>
          <p>{title}</p>
          <p>{artist}</p>
        </div>
        <PlaylistAddMenu
              onAddToFavorites={() =>addToFavButtClick(id)}
              onAddToQueue={() =>addQueueButtClick(id, title, artist)}
              onDelete={() => deleteTrack(id)}
              onAddToPlaylist={addToPlaylist}
              trackId={id}
              isOnTrack = {isOnTrack}
        />
      </div>
    </>
  );
}

export default PlaylistTrack
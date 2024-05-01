import React, { useState } from "react"
import { useQueue } from "../queue/queue.js"
import AddMenu from "./addmenu.js"
import addTrackToPlaylist from "../../requests/addtracktoplaylist.js"
import getUserPlaylists from "../../requests/getuserplaylists.js"

const Track = ({id, title, artist, onPlayClick}) =>{
  const [isOnTrack, setIsOnTrack] = useState(false)
  const {addTrackToQueue} = useQueue()


  const addToFavButtClick = async (id) => {
    const result = await getUserPlaylists()
    let favId = result.created[0]
    addTrackToPlaylist(id, favId)
    console.log("track added to favorites.")
  }

  const addQueueButtClick = (id, title, artist) => {
    addTrackToQueue({id, title, artist}) 
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
        <AddMenu
          onAddToFavorites={() =>addToFavButtClick(id)}
          onAddToQueue={() =>addQueueButtClick(id, title, artist)}
          playlists={[1,2,3]}
          trackId={id}
          isOnTrack = {isOnTrack}
        />
      </div>
    </>
  );
}

export default Track
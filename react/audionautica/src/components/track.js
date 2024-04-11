import React, { useContext } from "react"
import getTrackById from "../requests/gettrack"
import { Context } from "../index.js"
import queue from "../queue.js"
import AddMenu from "./search/addmenu.js"
import addTrackToPlaylist from "../requests/addtracktoplaylist.js"
import getUserPlaylists from "../requests/getuserplaylists.js"

const Track = ({id, title, artist, abort, setAbort}) =>{
  const {currentTrack, setCurrentTrack} = useContext(Context)
  const {setCurrTrackArtist} = useContext(Context)
  const {setCurrTrackTitle} = useContext(Context)

  const handleTrackClick = async (id, trackTitle, trackArtist) => {
    getTrackById(id, currentTrack, setCurrentTrack, abort, setAbort)
    setCurrTrackArtist(trackArtist)
    setCurrTrackTitle(trackTitle)
    queue.setTrack(id, trackTitle, trackArtist)
  }

  const addToFavButtClick = async (id) => {
    const result = await getUserPlaylists()
    let favId = result.created[0]
    addTrackToPlaylist(id, favId)
    console.log("track added to favorites.")
  }

  const addQueueButtClick = (id, title, artist) => {
    queue.addTrack({id, title, artist})
    console.log(queue.queue)    
  }

  return (
    <>
      <div
        className="result"
        key={id}
      >
        <img
          className="play-butt"
          onClick={() => handleTrackClick(id, title, artist)}
          src={require("./icons/play-button.png")}
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
        />
      </div>
    </>
  );
}

export default Track
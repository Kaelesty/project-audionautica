import React, { useContext } from "react"
import getTrackById from "../requests/gettrack"
import { Context } from "../index.js"
import queue from "../queue.js"
import AddMenu from "./search/addmenu.js"

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

  const addButtonClick = (id, trackTitle, trackArtist) => {
    console.log("track " , trackTitle, " added")
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
          onAddToFavorites={() =>addButtonClick(id, title, artist)}
          onAddToQueue={() =>addQueueButtClick(id, title, artist)}
          playlists={[1,2,3]}
        />
      </div>
    </>
  );
}

export default Track
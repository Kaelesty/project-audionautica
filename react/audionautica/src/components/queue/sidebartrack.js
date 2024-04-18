import React from "react"
import useQueue from "./queue";
import "./sidebartrack.css"

const SidebarTrack = ({id, title, artist, listIndex, handlePlay}) =>{
  const {currentId, removeFromQueue} = useQueue()
  
  return (
    <div
      className="SBtrack"
      key={id}
    >
    {listIndex !== currentId && (
      <img className="play-butt"
        onClick={handlePlay}
        src={require("../icons/play-button.png")}
        alt="Play"
      />)}
    {listIndex === currentId && (
      <div className="track-animation-container">
        <div className="pulse pulse1"></div>
        <div className="pulse pulse2"></div>
        <div className="pulse pulse3"></div>
      </div> )}
      
      <div className='track-info'>
        <p>{title}</p>
        <p>{artist}</p>
      </div>
      <button className="remove-button" onClick={() => removeFromQueue(listIndex)}>&times;</button>
    </div>
  );
}

export default SidebarTrack
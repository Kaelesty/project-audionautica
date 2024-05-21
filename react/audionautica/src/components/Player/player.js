import React, {useState, useEffect} from 'react';
import VolumeSlider from './volumeslider';
import AlbumCoverBig from './albumcoverbig';
import ProgressBar from './progressbar.js';
import AlbumCover from './../icons/nopic.png';
import useQueue from '../queue/queue.js';
import getTrackById from '../../requests/gettrack.js';
import './player.css';


const MusicPlayer = ({isSidebarOpen, setIsSidebarOpen}) => {
  const [isPlaying, setIsPlaying] = useState(false);
  const {repeatMode, setRepeatMode} = useQueue();
  const [volume, setVolume] = useState(0.5);
  const [abort, setAbort] = useState(null);
  const [currentTrack, setCurrentTrack] = useState(null)
  const [currTrackTitle, setCurrTrackTitle] = useState()
  const [currTrackArtist, setCurrTrackArtist] = useState()
  const [currTrackCover, setCurrTrackCover] = useState(AlbumCover)
  const {queue, nextTrack, prevTrack, currentId} = useQueue()

  useEffect(() => {
    if (currentTrack===null){
      return
    }
    currentTrack.addEventListener('ended', handleTrackEnded);
    if (repeatMode === 2){
      currentTrack.loop = true
    }
    setIsPlaying(true)
     // eslint-disable-next-line
  }, [currentTrack]);

  useEffect(() => {
    if (currentTrack===null){return}
    currentTrack.volume = volume
  }, [volume, currentTrack]);

  useEffect(() => {
    const setTrack = async () => {
      const response = await getTrackById(queue[currentId].id, currentTrack, setCurrentTrack, abort, setAbort);
      if (response===404){
        console.log("skip deleted track")
        nextTrack()
      }
    };
    if (queue.length !== 0){
      setTrack()
      setCurrTrackArtist(queue[currentId].artist)
      setCurrTrackTitle(queue[currentId].title)
      if (queue[currentId].cover){
        setCurrTrackCover(queue[currentId].cover)
      }
      else{
        setCurrTrackCover(AlbumCover)
      }
    }
    // eslint-disable-next-line
  }, [queue[currentId]]);

  const togglePlay = () => {
    if (isPlaying){
      fadeOut()
    }
    else{
      fadeIn()
    }
  };

  const fadeIn = () => {
    currentTrack.play()
    setIsPlaying(true);
    const fadeInterval = setInterval(() => {
      if (currentTrack.volume < (0.95 * volume)) {
        currentTrack.volume += 0.01;
      } else {
        currentTrack.volume = volume;
        clearInterval(fadeInterval);
      }
    }, 4);
  };

  const fadeOut = () => {
    setIsPlaying(false);
    const fadeInterval = setInterval(() => {
      if (currentTrack.volume > (0.05)) {
        currentTrack.volume -= 0.01;
      } else {
        currentTrack.volume = 0;
        clearInterval(fadeInterval);
        currentTrack.pause()
      }
    }, 4);
  }; 

  const toggleReplay = () => {
    const newMode = (repeatMode + 1) % 3
    if (newMode === 2) {currentTrack.loop = true}
    else {currentTrack.loop = false} 
    setRepeatMode(newMode)   

  }

  const handleVolumeChange = (newVolume) =>{
    setVolume(newVolume/100)
  }

  const toggleNext = () => {
    nextTrack()
  }
  const togglePrev = () => {
    prevTrack()
  }
  const toggleQueue = () => {
    setIsSidebarOpen(!isSidebarOpen)
  }

  const handleTrackEnded = () => {
    nextTrack()
  }

  return (
    currentTrack !== null && (
    <>
      <div className="music-player">
          <div className="album-cover">
            <img src={currTrackCover} alt="Album Cover"/>
          </div>
        <div className="song-details">
          <h3>{currTrackTitle}</h3>
          <p>{currTrackArtist}</p>
        </div>
       
        <div className="player-controls">
          <img className="control-button img" 
            src={require('./../icons/rewind.png')} 
            alt="Backward"
            onClick={togglePrev}
          />


          <img className="control-button img"
            src={isPlaying ? require('./../icons/pause.png') : require('./../icons/play-button.png')} 
            alt={isPlaying ? 'Pause' : 'Play'} 
            onClick={togglePlay} 
          />

          <img className="control-button img" 
            src={require("./../icons/fast-forward.png")} alt="Forward" 
            onClick={toggleNext}
          />

          <img className="control-button replay-button" 
            src={repeatMode < 2 ? require("./../icons/replay.png") : require("./../icons/replay-one.png")}
            style={repeatMode > 0 ? { backgroundColor: '#bebebe', transform: 'translateY(1px)' }: {}} 
            alt="Replay Mode" 
            onClick={toggleReplay}
          />
          
        </div>

        <div className='rightside-container'>
          <img className="control-button queue-button" 
              src={require("./../icons/list.png")} alt="Open queue" 
              onClick={toggleQueue}
            />

            <VolumeSlider volumeChange={handleVolumeChange} />
        </div>
        <div className='progress-bar-container'>
          <ProgressBar currentTrack={currentTrack}/>
        </div>
      </div>

      <AlbumCoverBig
        coverSrc={currTrackCover}
      />
   </>
    )
  );
};

export default MusicPlayer;
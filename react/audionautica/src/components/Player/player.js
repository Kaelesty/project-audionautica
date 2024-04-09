import React, {useState, useContext, useEffect} from 'react';
import VolumeSlider from './volumeslider';
import AlbumCoverBig from './albumcoverbig';
import ProgressBar from './progressbar.js';
import AlbumCover from './../icons/nopic.png';
import { Context } from '../../index.js';
import './player.css';


const MusicPlayer = ({albumCover}) => {
  const [isPlaying, setIsPlaying] = useState(false);
  const [repeatMode, setRepeatMode] = useState(0);
  const [volume, setVolume] = useState(0.5);
  const {currentTrack} = useContext(Context)
  const {currTrackTitle} = useContext(Context)
  const {currTrackArtist} = useContext(Context)
  

  useEffect(() => {
    if (currentTrack===null){return}
    setIsPlaying(true)
  }, [currentTrack]);

  useEffect(() => {
    if (currentTrack===null){return}
    currentTrack.volume = volume
  }, [volume,currentTrack]);

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
  const toggleTest = () => {
    console.log(currentTrack.duration)
  }

  return (
    currentTrack !== null && (
    <>
      <div className="music-player">
          <div className="album-cover">
            <img src={AlbumCover} alt="Album Cover"/>
          </div>
        <div className="song-details">
          <h3>{currTrackTitle}</h3>
          <p>{currTrackArtist}</p>
        </div>
       
        <div className="player-controls">
          <img className="control-button img" src={require('./../icons/rewind.png')} alt="Backward" />

          <img className="control-button img"
            src={isPlaying ? require('./../icons/pause.png') : require('./../icons/play-button.png')} 
            alt={isPlaying ? 'Pause' : 'Play'} 
            onClick={togglePlay} 
          />

          <img className="control-button img" src={require("./../icons/fast-forward.png")} alt="Forward" 
            onClick={toggleTest}
          />

          <img className="control-button replay-button" 
            src={repeatMode < 2 ? require("./../icons/replay.png") : require("./../icons/replay-one.png")}
            style={repeatMode > 0 ? { backgroundColor: '#bebebe', transform: 'translateY(1px)' }: {}} 
            alt="Replay Mode" onClick={toggleReplay}
          />
        </div>
        
        <div className='volume-controls'>
            <VolumeSlider volumeChange={handleVolumeChange} />
        </div>
        <div className='progress-bar-container'>
          <ProgressBar />
        </div>
      </div>
      <AlbumCoverBig
        coverSrc={AlbumCover}
      />
   </>
    )
  );
};

export default MusicPlayer;
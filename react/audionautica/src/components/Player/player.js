import React, {useState, useContext, useEffect} from 'react';
import VolumeSlider from './volumeslider';
import AlbumCover from './../icons/nopic.png';
import AlbumCoverBig from './albumcoverbig';
import './player.css';
import { Context } from '../../index.js';

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
    currentTrack.volume = volume
  }, [currentTrack, volume]);

  const togglePlay = () => {
    if (isPlaying){
      currentTrack.pause()
    }
    else{
      currentTrack.play()
    }
    setIsPlaying(!isPlaying);
    };

  const toggleReplay = () => {
    if (repeatMode === 1) {currentTrack.loop = true}
    else {currentTrack.loop = false}
    setRepeatMode((repeatMode + 1) % 3)   
  }

  const handleVolumeChange = (newVolume) =>{
    setVolume(newVolume/100)
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

          <img className="control-button img" src={require("./../icons/fast-forward.png")} alt="Forward" />

          <img className="control-button replay-button" 
            src={repeatMode < 2 ? require("./../icons/replay.png") : require("./../icons/replay-one.png")}
            style={repeatMode > 0 ? { backgroundColor: '#bebebe', transform: 'translateY(1px)' }: {}} 
            alt="Replay Mode" onClick={toggleReplay}
          />

        </div>
        <div className='volume-controls'>
            <VolumeSlider volumeChange={handleVolumeChange} />

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
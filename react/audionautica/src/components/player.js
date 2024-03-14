import React, {useState} from 'react';
import VolumeSlider from './volumeslider';
import './player.css';
import AlbumCover from './icons/nopic.png';

const MusicPlayer = ({ songTitle, artistName, albumCover, audio}) => {
  const [isPlaying, setIsPlaying] = useState(false);
  const [repeatMode, setRepeatMode] = useState(0);

  const togglePlay = () => {
    if (isPlaying){
      audio.pause()
      
    }
    else{
      audio.play()
    }
    setIsPlaying(!isPlaying);
    };

  const toggleReplay = () => {
    setRepeatMode((repeatMode+1)%3)
  }

  const handleVolumeChange = (newVolume) =>{
    audio.volume = newVolume/100

  }

  return (
    <div className="music-player">
        <div className="album-cover">
          <img src={AlbumCover} alt="Album Cover"/>
        </div>
      <div className="song-details">
        <h3>{songTitle}</h3>
        <p>{artistName}</p>
      </div>
      <div className="player-controls">
        <img className="control-button img" src={require('./icons/rewind.png')} alt="Backward" />

        <img className="control-button img"
          src={isPlaying ? require('./icons/pause.png') : require('./icons/play-button.png')} 
          alt={isPlaying ? 'Pause' : 'Play'} 
          onClick={togglePlay} 
        />

        <img className="control-button img" src={require("./icons/fast-forward.png")} alt="Forward" />

        <img className="control-button replay-button" 
          src={repeatMode < 2 ? require("./icons/replay.png") : require("./icons/replay-one.png")}
          style={repeatMode > 0 ? { backgroundColor: '#bebebe', transform: 'translateY(1px)' }: {}} 
          alt="Replay Mode" onClick={toggleReplay}
        />
        
      </div>
      <div className='volume-controls'>
          <VolumeSlider volumeChange={handleVolumeChange} />
        
        </div>
    </div>
  );
};

export default MusicPlayer;
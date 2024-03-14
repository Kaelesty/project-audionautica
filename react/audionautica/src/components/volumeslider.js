import React, { useState } from 'react';
import './volumeslider.css'; 

const VolumeSlider = ({ volumeChange }) => {
  const [volume, setVolume] = useState(50);
  const [lastVolume, setLastVolume] = useState(0);


  const handleVolumeChange = (e) => {
    const newVolume = e.target.value;
    setVolume(newVolume);

    volumeChange(volume);
  };

  const VolumeButPressed = () => {
    if (volume!==0){
      setLastVolume(volume)
      setVolume(0)
      volumeChange(0);
    }
    else {
      setVolume(lastVolume)
      volumeChange(lastVolume);
    }

  }

  return (
    <div className="volume-slider">
      <img className='volume-indicator'
          src={volume > 80 ? require("./icons/volume3.png") : volume > 45 ? require("./icons/volume2.png") : volume > 0 ? require("./icons/volume1.png"): require("./icons/volume0.png")}
          alt="Volume Ind"
          onClick={VolumeButPressed}
        />

      <input
        type="range"
        min="0"
        max="100"
        value={volume}
        onChange={handleVolumeChange}
        className="slider"
      />
    </div>
  );
};

export default VolumeSlider;
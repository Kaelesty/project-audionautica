import React, { useState, useEffect, useContext } from 'react';
import { Context } from '../../index.js';
import './progressbar.css'

const ProgressBar = () => {
  const { currentTrack } = useContext(Context);
  const [currentTime, setCurrentTime] = useState(0);
  const [duration, setDuration] = useState(0);

  useEffect(() => {
    if (currentTrack) {
        const handleTimeUpdate = () => {
            setCurrentTime(currentTrack.currentTime);
            
            if (duration!==currentTrack.duration){
                setDuration(currentTrack.duration);
            }  
        };

        
        currentTrack.addEventListener('timeupdate', handleTimeUpdate);
  
        return () => {
            currentTrack.removeEventListener('timeupdate', handleTimeUpdate);
        };
    }
  }, [currentTrack, duration]);

  const handleSeek = (e) => {
    const seekTime = e.target.value;
    setCurrentTime(seekTime);
    if (currentTrack) {
      currentTrack.currentTime = seekTime;
    }
  };

  const formatTime = (time) => {
    const minutes = Math.floor(time / 60);
    const seconds = Math.floor(time % 60);
    return `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
  };

  return (
    <div className="progressBarContainer">
      <input
        type="range"
        value={currentTime}
        max={duration}
        onChange={handleSeek}
        className="progressBar"
      />
      <div className="timeLabels">
        <span>{formatTime(currentTime)}</span>
        <span>{formatTime(duration)}</span>
      </div>
    </div>
  );
};

export default ProgressBar;
import React, { useState } from 'react';
import nopic from './icons/nopic.png'; 
import { useNavigate } from 'react-router-dom';
import useQueue from './queue/queue';
import getPlaylistById from '../requests/getplaylistbyid';
import getTrackInfoById from '../requests/gettrackinfobyid';
import './playlist.css';

const Playlist = ({ cover, title, creator, id }) => {
  const [isHovered, setIsHovered] = useState(false);
  const navigate = useNavigate();
  const {setQueuePlaylist} = useQueue();

  const playlistClick = () => {
    navigate(`/playlist/${id}`);
  }
  
  const playClick = async () => {
    try {
      const response = await getPlaylistById(id);
      if (response.tracksid === ""){
        return
      }
      else {
        const tracksid = response.tracksid.split("/").slice(0,-1)
        const tracksData = await Promise.all(tracksid.map(async (id) => {
          const track = await getTrackInfoById(id);
          return {
            id: id,
            title: track.title,
            artist: track.artist
          };
        }));
        setQueuePlaylist(tracksData)
      }
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  }

  if (cover === null){  
    cover = nopic
  }

  return (
    <div
      className={`playlist-container ${isHovered ? 'hovered' : ''}`}
      onMouseEnter={() => setIsHovered(true)}
      onMouseLeave={() => setIsHovered(false)}
      onClick={playlistClick}
    >
      <img className="playlist-cover" src={cover} alt="Playlist Cover" />
      
      <div className="playlist-info">
        <h3 className="playlist-title">{title}</h3>
        <p className="playlist-creator">{creator}</p>
      </div>

      {isHovered && (
        <button className="play-button" onClick={(event) => {
          event.stopPropagation(); 
          playClick();
        }}>
          <img className='playpic' alt=''
            src={require('./icons/play-button.png')} 
            />
        </button>
      )}
    </div>
  );
};

export default Playlist;
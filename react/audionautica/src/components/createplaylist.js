import React, {useState} from "react";
import { useNavigate } from 'react-router-dom';

const CreatePlaylist = () => {
    const [isHovered, setIsHovered] = useState(false);
    const navigate = useNavigate();
  
    const playlistClick = () => {
      navigate(`/createplaylist`);
    }
  
    return (
      <div
        className={`playlist-container ${isHovered ? 'hovered' : ''}`}
        onMouseEnter={() => setIsHovered(true)}
        onMouseLeave={() => setIsHovered(false)}
        onClick={playlistClick}
      >
        <img className="playlist-pluscover" src={require("./icons/plus.png")} alt="Playlist Cover" />
        
        <div className="playlist-info">
          <h3 className="playlist-title">Создать плейлист</h3>
        </div>
      </div>
    );
  };  

export default CreatePlaylist
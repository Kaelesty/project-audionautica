import React, { useState, useEffect, useContext } from 'react';
import { Context } from '../../index';
import { useNavigate } from 'react-router-dom';
import "./addmenu.css"

const PlaylistAddMenu = ({ onAddToQueue, onAddToFavorites, onDelete, onAddToPlaylist, isOnTrack}) => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [isPlaylistMenuOpen, setIsPlaylistMenuOpen] = useState(false);
  const {createdPlaylists} = useContext(Context)
  const navigate = useNavigate()

  useEffect(() => {
    if (isOnTrack){return}
    setIsMenuOpen(isOnTrack)
  }, [isOnTrack]);

  const handleToggleMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };

  const handleQueueAddClick = () =>{
    setIsMenuOpen(false)
    onAddToQueue()
  }

  const handleFavClick = () =>{
    setIsMenuOpen(false)
    onAddToFavorites()
  }

  const handleDelClick = () => {    
    setIsMenuOpen(false)
    onDelete()
  }

  const handleAddToPlaylist = (playlistId) =>{
    onAddToPlaylist(playlistId)
    setIsMenuOpen(false)
  }
  
  const createPlaylist = () =>{
    navigate("/createplaylist")
  }
  
  return (
    <>
      <img className='add-button'
        src={require("./../icons/plus.png")}
        alt="Добавить в ..."
        title="Добавить в ..."
        onClick={handleToggleMenu}>
      </img>
      {isMenuOpen && (
        <div className="add-menu2"
          onMouseLeave={() => setIsMenuOpen(false)}
        >
          <button onClick={handleQueueAddClick}>Добавить в очередь</button>
          <button onClick={handleFavClick}>Добавить в избранное</button>
          <button onClick={handleDelClick}>Удалить из плейлиста</button>
          <button
            onMouseEnter={() => setIsPlaylistMenuOpen(true)}
            onMouseLeave={() => setIsPlaylistMenuOpen(false)}
          >
            Добавить в плейлист &#10148;
          </button>
          {isPlaylistMenuOpen && (
            <div className="playlist-menu"
              onMouseLeave={() => setIsPlaylistMenuOpen(false)}
              onMouseEnter={() => setIsPlaylistMenuOpen(true)}
            >
              {createdPlaylists.map((playlist, index) => (
                <button key={index} onClick={() => handleAddToPlaylist(playlist.id)}>
                  {playlist.title}
                </button>
              ))}
              <button onClick={createPlaylist} style={{ textAlign: "center" }}>+</button>
            </div>
          )}
        </div>
      )}
    </>
  );
};

export default PlaylistAddMenu;
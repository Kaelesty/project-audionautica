import React, { useState, useEffect } from 'react';
import addTrackToPlaylist from '../../requests/addtracktoplaylist';
import "./addmenu.css"

const AddMenu = ({ onAddToQueue, onAddToFavorites, playlists, trackId, isOnTrack}) => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [isPlaylistMenuOpen, setIsPlaylistMenuOpen] = useState(false);

  useEffect(() => {
    if (isOnTrack){return}
    setIsMenuOpen(isOnTrack)
  }, [isOnTrack]);

  const handleToggleMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };

  const addToPlaylist = async (playlistId) => {
    await addTrackToPlaylist(trackId, playlistId)
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
        <div className="add-menu"
          onMouseLeave={() => setIsMenuOpen(false)}
        >
          <button onClick={onAddToQueue}>Добавить в очередь</button>
          <button onClick={onAddToFavorites}>Добавить в избранное</button>
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
              {playlists.map((playlist, index) => (
                <button key={index} onClick={() => addToPlaylist(playlist)}>
                  {playlist}
                </button>
              ))}
            </div>
          )}
        </div>
      )}
    </>
  );
};

export default AddMenu;
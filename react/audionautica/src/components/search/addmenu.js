import React, { useState } from 'react';

import "./addmenu.css"

const AddMenu = ({ onAddToQueue, onAddToFavorites, playlists}) => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [isPlaylistMenuOpen, setIsPlaylistMenuOpen] = useState(false);

  const handleToggleMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };

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
                <button key={index} onClick={() => console.log(`Добавить в плейлист ${playlist}`)}>
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
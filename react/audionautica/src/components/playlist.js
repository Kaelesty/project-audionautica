import React, { useState } from 'react';
import './playlist.css'; // Подключаем файл стилей

const Playlist = ({ cover, title, creator }) => {
  const [isHovered, setIsHovered] = useState(false);

  return (
    <div
      className={`playlist-container ${isHovered ? 'hovered' : ''}`}
      onMouseEnter={() => setIsHovered(true)}
      onMouseLeave={() => setIsHovered(false)}
    >
      {/* Обложка плейлиста */}
      <img className="playlist-cover" src={cover} alt="Playlist Cover" />

      {/* Название и создатель */}
      <div className="playlist-info">
        <h3 className="playlist-title">{title}</h3>
        <p className="playlist-creator">{creator}</p>
      </div>

      {/* Кнопка воспроизведения */}
      {isHovered && (
        <button className="play-button">
           <img className='playpic'
            src={require('./icons/play-button.png')} 
            />
        </button>
      )}
    </div>
  );
};

export default Playlist;
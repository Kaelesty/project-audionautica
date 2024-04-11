import React from 'react';
import Playlist from '../components/playlist';

import "./home.css"

const HomePage = () => {
  const createdPlaylists = [
    {
      cover: null,
      title: "Название плейлиста 1",
      creator: "Автор плейлиста 1"
    },
    {
      cover: null,
      title: "Название плейлиста 2",
      creator: "Автор плейлиста 2"
    },
  ];

  const savedPlaylists = [
    {
      cover: null,
      title: "Название плейлиста 1",
      creator: "Автор плейлиста 1"
    },
    {
      cover: null,
      title: "Название плейлиста 2",
      creator: "Автор плейлиста 2"
    },
    {
      cover: null,
      title: "Название плейлиста 3",
      creator: "Автор плейлиста 3"
    },
  ];

  return (
    <div>
      <h1>Добро пожаловать на главную страницу</h1>
      
      <h2>Созданные плейлисты</h2>
      <div className="playlists-container">
        {createdPlaylists.map((playlist, index) => (
          <Playlist key={index} 
            cover = {playlist.cover}
            title = {playlist.title}
            creator = {playlist.creator}
          />
        ))}
      </div>
      
      <h2>Сохраненные плейлисты</h2>
      <div className="playlists-container">
        {savedPlaylists.map((playlist, index) => (
          <Playlist key={index}  
            cover = {playlist.cover}
            title = {playlist.title}
            creator = {playlist.creator}
          />
        ))}
      </div>
    </div>
  );
}

export default HomePage;
import React from 'react';
import MusicPlayer from '../components/player'; 

const HomePage = () => {
  // Здесь могут быть данные о песне, исполнителе и обложке альбома
  const songTitle = "Song Title";
  const artistName = "Artist Name";
  const albumCover = "path/to/album/cover.jpg";

  return (
    <div className="home-page">
      <MusicPlayer
        songTitle={songTitle}
        artistName={artistName}
        albumCover={albumCover}
      />
    </div>
  );
};

export default HomePage;
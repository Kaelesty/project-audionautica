import React from 'react';
import MusicPlayer from '../components/player'; 
import AlbumCoverBig from '../components/albumcoverbig';
import CoverImg from '../components/icons/nopic.png'
import "./home.css"


const HomePage = () => {
  // Здесь могут быть данные о песне, исполнителе и обложке альбома
  const songTitle = "Song Title";
  const artistName = "Artist Name";
  const albumCover = CoverImg;

  return (
    <div className="home-page">
      <div className="Player">
        <MusicPlayer
          songTitle={songTitle}
          artistName={artistName}
          albumCover={albumCover}
        />
        <AlbumCoverBig
          coverSrc={CoverImg}
        />
      </div>
    </div>
  );
};

export default HomePage;
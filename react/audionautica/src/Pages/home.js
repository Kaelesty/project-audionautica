import React, { useContext} from 'react';
import {Navigate, Outlet} from 'react-router-dom';
import MusicPlayer from '../components/player/player.js';
import CoverImg from '../components/icons/nopic.png'
import {Context} from "../index.js"
import getTrackById from '../gettrack.js';
import "./home.css"



const HomePage = () => {
  // Здесь могут быть данные о песне, исполнителе и обложке альбома
  const songTitle = "Song Title";
  const artistName = "Artist Name";
  const albumCover = CoverImg;
  const [isLoggedIn] = useContext(Context);
  const [currentTrack, setCurrentTrack] = useContext(Context)

  if (!isLoggedIn){
    return <Navigate to="/login" />
  }

  const getTrack = async () => {
    try{
      const trackBlob = await getTrackById(4)
      const trackUrl = URL.createObjectURL(trackBlob);
      setCurrentTrack(new Audio(trackUrl)); 
    }
    catch(error){
      console.error("Ошибка при получении трека",error)
    }
    finally {console.log(currentTrack)}
  };

  return (
      <div className="home-page">
        <Outlet/>
        <div className="Player">
          <MusicPlayer
            songTitle={songTitle}
            artistName={artistName}
            albumCover={albumCover}
          />
        </div>

        
      <button onClick={getTrack}> Загрузить аудио</button>
      </div>
  );
};

export default HomePage;
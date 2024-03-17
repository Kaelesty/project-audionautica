import React, {useState, useContext} from 'react';
import {Navigate, Outlet} from 'react-router-dom';
import MusicPlayer from '../components/player'; 
import CoverImg from '../components/icons/nopic.png'
import BackURL from '../urls';
import {Context} from "../index.js"
import "./home.css"


const HomePage = () => {
  // Здесь могут быть данные о песне, исполнителе и обложке альбома
  const songTitle = "Song Title";
  const artistName = "Artist Name";
  const albumCover = CoverImg;
  const [audio, setAudio] = useState(null);
  const [isLoggedIn] = useContext(Context);

  if (!isLoggedIn){
    return <Navigate to="/login" />
  }

  const getTrack = async () => {
    setAudio(1)//отображение плеера без музsыки
    return
    try{
      const trackBlob = await postGetTrackById(8);
      const trackUrl = URL.createObjectURL(trackBlob);
      setAudio(new Audio(trackUrl)); 
    }
    catch(error){
      console.error("Ошибка при получении трека",error)
    }
  };

  return (
      <div className="home-page">
        <Outlet/>
        <div className="Player">
          <MusicPlayer
            songTitle={songTitle}
            artistName={artistName}
            albumCover={albumCover}
            audio={audio}
          />
        </div>

        
      <button onClick={getTrack}> Загрузить аудио</button>
      </div>
  );
};

async function postGetTrackById(id) {
  try {
    const response = await fetch(BackURL + "/Music/GetTrack/", {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({id}),
      responseType: 'blob'
    });

    if (response.status === 404) {
      console.error('Трек не найден');
      return null;
    }
    
    console.log("Отправлен запрос на получение трека")
    const trackBlob = await response.blob(); // Получаем бинарный трек
    return trackBlob;
  } catch (error) {
      console.error('Ошибка:', error);
      return null;
  }
};

export default HomePage;
import React, {useState} from 'react';
import MusicPlayer from '../components/player'; 
import AlbumCoverBig from '../components/albumcoverbig';
import CoverImg from '../components/icons/nopic.png'
import BackURL from '../urls';
import "./home.css"


const HomePage = () => {
  // Здесь могут быть данные о песне, исполнителе и обложке альбома
  const songTitle = "Song Title";
  const artistName = "Artist Name";
  const albumCover = CoverImg;
  const [audio, setAudio] = useState(null);

  const getTrack = async () => {
    const trackBlob = await postGetTrackById(8);
    const trackUrl = URL.createObjectURL(trackBlob);
    setAudio(new Audio(trackUrl)); 
  };

  return (
    <div className="home-page">
      <div className="Player">
        <MusicPlayer
          songTitle={songTitle}
          artistName={artistName}
          albumCover={albumCover}
          audio={audio}
        />
        <AlbumCoverBig
          coverSrc={CoverImg}
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
    console.log("Отправлен запрос на получение трека")
    const trackBlob = await response.blob(); // Получаем бинарный трек
    return trackBlob;

  } catch (error) {
      console.error('Ошибка:', error);
      return null;
  }
};

export default HomePage;
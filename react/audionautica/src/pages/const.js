import React, { useContext} from 'react';
import {Navigate, Outlet} from 'react-router-dom';
import MusicPlayer from '../components/player/player.js';
import CoverImg from '../components/icons/nopic.png'
import {Context} from "../index.js"
import "./const.css"
import getPlaylistById from '../requests/getplaylistbyid.js';

const ConstPage = () => {
  const albumCover = CoverImg;
  const {isLoggedIn} = useContext(Context);

  if (!isLoggedIn){
    return <Navigate to="/login" />
  }

  const logPlaylists = async () => {
    const result = await getPlaylistById(18)
    console.log(result.tracksid.split("/").slice(0,-1))//скорее всего ошибка при пустом плейлисте
  }

  return (
      <div className="const-page">
        <Outlet/>
        <button onClick={logPlaylists}>Просто кнопка</button>
        <div className="Player">
          <MusicPlayer
            albumCover={albumCover}
          />
        </div>
      </div>
  );
};

export default ConstPage;
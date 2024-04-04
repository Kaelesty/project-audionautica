import React, { useContext} from 'react';
import {Navigate, Outlet} from 'react-router-dom';
import MusicPlayer from '../components/player/player.js';
import CoverImg from '../components/icons/nopic.png'
import {Context} from "../index.js"
import "./home.css"



const HomePage = () => {
  const albumCover = CoverImg;
  const {isLoggedIn} = useContext(Context);

  if (!isLoggedIn){
    return <Navigate to="/login" />
  }


  return (
      <div className="home-page">
        <Outlet/>
        <div className="Player">
          <MusicPlayer
            albumCover={albumCover}
          />
        </div>
      </div>
  );
};

export default HomePage;
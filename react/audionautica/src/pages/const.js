import React, { useContext, useState} from 'react';
import {Navigate, Outlet} from 'react-router-dom';
import MusicPlayer from '../components/player/player.js';
import CoverImg from '../components/icons/nopic.png'
import {Context} from "../index.js"
import "./const.css"
import Sidebar from '../components/queue/sidebar.js';
import { QueueProvider } from '../components/queue/queue.js';
const ConstPage = () => {
  const albumCover = CoverImg;
  const {isLoggedIn} = useContext(Context);
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);

  if (!isLoggedIn){
    console.log(isLoggedIn)
    return <Navigate to="/login" />
  }

  const toggleSidebar = () => {
    setIsSidebarOpen(!isSidebarOpen);
  }

  return (
      <div className="const-page">
        <QueueProvider>
          <Outlet/>
          <Sidebar isOpen={isSidebarOpen} onClose={toggleSidebar} />
          <div className="Player">
            <MusicPlayer
              albumCover={albumCover}
              isSidebarOpen={isSidebarOpen}
              setIsSidebarOpen={setIsSidebarOpen}
            />
          </div>
        </QueueProvider>
      </div>
  );
};

export default ConstPage;
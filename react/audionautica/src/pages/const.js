import React, { useContext, useState} from 'react';
import {Navigate, Outlet} from 'react-router-dom';
import MusicPlayer from '../components/player/player.js';
import CoverImg from '../components/icons/nopic.png'
import {Context} from "../index.js"
import "./const.css"
import getPlaylistById from '../requests/getplaylistbyid.js';
import Sidebar from '../components/queue/sidebar.js';
import { QueueProvider } from '../components/queue/queue.js';
const ConstPage = () => {
  const albumCover = CoverImg;
  const {isLoggedIn} = useContext(Context);
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);

  if (!isLoggedIn){
    return <Navigate to="/login" />
  }

  const logPlaylists = async () => {
    const result = await getPlaylistById(18)
    console.log(result.tracksid.split("/").slice(0,-1))//скорее всего ошибка при пустом плейлисте
  }
  
  const toggleSidebar = () => {
    setIsSidebarOpen(!isSidebarOpen);
  }

  return (
      <div className="const-page">
        <QueueProvider>
          <Outlet/>
          <button onClick={logPlaylists}>Просто кнопка</button>
          <button onClick={toggleSidebar}>Toggle Sidebar</button>
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
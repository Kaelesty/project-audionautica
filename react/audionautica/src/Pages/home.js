import React, {useState, useEffect, useContext} from 'react';
import Playlist from '../components/playlist';
import getUserPlaylists from '../requests/getuserplaylists';
import LoaderIcon from '../components/search/download';
import getPlaylistById from '../requests/getplaylistbyid';
import getUsernameById from '../requests/getusernamebyid';
import { Context } from "../index.js"
import BackURL from '../urls.js';
import token from '../token.js';
import CreatePlaylist from '../components/createplaylist.js';
import "./home.css"

const HomePage = () => {
  const [isLoading, setIsLoading] = useState(true)
  const {createdPlaylists, setCreatedPlaylists} = useContext(Context) 
  const {savedPlaylists, setSavedPlaylists} = useContext(Context) 

  useEffect(() => {
    const fetchUserPlaylists = async () => {
      try {
        const response = await getUserPlaylists();

        const createdPlaylistsData = await Promise.all(response.created.map(async (id) => {
          const playlist = await getPlaylistById(id);
          const creator = await getUsernameById(playlist.creator_id)
          return {
            cover: null,
            title: playlist.title,
            creator: creator,
            id: id
          };
        }));
        const savedPlaylistsData = await Promise.all(response.liked.map(async (id) => {
          const playlist = await getPlaylistById(id);
          const creator = await getUsernameById(playlist.creator_id)
          return {
            cover: null,
            title: playlist.title,
            creator: creator,
            id: id
          };
        }));

        setCreatedPlaylists(createdPlaylistsData);      
        setSavedPlaylists(savedPlaylistsData);
        
      } catch (error) {
        console.error('Error fetching data:', error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchUserPlaylists();
    // eslint-disable-next-line
  }, []);

  return (
    <div className='home-page'>
      {isLoading ? (
        <LoaderIcon />
      ) : ( 
      <div>
        <h2>Созданные плейлисты</h2>
          <div className="playlists-container">
          {createdPlaylists.map((playlist, index) => (
            <Playlist key={index} 
              cover = {BackURL + "/PlayList/GetPoster/?id=" + playlist.id + "&token=" + token.getToken()}
              title = {playlist.title}
              creator = {playlist.creator}
              id = {playlist.id}
            />
          ))}
          <CreatePlaylist/>
        </div>
        
        <h2>Сохраненные плейлисты</h2>
        {savedPlaylists && savedPlaylists.length > 0  ? (
        <div className="playlists-container">
          {savedPlaylists.map((playlist, index) => (
            <Playlist key={index}  
              cover = {BackURL + "/PlayList/GetPoster/?id=" + playlist.id + "&token=" + token.getToken()}
              title = {playlist.title}
              creator = {playlist.creator}
              id = {playlist.id}
            />
          ))}
        </div>) : (<p>Пока что тут пусто</p>)}
      </div>)}
    </div>
  );
}

export default HomePage;
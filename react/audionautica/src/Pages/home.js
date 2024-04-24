import React, {useState, useEffect} from 'react';
import Playlist from '../components/playlist';
import getUserPlaylists from '../requests/getuserplaylists';
import LoaderIcon from '../components/search/download';
import getPlaylistById from '../requests/getplaylistbyid';
import getUsernameById from '../requests/getusernamebyid';
import "./home.css"

const HomePage = () => {
  const [isLoading, setIsLoading] = useState(true)
  const [createdPlaylists, setCreatedPlaylists] = useState([]) 
  const [savedPlaylists, setSavedPlaylists] = useState([]) 

  useEffect(() => {
    console.log("created: ",createdPlaylists)
  },[createdPlaylists])

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
  }, []);

  return (
    <div className='home-page'>
      <h1>Добро пожаловать на главную страницу</h1>

      {isLoading ? (
        <LoaderIcon />
      ) : ( 
      <div>
        <h2>Созданные плейлисты</h2>
        {createdPlaylists && createdPlaylists.length > 0 ? (
          <div className="playlists-container">
          {createdPlaylists.map((playlist, index) => (
            <Playlist key={index} 
              cover = {playlist.cover}
              title = {playlist.title}
              creator = {playlist.creator}
              id = {playlist.id}
            />
          ))}
        </div>) : (<p>Пока что здесь пусто</p>)}
        
        <h2>Сохраненные плейлисты</h2>
        {savedPlaylists && savedPlaylists.length > 0  ? (
        <div className="playlists-container">
          {savedPlaylists.map((playlist, index) => (
            <Playlist key={index}  
              cover = {playlist.cover}
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
import React, { useState, useEffect, useContext } from 'react';
import { useParams } from 'react-router-dom';
import PlaylistTrack from '../components/track/playlisttrack';
import useQueue from '../components/queue/queue';
import getPlaylistById from '../requests/getplaylistbyid';
import LoaderIcon from '../components/search/download';
import getUsernameById from '../requests/getusernamebyid';
import getPlaylistTracksById from '../requests/getplaylisttracksbyid';
import likePlaylist from '../requests/likeplaylist';
import { Context } from '../index';
import dislikePlaylist from '../requests/dislikeplaylist';
import BackURL from '../urls';
import token from '../token';
import "./playlistpage.css"


const PlaylistPage = () => {
  const { id } = useParams();
  const {gotoTrack, setQueuePlaylist} = useQueue()
  const [isLoading, setIsLoading] = useState(true)
  const [playlistTitle, setPlaylistTitle] = useState("")
  const [playlistCreator, setPlaylistCreator] = useState("")
  const [playlistDiscription, setPlaylistDiscription] = useState("")
  const [tracks, setTracks] = useState([])
  const [update, setUpdate] = useState(false)
  const {savedPlaylists} = useContext(Context)
  const [liked, setLiked] = useState(savedPlaylists.some(playlist => playlist.id === parseInt(id)))

  useEffect(()=>{
    const fetchPlaylist= async () => {
      try {
        const response = await getPlaylistById(id);
        const creator = await getUsernameById(response.creator_id)
        setPlaylistTitle(response.title);      
        setPlaylistCreator(creator);
        setPlaylistDiscription(response.description)
      
      } catch (error) {
        console.error('Error fetching data:', error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchPlaylist();
    // eslint-disable-next-line
  },[])

  useEffect(() => {
    const fetchPlaylist = async () => {
      try {
        const fullTracksData = await getPlaylistTracksById(id)
        const tracksData = fullTracksData.map(track =>({
          id: track.id,
          title: track.title,
          artist: track.artist,
          cover: BackURL + "/PlayList/GetPoster/?id=" + id + "&token=" + token.getToken()
      }));
        setTracks(tracksData)
      } catch (error) {
        console.error('Error fetching data:', error);
      } 
    };

    fetchPlaylist();
    // eslint-disable-next-line
  }, [update]);

  const playlistPlayClick = () =>{
    setQueuePlaylist(tracks)
  }

  const trackPlayClick = async (index) =>{
    setQueuePlaylist(tracks)
    gotoTrack(index)
  }

  const likePlaylistClick = async () => {
    if (liked){
      await dislikePlaylist(id)
    }
    else {
      await likePlaylist(id)
    }
    setLiked(!liked)
  }

  const onUpdate = () => {
    setUpdate(!update)
  }

  return (
    isLoading ? (
      <LoaderIcon />
    ) : ( 
      <div className='playlistpage'>
        <div className="playlistpage-container">
          <div className="playlistpage-header">
            <img className="playlistpage-cover" src={BackURL + "/PlayList/GetPoster/?id=" + id + "&token=" + token.getToken()} alt="Обложка плейлиста"/>
            <div className="playlistpage-info">
              <h1 className="playlistpage-title">{playlistTitle}</h1>
              <p className="playlistpage-creator">Создатель: {playlistCreator}</p>
            </div>
            <div className='pprightside-container'>
            <button className={liked ? "like-button liked" : "like-button"} onClick={likePlaylistClick}>&#10084;</button>
              <button className="playlist-play-button" onClick={playlistPlayClick}>
                <img className='playpic' alt='' src={require('../components/icons/play-button.png')} />
              </button>

            </div>
          </div>
          <p>{playlistDiscription}</p>
          {tracks && tracks.length > 0 ? (
          <ul className="tracklist"> 
          {tracks.map((track, index) => (
              <PlaylistTrack
                key={index}
                id = {track.id}
                title = {track.title}
                artist = {track.artist}
                onPlayClick={() => trackPlayClick(index)}
                update = {()=> onUpdate()}
                playlistId = {parseInt(id)}
              />
            ))}
          </ul>) : (<p>Здесь пусто, добавьте что-нибудь!</p>)}
        </div>
      </div>
    ));
};

export default PlaylistPage;
import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import Track from '../components/track/track';
import useQueue from '../components/queue/queue';
import getPlaylistById from '../requests/getplaylistbyid';
import LoaderIcon from '../components/search/download';
import getUsernameById from '../requests/getusernamebyid';
import getTrackInfoById from '../requests/gettrackinfobyid';
import "./playlistpage.css"


const PlaylistPage = () => {
  const { id } = useParams();
  const {gotoTrack, setQueuePlaylist} = useQueue()
  const [isLoading, setIsLoading] = useState(true)
  const [playlistTitle, setPlaylistTitle] = useState("")
  const [playlistCreator, setPlaylistCreator] = useState("")
  const [tracks, setTracks] = useState([])

  useEffect(() => {
    const fetchPlaylist= async () => {
      try {
        const response = await getPlaylistById(id);
        const creator = await getUsernameById(response.creator_id)
        setPlaylistTitle(response.title);      
        setPlaylistCreator(creator);

        if (response.tracksid === ""){
          setTracks([])
          return
        }
        else {
        const tracksid = response.tracksid.split("/").slice(0,-1)
        const tracksData = await Promise.all(tracksid.map(async (id) => {
          const track = await getTrackInfoById(id);
          return {
            id: id,
            title: track.title,
            artist: track.artist
          };
        }));
        setTracks(tracksData)
      }

      } catch (error) {
        console.error('Error fetching data:', error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchPlaylist();
    // eslint-disable-next-line
  }, []);

  const playlistPlayClick = () =>{
    setQueuePlaylist(tracks)
  }

  const trackPlayClick = async (index) =>{
    setQueuePlaylist(tracks)
    gotoTrack(index)
  }

  return (
    isLoading ? (
      <LoaderIcon />
    ) : ( 
      <div className="playlistpage-container">
        <div className="playlistpage-header">
          <img src={require("../components/icons/nopic.png")} alt="Обложка плейлиста" className="playlistpage-cover" />
          <div className="playlistpage-info">
            <h1 className="playlistpage-title">{playlistTitle}</h1>
            <p className="playlistpage-creator">Создатель: {playlistCreator}</p>
          </div>
          <div className='pprightside-container'>
          <button className="like-button">&#10084;</button>
            <button className="playlist-play-button" onClick={playlistPlayClick}>
              <img className='playpic' alt='' src={require('../components/icons/play-button.png')} />
            </button>

          </div>
        </div>
        {tracks && tracks.length > 0 ? (
        <ul className="tracklist"> 
        {tracks.map((track, index) => (
            <Track
              key={index}
              id = {track.id}
              title = {track.title}
              artist = {track.artist}
              onPlayClick={() => trackPlayClick(index)}
            />
          ))}
        </ul>) : (<p>Здесь пусто, добавьте что-нибудь!</p>)}

      </div>
    ));
};

export default PlaylistPage;
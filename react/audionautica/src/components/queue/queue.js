import React, { createContext, useContext, useState } from 'react';

const QueueContext = createContext();

export function useQueue() {
  return useContext(QueueContext);
}

export function QueueProvider({ children }) {
  const [queue, setQueue] = useState([]);
  const [currentId, setCurrentId] = useState(0)
  const [repeatMode, setRepeatMode] = useState(0)
  
  const addTrackToQueue = (track) => {
    setQueue(prevQueue => [...prevQueue, track]);
  };

  const setQueueTrack = (track) =>{
    console.log([track])
    setQueue([track]);
    setCurrentId(0)
  }

  const removeFromQueue = (index) => {
    setQueue(prevQueue => {
      const newQueue = prevQueue.slice(0, index).concat(prevQueue.slice(index + 1));
      return newQueue;
    });
  };

  const nextTrack = () => {
    let newId = currentId
    if (repeatMode === 0){
      if (currentId === queue.length - 1){
        return
      }
      newId = (currentId + 1) % queue.length
    }
    else {
      newId = (currentId + 1) % queue.length
    }
    setCurrentId(newId)
  }

  const prevTrack = () => {
    if (currentId !==0 ){
      const newId = (currentId - 1)
      setCurrentId(newId)
    }
  }

  const clearQueue = () => {
    setQueue([]);
  }; 
  const contextValue = { queue, currentId, addTrackToQueue, 
                         setQueueTrack, removeFromQueue, nextTrack, 
                         prevTrack, clearQueue, repeatMode, setRepeatMode }
  return (
    <QueueContext.Provider value={contextValue}>
      {children}
    </QueueContext.Provider>
  );
}

export default useQueue
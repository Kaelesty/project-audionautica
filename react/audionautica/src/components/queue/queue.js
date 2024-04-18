import React, { createContext, useContext, useState, useRef, useEffect } from 'react';

const QueueContext = createContext();

export function useQueue() {
  return useContext(QueueContext);
}

export function QueueProvider({ children }) {
  const [queue, setQueue] = useState([]);
  const [currentId, setCurrentId] = useState(0)
  const [repeatMode, setRepeatMode] = useState(0)
  const queueRef = useRef(queue)
  
  useEffect(() => {
    queueRef.current = queue;
  }, [queue]);

  const addTrackToQueue = (track) => {
    setQueue(prevQueue => [...prevQueue, track]);
  };

  const setQueueTrack = (track) =>{
    console.log([track])
    setQueue([track]);
    setCurrentId(0)
  }

  const removeFromQueue = (index) => {
    if (index < currentId){//коректное удаление треков до текущего
      setCurrentId(currentId - 1);
    }
    else if (index === queue.length - 1 && index === currentId){//запрет на удаление последнего трека в очереди, если он текущий
      return
    }
    console.log(queue)
    setQueue(prevQueue => {
      const newQueue = prevQueue.slice(0, index).concat(prevQueue.slice(index + 1));
      return newQueue;
    });
    
  };

  const nextTrack = () => {
    let newId = currentId;
    console.log(queueRef.current);
    if (repeatMode === 0){
      if (currentId === queueRef.current.length - 1){
        return;
      }
      newId = (currentId + 1) % queueRef.current.length;
    }
    else {
      newId = (currentId + 1) % queueRef.current.length;
    }
    setCurrentId(newId);
  };

  const prevTrack = () => {
    if (currentId !==0 ){
      const newId = (currentId - 1)
      setCurrentId(newId)
    }
  }

  const gotoTrack = (newid) => {
    setCurrentId(newid)
  }

  const clearQueue = () => {
    setQueue([]);
  }; 

  const contextValue = { queue, currentId, addTrackToQueue, 
                         setQueueTrack, removeFromQueue, nextTrack, 
                         prevTrack, repeatMode, setRepeatMode, 
                         gotoTrack, clearQueue}
  return (
    <QueueContext.Provider value={contextValue}>
      {children}
    </QueueContext.Provider>
  );
}

export default useQueue
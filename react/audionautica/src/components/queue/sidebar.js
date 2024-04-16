import React from 'react';
import './sidebar.css';
import useQueue from './queue';
import SidebarTrack from './sidebartrack';

const Sidebar = ({ isOpen}) => {
  const {queue, gotoTrack} = useQueue()

  const handlePlay = (trackId) => {
    gotoTrack(trackId)
  }

  return (
    <div className={`sidebar ${isOpen ? 'open' : ''}`}>
      <div className="queue-container">
      {queue.map((result, index) => (
        <SidebarTrack key={index}
          id={result.id}
          title={result.title}
          artist={result.artist}
          listIndex={index}
          handlePlay={() => handlePlay(index)}
        />
      ))}
      </div>
    </div>
  );
}

export default Sidebar;
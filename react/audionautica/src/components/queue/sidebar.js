import React from 'react';
import './sidebar.css';
import useQueue from './queue';

const Sidebar = ({ isOpen}) => {
  const {queue, removeFromQueue, currentId} = useQueue()

  return (
    <div className={`sidebar ${isOpen ? 'open' : ''}`}>
      <div className="queue-container">
      {queue.map((result, index) => (
        <div className='track' key={index}>
          <p className='trackinfo'>{result.title}</p>
          <p className='trackinfo'>{result.artist}</p>
          {index === currentId && <p>текущий трек</p>}
          <button onClick={() => removeFromQueue(index)}/>Удалить <button/>
        </div>
      ))}
      </div>
    </div>
  );
}

export default Sidebar;
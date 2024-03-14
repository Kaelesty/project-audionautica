import React, { useState } from 'react';
import './albumcoverbig.css'; 

const AlbumCoverBig = ({ coverSrc }) => {
  const [isOpen, setIsOpen] = useState(false); 

  const openModal = () => {
    setIsOpen(true);
  };

  const closeModal = () => {
    setIsOpen(false);
  };

  return (
    <div>
      <img className="open-button" alt="Open Album Cover" src={require("./icons/up-arrow.png")} onClick={openModal}/>
      {isOpen && (
        <div className="modal-overlay" onClick={closeModal}>
          <div className="modal-content">
            <button className="close-button" onClick={closeModal}>&times;</button>
            <img src={coverSrc} alt="Album Cover" className="cover-image" />
          </div>
        </div>
      )}
    </div>
  );
};

export default AlbumCoverBig;
import React, {useContext} from 'react';
import { Link } from 'react-router-dom';
import token from '../token';
import {Context} from "../index"
import "./menu.css"


const Menu = () => {
  const {isLoggedIn, setIsLoggedIn} = useContext(Context);
  const { currentTrack, setCurrentTrack} = useContext(Context)
  

  const handleLogout = () => {
    token.removeToken()
    if (currentTrack){ 
      currentTrack.pause()
      setCurrentTrack(null)
    }
    setIsLoggedIn(false)
  }

  return (
    <div className="menu">
      {isLoggedIn ? (
        <>
          <Link to="/login" className='ButtonLink'>
            <button onClick={handleLogout}>Выход</button>
          </Link>
          <Link to="/home">Домой</Link>
          <Link to="/rec">Рекомендации</Link>
          <Link to="/search">Поиск</Link>
          <Link to="/test">Тест</Link>
        </>
      ) : (
        <>
          <Link to="/login" className='right'>Войти</Link>
          <Link to="/register" className='right'>Регистрация</Link>
        </>
      )}
    </div>
    
  );
}

export default Menu;
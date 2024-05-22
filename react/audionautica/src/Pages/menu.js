import React, {useContext} from 'react';
import { Link } from 'react-router-dom';
import token from '../token';
import { Context } from "../index"
import "./menu.css"


const Menu = () => {
  const {isLoggedIn, setIsLoggedIn} = useContext(Context); 

  const handleLogout = () => {
    token.removeToken()
    setIsLoggedIn(false)
  }

  return (
    <div className="menu">
      {isLoggedIn ? (
        <>
          <Link to="/login" className='ButtonLink'>
            <button className='logout-button' onClick={handleLogout}></button>
          </Link>
          <Link to="/home">
            <button className='home-button'> </button>
          </Link>
          <Link to="/search">
            <button className='search-button'></button>
          </Link>
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
import React from 'react';
import { Link } from 'react-router-dom';
import "./menu.css"

const Menu = () => {
  return (
    <div className="menu">
      <Link to="/">Домой</Link>
      <Link to="/login">Войти</Link>
      <Link to="/register">Регистрация</Link>
    </div>
  );
}

export default Menu;
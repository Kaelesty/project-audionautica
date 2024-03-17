import React, { useContext, useState } from 'react';
import { Navigate } from 'react-router-dom';
import token from '../token';
import {Context} from "../index"
import BackURL from '../urls';
import './login.css'

const LoginPage = () => {
    const [login, setlogin] = useState('');
    const [password, setPassword] = useState('');
    const [isLoggedIn, setisLoggedIn] = useContext(Context);
    const [errorMessage, setErrorMessage] = useState('');

    const handleLoginChange = (event) => {
        setlogin(event.target.value);
    };

    const handlePasswordChange = (event) => {
        setPassword(event.target.value);
    };

    // Отправка формы
    const handleSubmit = async (event) => {
        event.preventDefault();
       
        console.log('Отправлены данные для входа:', { login, password });
    
        const tokenResponse = await getTokenFromServer(login, password);
        if (tokenResponse===400){
            setErrorMessage("Пользователь не зарегестрирован")
            return
        }
        if (tokenResponse===401){
            setErrorMessage("Неверный пароль")
            return
        }
        if (tokenResponse === null) {
            return
        }
        
        token.setToken(tokenResponse);
        console.log(token.getToken());
        setisLoggedIn(true);
        setlogin('');
        setPassword('');
    };

    if (isLoggedIn){
        return <Navigate to="/home/" />
    }

    return (
        <div className="login-page">
            <div className="login-form">
                <h2>Авторизация</h2>
                <form onSubmit={handleSubmit}>
                    <div>
                        <label htmlFor="login">Имя пользователя:</label>
                        <input
                            type="text"
                            id="login"
                            name="login"
                            value={login}
                            onChange={handleLoginChange}
                            required
                        />
                    </div>
                    <div>
                        <label htmlFor="password">Пароль:</label>
                        <input
                            type="password"
                            id="password"
                            name="password"
                            value={password}
                            onChange={handlePasswordChange}
                            required
                        />
                    </div>
                    <p className='error-message'>{errorMessage}</p>
                    <button type="submit">Войти</button>
                </form>
            </div>
        </div>
    );
};

async function getTokenFromServer(login, password) {
    const requestData = {
        "login": login,
        "password": password
    };
    try {
        const response = await fetch(BackURL + "/Auth/Login/", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestData)
        });

        if (response.status === 400) {
            return 400
          }
        if (response.status === 401) {
          return 401
        }
        
        const responseData = await response.json();
        return responseData;
       
    } catch (error) {
        console.error( error);
        return null; 
    }
}

export default LoginPage;
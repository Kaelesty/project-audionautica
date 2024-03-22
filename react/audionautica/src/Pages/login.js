import React, { useContext, useState } from 'react';
import { Navigate } from 'react-router-dom';
import token from '../token';
import {Context} from "../index"
import './login.css'

const LoginPage = () => {
    const [login, setlogin] = useState('');
    const [password, setPassword] = useState('');
    const [isLoggedIn, setisLoggedIn] = useContext(Context);

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
        if (tokenResponse !== null) {
            token.setToken(tokenResponse);
            console.log(token.getToken());
            setisLoggedIn(true);
        }
        
    
        setlogin('');
        setPassword('');
    };

    if (isLoggedIn){
        return <Navigate to="/" />
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
                    <div>
                        <button type="submit">Войти</button>
                    </div>
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
        const response = await fetch('http://127.0.0.1:8000/Auth/Login/', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestData)
        });

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        
        const responseData = await response.json();
        return responseData;
       
    } catch (error) {
        console.error( error);
        return null; 
    }
}

export default LoginPage;
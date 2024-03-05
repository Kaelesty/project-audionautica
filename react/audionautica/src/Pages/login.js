import React, { useState } from 'react';
import './login.css'

const LoginPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleUsernameChange = (event) => {
        setUsername(event.target.value);
    };

    const handlePasswordChange = (event) => {
        setPassword(event.target.value);
    };

    // Отправка формы пока что в консоль
    const handleSubmit = (event) => {
        event.preventDefault();

        console.log('Отправлены данные для входа:', { username, password });
        
        setUsername('');
        setPassword('');
    };

    return (
        <div className="login-page">
            <div className="login-form">
                <h2>Авторизация</h2>
                <form onSubmit={handleSubmit}>
                    <div>
                        <label htmlFor="username">Имя пользователя:</label>
                        <input
                            type="text"
                            id="username"
                            name="username"
                            value={username}
                            onChange={handleUsernameChange}
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

export default LoginPage;
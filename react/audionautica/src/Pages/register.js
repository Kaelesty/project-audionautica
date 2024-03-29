import React, { useState } from 'react';
import { Navigate } from 'react-router-dom';
import './register.css'; // Импорт файла стилей

const RegisterPage = () => {
    const [email, setEmail] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [passwordMismatch, setPasswordMismatch] = useState(false);
    const [registrationSuccess, setRegistrationSuccess] = useState(false);

    const handleEmailChange = (event) => {
        setEmail(event.target.value);
    };

    const handleUsernameChange = (event) => {
        setUsername(event.target.value);
    };

    const handlePasswordChange = (event) => {
        setPassword(event.target.value);
    };

    const handleConfirmPasswordChange = (event) => {
        setConfirmPassword(event.target.value);
    };

    const handleSubmit = (event) => {
        event.preventDefault();

        // Проверка пароля
        if (password !== confirmPassword) {
            setPasswordMismatch(true);
            return;
        } else {
            setPasswordMismatch(false);
        }
        postDataRegister(username,password,email)
        console.log('Отправлены данные для регистрации:', { email, username, password });
        
        setEmail('');
        setUsername('');
        setPassword('');
        setConfirmPassword('');
        setRegistrationSuccess(true);
    };

    if (registrationSuccess) {
        return <Navigate to="/login" />;
    }

    return (
        <div className="register-page">
            <div className="register-form">
                <h2>Регистрация</h2>
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
                        <label htmlFor="email">Email:</label>
                        <input
                            type="email"
                            id="email"
                            name="email"
                            value={email}
                            onChange={handleEmailChange}
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
                        <label htmlFor="confirm-password">Подтвердите пароль:</label>
                        <input
                            type="password"
                            id="confirm-password"
                            name="confirm-password"
                            value={confirmPassword}
                            onChange={handleConfirmPasswordChange}
                            required
                        />
                    </div>
                    {passwordMismatch && <p style={{ color: 'red' }}>Пароли не совпадают</p>}
                    <button type="submit">Зарегистрироваться</button>
                </form>
            </div>
        </div>
    );
};

async function postDataRegister(name, pass, login) {
    const requestData = {
        "name": name,
        "password": pass,
        "login": login
    };
    try {
      const response = await fetch('http://127.0.0.1:8000/Auth/Register/', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestData),
      });
  
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
  
      const data = await response.json();
      console.log('Success:', data);
    } catch (error) {
      console.error(error);
    }
  }

export default RegisterPage;
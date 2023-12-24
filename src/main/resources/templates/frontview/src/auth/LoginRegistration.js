import React, { useState } from 'react';
import axios from 'axios';
import './LoginRegistration.css';

const LoginRegistration = () => {
    document.body.style.backgroundColor = '#4d5dee';
    const [isLogin, setIsLogin] = useState(true);
    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        email: '',
        password: '',
    });
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(false);

    const toggleForm = () => {
        setIsLogin((prev) => !prev);
        setError(null);
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            if (isLogin) {
                const response = await axios.post('http://localhost:8080/api/client/login', formData);
                console.log('Успішний логін:', response.data);

                if (response.data.success === 'true') {
                    setSuccess(true);
                    const responseClientId = await axios.get(`http://localhost:8080/api/client/current?email=${formData.email}`);
                    const clientId = responseClientId.data.userID;
                    console.log(formData);

                    localStorage.setItem('id', clientId);
                    localStorage.setItem('firstName', responseClientId.data.firstName);
                    localStorage.setItem('lastName', responseClientId.data.lastName);
                    localStorage.setItem('email', formData.email);
                    console.log(localStorage.getItem('id'));
                    console.log(clientId);

                    window.location.href = 'http://localhost:3000/#/main';
                } else {
                    setError( 'Неправильний email або пароль. Спробуйте ще раз.');
                }
            } else {
                const response = await axios.post('http://localhost:8080/api/client/register', formData);
                const responseClientId = await axios.get(`http://localhost:8080/api/client/current?email=${formData.email}`);
                const clientId = responseClientId.data.userID;


                localStorage.setItem('id', clientId);
                localStorage.setItem('firstName', formData.firstName);
                localStorage.setItem('lastName', formData.lastName);
                localStorage.setItem('email', formData.email);
                console.log(localStorage.getItem('id'));

                console.log(clientId);
                if (response.data.success === 'true') {
                    setSuccess(true);
                    window.location.href = 'http://localhost:3000/#/main';
                } else {
                    setError('Реєстрація не вдалася, Email збігаються. Спробуйте ще раз.');
                }
            }
        } catch (error) {
            console.error('Помилка:', error);
            setError('Помилка авторизації або реєстрації. Спробуйте ще раз.');
        }
    };
    return (
        <div className="login-registration-container">
            <h3 className="form-group_title">{isLogin ? 'Логін' : 'Реєстрація'}</h3>
            {error && <div className="error-message">{error}</div>}
            <form onSubmit={handleSubmit}>
                {!isLogin && (
                    <>
                        <div className="form-group">
                            <label>Ім'я:</label>
                            <input type="text" name="firstName" value={formData.firstName} onChange={handleChange} required />
                        </div>
                        <br />
                        <div className="form-group">
                            <label>Прізвище: </label>
                            <input type="text" name="lastName" value={formData.lastName} onChange={handleChange} required />
                        </div>
                        <br />
                    </>
                )}
                <div className="form-group">
                    <label>Email:</label>
                    <input type="email" name="email" value={formData.email} onChange={handleChange} required />
                </div>
                <br />
                <div className="form-group">
                    <label>Пароль:</label>
                    <input type="password" name="password" value={formData.password} onChange={handleChange} required />
                </div>
                <br />
                <div className="form-group">
                    <button type="submit">{isLogin ? 'Увійти' : 'Зареєструватись'}</button>
                </div>
            </form>
            <button className="toggle-button" onClick={toggleForm}>
                Перейти до {isLogin ? 'реєстрації' : 'логіну'}
            </button>
        </div>
    );
};

export default LoginRegistration;

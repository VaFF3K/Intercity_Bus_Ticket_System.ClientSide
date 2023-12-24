import React from 'react';
import { Link } from 'react-router-dom';
import './OrderSuccess.css';

const OrderSuccess = () => {
    document.body.style.backgroundColor = '#3D52D5';
    return (
        <div className="order-success-container">
            <h2 className="order-success-header">Замовлення успішно створено!</h2>
            <p className="order-success-message">Дякуємо за ваше замовлення! Ваш квиток був успішно заброньований. Усі замовлення можна переглянути у "Меню" на головній сторінці.</p>
            <Link to="/" className="return-home-link">Повернутися на головну</Link>
        </div>
    );
};

export default OrderSuccess;

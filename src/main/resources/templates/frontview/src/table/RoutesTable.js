import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import './RoutesTable.css';
import logo from '../logo.png';

const RoutesTable = () => {
    document.body.style.backgroundColor = '#6D7EDC';
    const [routes, setRoutes] = useState([]);
    const [allSeatsOccupied, setAllSeatsOccupied] = useState(false);
    const [isLoggedIn, setIsLoggedIn] = useState(localStorage.getItem('id') !== null);
    const [userName, setUserName] = useState('');
    const [clientId, setClientId] = useState('');

    useEffect(() => {

        setClientId(localStorage.getItem('id'));
        setUserName(`${localStorage.getItem('firstName')} ${localStorage.getItem('lastName')}`);
        axios.get('http://localhost:8080/api/routes/all')
            .then(response => {
                const currentDate = new Date();
                const validRoutes = response.data.filter(route => new Date(route.date) > currentDate);
                setRoutes(validRoutes);
                const anyRouteWithAvailableSeats = validRoutes.some(route => route.max_seats > route.occupied_seats);
                setAllSeatsOccupied(!anyRouteWithAvailableSeats);
            })
            .catch(error => console.error('Error fetching routes:', error));


    }, []);
    const handleLogout = () => {
        localStorage.removeItem('id');
        localStorage.removeItem('firstName');
        localStorage.removeItem('lastName');
        localStorage.removeItem('email');
        setUserName('');
        setIsLoggedIn(false);
    };

    console.log(clientId);
    return (
        <div>
            <div className="header_main">
                <img src={logo} alt="Логотип" className="logo" />
                <h2 className="title">Ласкаво просимо!</h2>
                {isLoggedIn ? (
                    <div className="dropdown">
                        <button className="dropbtn">Меню</button>
                        <div className="dropdown-content">
                            <p>Авторизовано як:</p>
                            <p className="username">{userName}</p>
                            <button onClick={handleLogout}>Вихід</button>
                            <Link to="/authorization">Змінити користувача</Link>
                            <Link to={`/tickets-history/${clientId}`} >Переглянути історію квитків</Link>
                        </div>
                    </div>
                ) : (
                    <Link to="/authorization" className="auth">
                        <button>Авторизація</button>
                    </Link>
                )}
            </div>
            {allSeatsOccupied ? (
                <div>
                    <p className="sold_out">   Всі квитки на маршрути розпродано! Повертайтесь сюди пізніше!</p>
                </div>
            ) : (
                <div>

                <table>
                    <thead>
                    <tr>
                        <th>№ Маршруту</th>
                        <th>№ Автобуса</th>
                        <th>Назва Маршруту</th>
                        <th>Дата</th>
                        <th>Час</th>
                        <th>Ціна</th>
                        <th>Зайнято місць</th>
                        <th>Максимум місць</th>
                    </tr>
                    </thead>
                    <tbody>
                    {routes.map(route => (
                        route.max_seats > route.occupied_seats && (
                            <tr key={route.route_id}>
                                <td> {route.route_id} </td>
                                <td> {route.bus_number} </td>
                                <td> {route.route_name} </td>
                                <td> {route.date} </td>
                                <td> {route.time} </td>
                                <td> {route.price + " ₴"} </td>
                                <td> {route.occupied_seats} </td>
                                <td> {route.max_seats} </td>
                                <Link to={`/form/${route.route_id}`}>
                                    <button disabled={!isLoggedIn}>ОБРАТИ</button>
                                </Link>
                            </tr>
                        )
                    ))}
                    </tbody>
                </table>
                </div>
            )}
        </div>
    );
};

export default RoutesTable;
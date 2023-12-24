import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import './OrderForm.css';

const OrderForm = () => {
    document.body.style.backgroundColor = '#4d5dee';
    const { routeId } = useParams();
    const [route, setRoute] = useState({
        route_id: '',
        bus_number: '',
        route_name: '',
        date: '',
        time: '',
        price: 0,
    });

    const [orderData, setOrderData] = useState({
        firstName: '',
        lastName: '',
        email: '',
        seats: 1,
    });

    const [formCompleted, setFormCompleted] = useState(false);
    const [orderStatus, setOrderStatus] = useState(null);
    const [userEmail, setUserEmail] = useState('');
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/api/routes/${routeId}`);
                setRoute(response.data);
                const storedFirstName = localStorage.getItem('firstName') || '';
                const storedLastName = localStorage.getItem('lastName') || '';
                const storedUserEmail = localStorage.getItem('email') || '';

                setFirstName(storedFirstName);
                setLastName(storedLastName);
                setUserEmail(storedUserEmail);
            } catch (error) {
                console.error('Error fetching route:', error);
                // Log the detailed error response from the server
                if (error.response) {
                    console.error('Server responded with:', error.response.data);
                }
            }
        };
        fetchData();
    }, [routeId]);

    const handleInputChange = (e) => {
        const { name, value } = e.target;

        setOrderData((prevOrderData) => ({
            ...prevOrderData,
            [name]: value,
        }));

        validateForm();
    };


    const validateForm = () => {
        if ((orderData.firstName || firstName) && (orderData.lastName || lastName) && (orderData.email || userEmail) && orderData.seats) {
            setFormCompleted(true);
        } else {
            setFormCompleted(false);
        }
    };


    const calculateTotalPrice = () => {
        return orderData.seats * route.price;
    };

    const handleSeatsChange = async () => {
        const newSeats = orderData.seats;

        try {
            const response = await axios.put(
                `http://localhost:8080/api/routes/${routeId}/updateOccupiedSeats?occupiedSeats=${newSeats}`
            );
            setRoute((prevRoute) => ({
                ...prevRoute,
                occupied_seats: response.data.occupied_seats,
                max_seats: response.data.max_seats,
            }));

            console.log('Кількість зайнятих місць оновлено успішно');
        } catch (error) {
            if (error.response && error.response.status === 400) {
                console.error('Перевищено максимальну кількість доступних місць:', error.response.data);
            } else {
                console.error('Помилка при оновленні кількості зайнятих місць:', error);
            }
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (route.occupied_seats >= route.max_seats) {
            console.error('Максимальна кількість місць зайнята. Неможливо замовити додаткові місця.');
            return;
        }
        const responseClientId = await axios.get(`http://localhost:8080/api/client/current?email=${userEmail}`);
        const clientId = responseClientId.data.userID;
        try {
            await handleSeatsChange();
            const orderToSend = {
                route_id: route.route_id,
                client_id: clientId,
                route_name: route.route_name,
                first_name: firstName,
                last_name: lastName,
                email: userEmail,
                date: route.date + " " + route.time,
                seats: orderData.seats,
                total_price: route.price * orderData.seats,
            };

            const response = await axios.post(`http://localhost:8080/api/tickets/${clientId}/create?routeId=${route.route_id}`, orderToSend);
            console.log('Замовлення успішно відправлено:', response.data);
            window.location.href = 'http://localhost:3000/#/order-success';
            setOrderStatus('success');
        } catch (error) {
            console.error('Помилка відправлення замовлення:', error);
            setOrderStatus('error');
        }
    };

    return (

        <div className="order-form-container">
            <h2 className="title_order">Форма замовлення квитка</h2>
            <form onSubmit={handleSubmit}>
                <label className="form-label">
                    Маршрут: <span className="label-text">{route.route_name}</span>,
                    Автобус: <span className="label-text">{route.bus_number}</span>,
                    Ціна: <span className="label-text">{route.price} грн</span>
                </label>
                <br />
                <label className="form-label">
                    Ім'я:
                    <input
                        type="text"
                        name="firstName"
                        value={firstName}
                        onChange={(e) => setFirstName(e.target.value)}
                        className="form-input"
                        required
                    />
                </label>
                <br />
                <label className="form-label">
                    Прізвище:
                    <input
                        type="text"
                        name="lastName"
                        value={lastName}
                        onChange={(e) => setLastName(e.target.value)}
                        className="form-input"
                        required
                    />
                </label>
                <br />
                <label className="form-label">
                    Email:
                    <input
                        type="email"
                        name="email"
                        value={userEmail}
                        onChange={(e) => setUserEmail(e.target.value)}
                        readOnly
                        className="form-input"
                        required
                    />
                </label>
                <br />
                <label className="form-label">
                    Кількість місць:
                    <select
                        name="seats"
                        value={orderData.seats}
                        onChange={handleInputChange}
                        className="form-select"
                        required
                    >
                        {[...Array(Number.isInteger(route.max_seats)
                        && Number.isInteger(route.occupied_seats)
                        && route.max_seats >= route.occupied_seats ? route.max_seats - route.occupied_seats : 0)].map((_, index) => (
                            <option key={index + 1} value={index + 1}>
                                {index + 1}
                            </option>
                        ))}
                    </select>
                </label>
                <br />
                <label className="form-label-price">
                    До сплати: <span className="label-text"> {calculateTotalPrice()} грн</span>
                </label>
                <br />
                <button
                    type="submit"
                    className="form-button">
                    Замовити
                </button>
            </form>
            {orderStatus === 'success' && <p className="order-status">Замовлення створено успішно!</p>}
            {orderStatus === 'error' && <p className="order-status error-message">Помилка створення замовлення.</p>}
            <p className="free_seats">Вільних місць: {route.max_seats - route.occupied_seats}</p>
        </div>
    );
};

export default OrderForm;

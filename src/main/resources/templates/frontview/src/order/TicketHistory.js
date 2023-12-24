import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './TicketHistory.css';

const TicketHistory = () => {
    document.body.style.backgroundColor = '#6D7EDC';
    const [userTickets, setUserTickets] = useState([]);
    const clientId = localStorage.getItem('id');
    const handleReturnTicket = (ticketId, seats) => {
        // Ваш код для видалення квитка та оновлення значень в БД
        axios.delete(`http://localhost:8080/api/tickets/${ticketId}/return`)
            .then(response => {
                console.log(response.data);
                // Після успішного видалення оновити стан компонента
                setUserTickets(prevTickets => prevTickets.filter(ticket => ticket.id !== ticketId));
            })
            .catch(error => console.error('Error returning ticket:', error));
    };

    useEffect(() => {

            axios.get(`http://localhost:8080/api/tickets/history/${clientId}`)
                .then(response => setUserTickets(response.data))
                .catch(error => console.error('Error fetching ticket history:', error));

        console.log(clientId);

        console.log()
    }, [clientId]);

    return (
        <div>
        <div className="container_ticket">
            <h2 className="ticket_text">Історія придбаних квитків</h2>
            <table>
                <thead>
                <tr>
                    <th>№ Квитка</th>
                    <th>Ініціали покупця</th>
                    <th>Назва маршруту</th>
                    <th>Дата покупки</th>
                    <th>К-сть квитків</th>
                    <th>Загальна ціна</th>
                </tr>
                </thead>
                <tbody>
                {userTickets.map(ticket => (
                    <tr key={ticket.id}>
                        <td>{ticket.id}</td>
                        <td>{ticket.first_name} {ticket.last_name}</td>
                        <td>{ticket.route_name}</td>
                        <td>{ticket.date}</td>
                        <td>{ticket.seats}</td>
                        <td>{ticket.total_price} ₴</td>
                        <button onClick={() => handleReturnTicket(ticket.id, ticket.seats)}>
                            Повернути
                        </button>

                    </tr>
                ))}
                </tbody>
            </table>
        </div>
        </div>
    );
};

export default TicketHistory;

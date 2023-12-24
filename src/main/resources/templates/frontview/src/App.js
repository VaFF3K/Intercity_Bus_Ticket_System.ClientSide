import React, {useEffect} from 'react';
import { HashRouter as Router, Navigate, Route, Routes } from 'react-router-dom';
import RoutesTable from './table/RoutesTable';
import OrderForm from './order/OrderForm';
import OrderSuccess from "./order/OrderSuccess";
import LoginRegistration from "./auth/LoginRegistration";
import TicketHistory from "./order/TicketHistory";

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/main" element={<RoutesTable />} />
                <Route path="/form/:routeId" element={<OrderForm />} />
                <Route path="/order-success" element={<OrderSuccess/>} />
                <Route index element={<Navigate to="/main" />} />
                <Route path="/authorization" element={<LoginRegistration/>}/>
                <Route path="/tickets-history/:clientId" element={<TicketHistory/>} />
            </Routes>
        </Router>
    );
}
export default App;

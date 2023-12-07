import React, { useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate, Link } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import Feed from './Feed';
import MyPosts from './components/MyPosts';
import AuthenticationPage from './AuthenticationPage';
import { useLocation } from 'react-router-dom';
import NavigationBar from './NavigationBar'; 
import PasswordRecovery from './PasswordRecovery';



import './App.css';

const App = () => {
  const user = useSelector((state) => state.user);
  const dispatch = useDispatch();

  
  useEffect(() => {
    console.log('Redux User State:', user);
  }, [user]);
  useEffect(() => {
    if (user !== -1) {
      const timer = setTimeout(() => {
        dispatch({ type: 'LOGOUT' });
      }, 1200000); // 120000 milliseconds = 2 minutes

      // Clear the timer if the component unmounts
      return () => clearTimeout(timer);
    }
  }, [user, dispatch]);
  
  
  
  return (
    <Router>
      <NavigationBar />
      <Routes>
      <Route path="/password-recovery" element={<PasswordRecovery />} />
        <Route path="/login" element={<AuthenticationPage />} />
        <Route path="/myposts" element={user !== -1 ? <MyPosts /> : <Navigate to="/login" />} />
        <Route path="/" element={user !== -1 ? <Feed userId={user} /> : <Navigate to="/login" />} />
      </Routes>
    </Router>
  );
};
const Layout = ({ showNav = true }) => {
  const user = useSelector((state) => state.user);

  return (
    <div>
      <NavigationBar showNav={showNav} user={user} />
      {/* Routes */}
      <Routes>
        <Route path="/myposts" element={user !== -1 ? <MyPosts /> : <Navigate to="/login" />} />
        <Route path="/" element={user !== -1 ? <Feed userId={user} /> : <Navigate to="/login" />} />
      </Routes>
    </div>
  );
};





export default App;

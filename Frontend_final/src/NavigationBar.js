// NavigationBar.js
import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Link } from 'react-router-dom';

const NavigationBar = () => {
  const user = useSelector((state) => state.user);
  const dispatch = useDispatch();

  const handleLogout = () => {
    dispatch({ type: 'LOGOUT' });
  };

  if (user === -1) {
    return null; // Don't render the bar if the user is not authenticated
  }

  return (
    <nav>
      <ul>
        <li><Link to="/">Feed</Link></li>
        <li><Link to="/myposts">My Posts</Link></li>
        
        <li><Link to="/users">Users</Link></li>
        <li><button onClick={handleLogout}>Logout</button></li>
      </ul>
    </nav>
  );
};

export default NavigationBar;

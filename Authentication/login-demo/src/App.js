import React, { useState } from 'react';
import axios from 'axios';
import Welcome from './Welcome';
import { auth, googleAuthProvider, signInWithPopup,githubAuthProvider } from './firebaseConfig';
import Register from './register';
import PasswordRecovery from './PasswordRecovery';

function App() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const [user, setUser] = useState(null);
    const [showRegister, setShowRegister] = useState(false);
  const handleGoogleLogin = async () => {
    try {
      const result = await signInWithPopup(auth, googleAuthProvider);
      const idToken = await result.user.getIdToken();


      const backendResponse = await axios.post('http://localhost:8098/login', {
      type: 'google',
      idToken: idToken,
    }, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
     setUser(backendResponse.data);
      setMessage('Google login successful');
      // Handle further actions based on backendResponse
    } catch (error) {
      setMessage('Google login failed: ' + error.message);
    }
  };

  const handleEmailPasswordLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('http://localhost:8098/login', {
        type: 'email',
        email: email,
        password: password,
      });
      setMessage('Login successful');
      setUser(response.data);
      // Handle redirection or further actions here
    } catch (error) {
      setMessage('Login failed: ' + error.response.data.error);
    }
  };
  const handleRegisterSuccess = (userData) => {
    setUser(userData); // Set the user data received from the registration response
    setShowRegister(false); // Hide the registration form
  };

  if (user) {
    return <Welcome user={user} />;
  }
  return (
  <div>
      {showRegister ? (
        <>
<Register onRegisterSuccess={handleRegisterSuccess} />
          <button onClick={() => setShowRegister(false)}>Go to Login</button>
        </>
      ) : (
        <>
          <h2>Login</h2>
<form onSubmit={handleEmailPasswordLogin}>
        <input
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          placeholder="Email"
          required
        />
        <input
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          placeholder="Password"
          required
        />
        <button type="submit">Login</button>
      </form>
          <button onClick={handleGoogleLogin}>Login with Google</button>
          {message && <p>{message}</p>}
          <button onClick={() => setShowRegister(true)}>Go to Register</button>
        </>
      )}
      <PasswordRecovery />
    </div>


  );
}

export default App;


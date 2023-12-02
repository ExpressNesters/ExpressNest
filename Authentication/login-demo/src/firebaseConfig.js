import { initializeApp } from "firebase/app";
import { getAuth, GoogleAuthProvider, signInWithPopup } from "firebase/auth";
import { GithubAuthProvider } from "firebase/auth";

const firebaseConfig = {
    apiKey: "AIzaSyDSDr_R7qMzgRE5puIOfrisFgYmvVS-XIg",
    authDomain: "auth-405900.firebaseapp.com",
    projectId: "auth-405900",
    storageBucket: "auth-405900.appspot.com",
    messagingSenderId: "233586076258",
    appId: "1:233586076258:web:95c1acde289a9120833f3b",
    measurementId: "G-H6B5JXVN46"
  };

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const auth = getAuth(app);
const googleAuthProvider = new GoogleAuthProvider();
export const githubAuthProvider = new GithubAuthProvider();
export { auth, googleAuthProvider, signInWithPopup };


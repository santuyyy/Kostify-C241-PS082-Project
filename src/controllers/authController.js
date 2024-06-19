const admin = require('../config/firebaseInit');
const { getAuth, signInWithEmailAndPassword } = require('firebase/auth');
const { initializeApp } = require('firebase/app');
const dotenv = require('dotenv');
dotenv.config();

const firebaseConfig = {
    apiKey: process.env.FIREBASE_API_KEY,
    authDomain: process.env.FIREBASE_AUTH_DOMAIN,
    projectId: process.env.FIREBASE_PROJECT_ID,
    storageBucket: process.env.FIREBASE_STORAGE_BUCKET,
    messagingSenderId: process.env.FIREBASE_MESSAGING_SENDER_ID,
    appId: process.env.FIREBASE_APP_ID,
    measurementId: process.env.FIREBASE_MEASUREMENT_ID
  };

const firebaseApp = initializeApp(firebaseConfig);
const firebaseAuth = getAuth(firebaseApp);
const firestore = admin.firestore();

// Fungsi untuk mendaftarkan pengguna baru
exports.registerUser = async (req, res) => {
  try {
    const { email, password, displayName, phoneNumber } = req.body;
    console.log("Received data:", { email, password, displayName, phoneNumber });

    // Buat pengguna baru di Firebase Authentication
    const userRecord = await admin.auth().createUser({
      email,
      password,
      displayName,
      phoneNumber,
    });

    // Dapatkan UID dari pengguna yang berhasil diregistrasi
    const { uid } = userRecord;

    // Buat objek pengguna yang akan disimpan di Firestore
    const userData = {
      email: email,
      uid: uid,
      displayName: displayName,
      phoneNumber: phoneNumber,
    };

    console.log("User data to be saved:", userData);

    // Simpan objek pengguna ke Firestore
    await firestore.collection('users').doc(uid).set(userData);

    res.status(201).json({ message: 'User registered successfully', user: userRecord });
  } catch (error) {
    console.error("Error registering user:", error.message);
    res.status(400).json({ error: error.message });
  }
};


// Fungsi untuk login pengguna
exports.loginUser = async (req, res) => {
  try {
    const { email, password } = req.body;
    const userCredential = await signInWithEmailAndPassword(firebaseAuth, email, password);
    const idToken = await userCredential.user.getIdToken();
    res.status(200).json({ message: 'Login successful', idToken });
  } catch (error) {
    res.status(400).json({ error: error.message });
  }
};

// Fungsi untuk logout pengguna
exports.logoutUser = async (req, res) => {
  try {
    const idToken = req.header('Authorization')?.split(' ')[1];
    if (!idToken) {
      return res.status(401).json({ message: 'Unauthorized' });
    }

    // Revoke refresh tokens for the user
    const decodedToken = await admin.auth().verifyIdToken(idToken);
    console.log('Decoded token:', decodedToken); // Logging decoded token
    await admin.auth().revokeRefreshTokens(decodedToken.uid);
    console.log('Token revoked for UID:', decodedToken.uid); // Logging token revocation

    res.status(200).json({ message: 'Logout successful' });
  } catch (error) {
    console.error('Error revoking token:', error); // Logging error
    res.status(400).json({ error: error.message });
  }
};
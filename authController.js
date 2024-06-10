const admin = require('../config/firebaseInit');
const { getAuth, signInWithEmailAndPassword } = require('firebase/auth');
const { initializeApp } = require('firebase/app');

const firebaseConfig = {
    apiKey: "AIzaSyDUBmzr8sXEFF6_gzEIjTeoJWSOymYqUOc",
    authDomain: "kostify-database.firebaseapp.com",
    projectId: "kostify-database",
    storageBucket: "kostify-database.appspot.com",
    messagingSenderId: "176060989098",
    appId: "1:176060989098:web:6216bd2769e43010c89ad3",
    measurementId: "G-WL54FEX8Q8"
  };

const firebaseApp = initializeApp(firebaseConfig);
const firebaseAuth = getAuth(firebaseApp);
const firestore = admin.firestore();

// Fungsi untuk mendaftarkan pengguna baru
exports.registerUser = async (req, res) => {
  try {
    const { email, password } = req.body;
    const userRecord = await admin.auth().createUser({
      email,
      password,
    });

    // Dapatkan UID dari pengguna yang berhasil diregistrasi
    const { uid } = userRecord;

    // Buat objek pengguna yang akan disimpan di Firestore
    const userData = {
      email: email,
      uid: uid,
    };

    // Simpan objek pengguna ke Firestore
    await firestore.collection('users').doc(uid).set(userData);

    res.status(201).json({ message: 'User registered successfully', user: userRecord });
  } catch (error) {
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

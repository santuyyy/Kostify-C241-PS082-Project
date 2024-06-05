const admin = require('firebase-admin');

// Fungsi untuk mendaftarkan pengguna baru
exports.registerUser = async (req, res) => {
    try {
        const { email, password } = req.body;
        const userRecord = await admin.auth().createUser({
            email,
            password,
        });
        res.status(201).json({ message: 'User registered successfully', user: userRecord });
    } catch (error) {
        res.status(400).json({ error: error.message });
    }
};

// Fungsi untuk login pengguna
exports.loginUser = async (req, res) => {
    try {
        const { idToken } = req.body;
        const decodedToken = await admin.auth().verifyIdToken(idToken);
        const { uid } = decodedToken;
        res.status(200).json({ message: 'Login successful', uid });
    } catch (error) {
        res.status(400).json({ error: error.message });
    }
};

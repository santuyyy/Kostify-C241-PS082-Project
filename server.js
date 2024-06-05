const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');
const admin = require('firebase-admin');
const serviceAccount = require('./src/config/serviceAccountKey.json');

const kostRoutes = require('./src/routes/kostRoutes');

const app = express();
const PORT = process.env.PORT || 3000;

// Inisialisasi Firebase Admin SDK
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: 'https://kostify-database.firebaseio.com' // Ganti dengan URL database Firebase Anda
});

app.use(cors());
app.use(bodyParser.json());
app.use('/api', kostRoutes);

app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});

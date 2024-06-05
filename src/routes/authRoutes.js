const express = require('express');
const router = express.Router();
const authController = require('../controllers/authController');

// Rute untuk mendaftar pengguna baru
router.post('/register', authController.registerUser);

// Endpoint untuk login pengguna
router.post('/login', authController.loginUser);

module.exports = router;

const express = require('express');
const router = express.Router();
const authController = require('../controllers/authController');
const verifyToken = require('../middleware/authMiddleware');

// Rute untuk mendaftar pengguna baru
router.post('/register', authController.registerUser);

// Endpoint untuk login pengguna
router.post('/login', authController.loginUser);

// Endpoint untuk login pengguna
router.post('/logout', authController.logoutUser);

module.exports = router;

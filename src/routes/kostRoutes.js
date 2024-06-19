const express = require('express');
const router = express.Router();
const kostController = require('../controllers/kostController');

// Endpoint untuk menampilkan data Kos secara acak
router.get('/random-kos', kostController.getRandomKost);

// Endpoint untuk mencari Kos berdasarkan lokasi, harga, filter AC; WiFi; Lemari, dll.
router.get('/search', kostController.searchKostByParams);

// Endpoint untuk menampilkan data Kos berdasrkan ID
router.get('/:id', kostController.getKostById);

module.exports = router;

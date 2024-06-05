const express = require('express');
const router = express.Router();
const kostController = require('../controllers/kostController');

// Rute untuk pencarian data kost dengan query parameters
router.get('/kost', kostController.searchKost);

// Rute untuk pencarian data kost berdasarkan nama
router.get('/kost/nama/:nama_kost', kostController.searchKostByNama);

// Rute untuk pencarian data kost berdasarkan alamat
router.get('/kost/alamat/:alamat', kostController.searchKostByAlamat);

// Rute untuk pencarian data kost berdasarkan lokasi
router.get('/kost/lokasi/:lokasi_jabodetabek', kostController.searchKostByLokasi);

// Rute untuk pencarian data kost berdasarkan harga
router.get('/kost/harga/:harga', kostController.searchKostByHarga);

// Rute untuk pencarian data kost berdasarkan fasilitas AC
router.get('/kost/AC/:AC', kostController.searchKostByAC);

// Rute untuk pencarian data kost berdasarkan jumlah kasur
router.get('/kost/Kasur/:Kasur', kostController.searchKostByKasur);

// Rute untuk pencarian data kost berdasarkan jumlah lemari
router.get('/kost/Lemari/:Lemari', kostController.searchKostByLemari);

// Rute untuk pencarian data kost berdasarkan ketersediaan WiFi
router.get('/kost/WiFi/:WiFi', kostController.searchKostByWiFi);

// Rute untuk pencarian data kost berdasarkan ketersediaan WC duduk
router.get('/kost/Wc_duduk/:Wc_duduk', kostController.searchKostByWc);

// Rute untuk pencarian data kost berdasarkan ketersediaan kamar mandi dalam
router.get('/kost/Kamar_mandi_dalam/:Kamar_mandi_dalam', kostController.searchKostByKamarMandiDalam);

// Rute untuk pencarian data kost berdasarkan ketersediaan listrik
router.get('/kost/Listrik/:Listrik', kostController.searchKostByListrik);

// Rute untuk pencarian data kost berdasarkan luas kamar
router.get('/kost/Luas_kamar/:Luas_kamar', kostController.searchKostByLuasKamar);

module.exports = router;

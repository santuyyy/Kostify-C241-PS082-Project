const fs = require('fs');
const path = require('path');
const bodyParser = require('body-parser');
const axios = require('axios');
const dotenv = require('dotenv');
dotenv.config();

// controller to get random kost
exports.getRandomKost = (req, res) => {
    // Terima parameter dari permintaan HTTP
    const { count, orderBy } = req.query;

    axios.get(`${process.env.URL_ML}/api/recommendations`)
        .then(response => {
            let kostData = response.data;
            
            // Urutkan data berdasarkan kriteria yang diberikan
            kostData = sortByCriteria(kostData, orderBy || 'harga'); // Jika tidak ada orderBy, urutkan berdasarkan harga
            
            // Shuffle data setelah diurutkan
            kostData = shuffleArray(kostData);
            
            // Pilih beberapa data secara acak dari data yang sudah diurutkan dan diacak
            const selectedKosts = selectItems(kostData, count || 10); // Misalnya, pilih 10 data jika count tidak diberikan
            
            // Bungkus hasil dengan objek items
            res.json({ randomkostitem: selectedKosts });
        })
        .catch(error => {
            res.status(500).json({ error: 'Failed to fetch ranked random kost data from ML model' });
        });
};

// controller to get random kost with shuffle
exports.searchKostByParams = (req, res) => {
    // Dapatkan query parameters dari permintaan
    const params = req.query;
    
    // Lakukan permintaan HTTP ke endpoint model
    axios.get(`${process.env.URL_ML}/api/recommendations`, {
        params: params // Sertakan semua query parameters dari permintaan
    })
    .then(response => {
        // Tanggapan yang diterima dari model
        let kostData = response.data;
        
        // Shuffle data setelah menerima respons dari model
        kostData = shuffleArray(kostData);
        
        // Bungkus data dalam objek 'items'
        const wrappedData = { items: kostData };
        
        // Kirim seluruh data yang diterima dan sudah di-shuffle ke klien
        res.json(wrappedData);
    })
    .catch(error => {
        // Tangani kesalahan jika permintaan gagal
        res.status(500).json({ error: 'Failed to fetch shuffled kost data from ML model' });
    });
};

// controller to get kost by ID
exports.getKostById = (req, res) => {
    const id = req.params.id;
    
    axios.get(`${process.env.URL_ML}/kosan/${id}`)
        .then(response => {
            const kostData = response.data;
            res.json(kostData);
        })
        .catch(error => {
            res.status(500).json({ error: 'Failed to fetch kost data by ID from ML model' });
        });
};

// Fungsi untuk mengurutkan data berdasarkan kriteria tertentu
function sortByCriteria(array, orderBy) {
    return array.sort((a, b) => {
        // Ubah nilai ke dalam bentuk angka jika perlu
        const aValue = parseFloat(a[orderBy]) || 0;
        const bValue = parseFloat(b[orderBy]) || 0;
        
        // Bandingkan nilai berdasarkan kriteria yang diberikan
        return aValue - bValue;
    });
}

// Fungsi untuk mengacak urutan elemen dalam array
function shuffleArray(array) {
    for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
    }
    return array;
}

// Fungsi untuk memilih beberapa item dari array
function selectItems(array, count) {
    return array.slice(0, count);
}

const express = require('express');
const router = express.Router();
const fs = require('fs');
const path = require('path');

// Endpoint untuk mendapatkan semua kost
router.get('/kosts', (req, res) => {
    fs.readFile(path.join(__dirname, '../data/kost.json'), 'utf8', (err, data) => {
        if (err) {
            res.status(500).json({ message: 'Error reading kost data' });
        } else {
            res.json(JSON.parse(data));
        }
    });
});

// Endpoint untuk mendapatkan kost berdasarkan ID
router.get('/kosts/:id', (req, res) => {
    const kostId = parseInt(req.params.id, 10);
    fs.readFile(path.join(__dirname, '../data/kost.json'), 'utf8', (err, data) => {
        if (err) {
            res.status(500).json({ message: 'Error reading kost data' });
        } else {
            const kosts = JSON.parse(data);
            const kost = kosts.find(k => k.id === kostId);
            if (kost) {
                res.json(kost);
            } else {
                res.status(404).json({ message: 'Kost not found' });
            }
        }
    });
});

module.exports = router;

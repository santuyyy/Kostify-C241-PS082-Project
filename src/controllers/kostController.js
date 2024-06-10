const fs = require('fs');
const path = require('path');

const kostFilePath = path.join(__dirname, '../data/datakost.json');

// Helper function to read kost data
function readKostData(callback) {
    fs.readFile(kostFilePath, 'utf8', (err, data) => {
        if (err) {
            return callback(err, null);
        }
        const kostData = JSON.parse(data);
        callback(null, kostData);
    });
}

// Kontroller untuk pencarian data kost dengan query parameters
exports.searchKost = (req, res) => {
    readKostData((err, kostData) => {
        if (err) {
            return res.status(500).json({ error: 'Failed to read kost data' });
        }

        // Mengambil query parameters
        const { lokasi_jabodetabek, harga, AC } = req.query;

        // Filter data kost berdasarkan query parameters
        let filteredKost = kostData;

        if (lokasi_jabodetabek) {
            filteredKost = filteredKost.filter(kost => kost.lokasi_jabodetabek.toLowerCase().includes(lokasi_jabodetabek.toLowerCase()));
        }

        if (harga) {
            const maxHarga = parseFloat(harga);
            filteredKost = filteredKost.filter(kost => kost.harga <= maxHarga);
        }

        if (AC) {
            const acValue = parseInt(AC);
            filteredKost = filteredKost.filter(kost => kost.AC === acValue);
        }

        // Mengirimkan data yang telah difilter sebagai respon
        res.json(filteredKost);
    });
};

// Kontroller untuk pencarian data kost berdasarkan nama
exports.searchKostByNama = (req, res) => {
    const nama_kost = req.params.nama_kost.toLowerCase();
    readKostData((err, kostData) => {
        if (err) {
            return res.status(500).json({ error: 'Failed to read kost data' });
        }
        const filteredData = kostData.filter(kost => kost.nama_kost.toLowerCase().includes(nama_kost));
        res.json(filteredData);
    });
};

// Kontroller untuk pencarian data kost berdasarkan alamat
exports.searchKostByAlamat = (req, res) => {
    const alamat = req.params.alamat.toLowerCase();
    readKostData((err, kostData) => {
        if (err) {
            return res.status(500).json({ error: 'Failed to read kost data' });
        }
        const filteredData = kostData.filter(kost => kost.alamat.toLowerCase().includes(alamat));
        res.json(filteredData);
    });
};

// Kontroller untuk pencarian data kost berdasarkan lokasi
exports.searchKostByLokasi = (req, res) => {
    const lokasi = req.params.lokasi_jabodetabek.toLowerCase();
    readKostData((err, kostData) => {
        if (err) {
            return res.status(500).json({ error: 'Failed to read kost data' });
        }
        const filteredData = kostData.filter(kost => kost.lokasi_jabodetabek.toLowerCase().includes(lokasi));
        res.json(filteredData);
    });
};

// Kontroller untuk pencarian data kost berdasarkan harga
exports.searchKostByHarga = (req, res) => {
    const harga = parseInt(req.params.harga);
    readKostData((err, kostData) => {
        if (err) {
            return res.status(500).json({ error: 'Failed to read kost data' });
        }
        const filteredData = kostData.filter(kost => kost.harga === harga);
        res.json(filteredData);
    });
};

// Kontroller untuk pencarian data kost berdasarkan fasilitas AC
exports.searchKostByAC = (req, res) => {
    const AC = parseInt(req.params.AC);
    readKostData((err, kostData) => {
        if (err) {
            return res.status(500).json({ error: 'Failed to read kost data' });
        }
        const filteredData = kostData.filter(kost => kost.AC === AC);
        res.json(filteredData);
    });
};

// Kontroller untuk pencarian data kost berdasarkan jumlah Kasur
exports.searchKostByKasur = (req, res) => {
    const Kasur = parseInt(req.params.Kasur);
    readKostData((err, kostData) => {
        if (err) {
            return res.status(500).json({ error: 'Failed to read kost data' });
        }
        const filteredData = kostData.filter(kost => kost.Kasur === Kasur);
        res.json(filteredData);
    });
};

// Kontroller untuk pencarian data kost berdasarkan keberadaan Lemari
exports.searchKostByLemari = (req, res) => {
    const Lemari = parseInt(req.params.Lemari);
    readKostData((err, kostData) => {
        if (err) {
            return res.status(500).json({ error: 'Failed to read kost data' });
        }
        const filteredData = kostData.filter(kost => kost.Lemari === Lemari);
        res.json(filteredData);
    });
};

// Kontroller untuk pencarian data kost berdasarkan ketersediaan WiFi
exports.searchKostByWiFi = (req, res) => {
    const WiFi = parseInt(req.params.WiFi);
    readKostData((err, kostData) => {
        if (err) {
            return res.status(500).json({ error: 'Failed to read kost data' });
        }
        const filteredData = kostData.filter(kost => kost.WiFi === WiFi);
        res.json(filteredData);
    });
};

// Kontroller untuk pencarian data kost berdasarkan ketersediaan Wc_duduk
exports.searchKostByWc = (req, res) => {
    const Wc_duduk = parseInt(req.params.Wc_duduk);
    readKostData((err, kostData) => {
        if (err) {
            return res.status(500).json({ error: 'Failed to read kost data' });
        }
        const filteredData = kostData.filter(kost => kost.Wc_duduk === Wc_duduk);
        res.json(filteredData);
    });
};

// Kontroller untuk pencarian data kost berdasarkan ketersediaan Kamar_mandi_dalam
exports.searchKostByKamarMandiDalam = (req, res) => {
    const Kamar_mandi_dalam = parseInt(req.params.Kamar_mandi_dalam);
    readKostData((err, kostData) => {
        if (err) {
            return res.status(500).json({ error: 'Failed to read kost data' });
        }
        const filteredData = kostData.filter(kost => kost.Kamar_mandi_dalam === Kamar_mandi_dalam);
        res.json(filteredData);
    });
};

// Kontroller untuk pencarian data kost berdasarkan fasilitas Listrik
exports.searchKostByListrik = (req, res) => {
    const Listrik = parseInt(req.params.Listrik);
    readKostData((err, kostData) => {
        if (err) {
            return res.status(500).json({ error: 'Failed to read kost data' });
        }
        const filteredData = kostData.filter(kost => kost.Listrik === Listrik);
        res.json(filteredData);
    });
};

// Kontroller untuk pencarian data kost berdasarkan luas kamar
exports.searchKostByLuasKamar = (req, res) => {
    const Luas_kamar = req.params.Luas_kamar;
    readKostData((err, kostData) => {
        if (err) {
            return res.status(500).json({ error: 'Failed to read kost data' });
        }
        const filteredData = kostData.filter(kost => kost.Luas_kamar === Luas_kamar);
        res.json(filteredData);
    });
};
const admin = require('../config/firebaseInit');
const db = admin.firestore();
const axios = require('axios');

exports.addBookmark = async (req, res) => {
  try {
    const { userId, kostId } = req.body;

    // Lakukan permintaan ke model ML untuk mendapatkan data kost berdasarkan kostId
    const mlResponse = await axios.get(`${process.env.URL_ML}/kosan/${kostId}`);
    const kostData = mlResponse.data;

    // Gabungkan data dari model ML dengan informasi bookmark
    const newBookmark = {
      userId,
      kostId,
      kostDetails: kostData,  // Menyimpan data kost yang diterima dari model ML
      createdAt: new Date()
    };

    // Simpan data bookmark yang telah digabungkan ke Firestore
    const bookmarkRef = await db.collection('bookmarks').add(newBookmark);

    res.status(201).json({ message: 'Bookmark added successfully', id: bookmarkRef.id });
  } catch (error) {
    res.status(400).json({ error: error.message });
  }
};

exports.getBookmarks = async (req, res) => {
    try {
      const { userId } = req.params;
      // Pastikan userId yang digunakan berasal dari data pengguna yang terotentikasi
      // (Misalnya, ambil userId dari token Firebase)
      const bookmarksSnapshot = await db.collection('bookmarks').where('userId', '==', userId).get();
      const bookmarks = bookmarksSnapshot.docs.map(doc => ({ id: doc.id, ...doc.data() }));
      res.status(200).json(bookmarks);
    } catch (error) {
      res.status(500).json({ error: 'Failed to fetch bookmarks' });
    }
  };

exports.removeBookmark = async (req, res) => {
    try {
      const { bookmarkId } = req.body;
      // Pastikan bookmarkId ada dan valid sebelum mencoba menghapusnya
      if (!bookmarkId) {
        return res.status(400).json({ error: 'Bookmark ID is required' });
      }
  
      await db.collection('bookmarks').doc(bookmarkId).delete();
      res.status(200).json({ message: 'Bookmark removed successfully' });
    } catch (error) {
      res.status(500).json({ error: 'Failed to remove bookmark' });
    }
  };
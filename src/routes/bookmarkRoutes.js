const express = require('express');
const router = express.Router();
const bookmarkController = require('../controllers/bookmarkController');

// Endpoint untuk melakukan penambahan Bookmark
router.post('/add', bookmarkController.addBookmark);

// Endpoint untuk melakukan penghapusan bookmark
router.delete('/remove', bookmarkController.removeBookmark);

// Endpoint untuk menampilkan list bookmark berdasarkan user id
router.get('/:userId', bookmarkController.getBookmarks);

module.exports = router;

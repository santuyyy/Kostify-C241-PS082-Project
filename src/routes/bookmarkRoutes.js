const express = require('express');
const router = express.Router();
const bookmarkController = require('../controllers/bookmarkController');

router.post('/add', bookmarkController.addBookmark);
router.get('/:userId', bookmarkController.getBookmarks);
router.delete('/remove', bookmarkController.removeBookmark);

module.exports = router;

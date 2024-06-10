const admin = require('../config/firebaseInit');
const db = admin.firestore();

exports.addBookmark = async (req, res) => {
  try {
    const { userId, kostId } = req.body;
    const newBookmark = {
      userId,
      kostId,
      createdAt: new Date()
    };
    const bookmarkRef = await db.collection('bookmarks').add(newBookmark);
    res.status(201).json({ message: 'Bookmark added successfully', id: bookmarkRef.id });
  } catch (error) {
    res.status(400).json({ error: error.message });
  }
};

exports.getBookmarks = async (req, res) => {
  try {
    const { userId } = req.params;
    const bookmarksSnapshot = await db.collection('bookmarks').where('userId', '==', userId).get();
    const bookmarks = bookmarksSnapshot.docs.map(doc => ({ id: doc.id, ...doc.data() }));
    res.status(200).json(bookmarks);
  } catch (error) {
    res.status(400).json({ error: error.message });
  }
};

exports.removeBookmark = async (req, res) => {
  try {
    const { bookmarkId } = req.body;
    await db.collection('bookmarks').doc(bookmarkId).delete();
    res.status(200).json({ message: 'Bookmark removed successfully' });
  } catch (error) {
    res.status(400).json({ error: error.message });
  }
};

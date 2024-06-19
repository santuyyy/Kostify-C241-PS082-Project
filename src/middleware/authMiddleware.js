const admin = require('firebase-admin');

// Middleware untuk memverifikasi token ID Firebase
const verifyToken = async (req, res, next) => {
  const authHeader = req.headers.authorization;
  
  if (authHeader && authHeader.startsWith('Bearer ')) {
    const idToken = authHeader.split(' ')[1];

    try {
      const decodedToken = await admin.auth().verifyIdToken(idToken);
      req.user = decodedToken; // Menyimpan informasi pengguna yang terverifikasi di req.user
      next(); // Lanjutkan ke middleware atau handler berikutnya
    } catch (error) {
      res.status(401).json({ error: 'Unauthorized' });
    }
  } else {
    res.status(401).json({ error: 'Unauthorized' });
  }
};

module.exports = verifyToken;

const express = require('express');
const bodyParser = require('body-parser');
const kostRoutes = require('./src/routes/routes');

const app = express();
const PORT = process.env.PORT || 3000;

app.use(bodyParser.json());
app.use('/api', kostRoutes);

app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});

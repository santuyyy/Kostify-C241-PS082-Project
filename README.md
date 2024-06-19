# Kostify-Backend

This project focuses on finding the ideal place to live, which is often challenging for some people. High rental costs, fewer strategic locations, and inadequate facilities are the main obstacles to the boarding house search process. To solve this problem, we introduce “Kostify,” a smart boarding house search application specifically designed to make it easier for a user to find a place to live that suits their needs and preferences. Kostify combines advanced technology with a smart and user-friendly interface. This app can provide the right boarding house recommendations based on location, price, facilities, and other preferences the user wants.

## Authors

- [Reva Rasya Rafsanjani](https://github.com/RevaRafsanjani)
- [Fachrudin Nur Alfiansyah](https://github.com/aalffi)

## kostify-api

To run this api in your locally:

1. Change directory to kostify-api
```bash
  cd kostify-api
```
2. run NPM install
```bash
  npm run install
```
3. You need to edit the .env file with your configuration
```bash
# Model Machine Learning
URL_ML = https://model-ml-ia6oyiqe5q-et.a.run.app

# Key for Database
FIREBASE_API_KEY="AIzaSyDUBmzr8sXEFF6_gzEIjTeoJWSOymYqUOc"
FIREBASE_AUTH_DOMAIN="kostify-database.firebaseapp.com"
FIREBASE_PROJECT_ID="kostify-database"
FIREBASE_STORAGE_BUCKET="kostify-database.appspot.com"
FIREBASE_MESSAGING_SENDER_ID="176060989098"
FIREBASE_APP_ID="1:176060989098:web:6216bd2769e43010c89ad3"
FIREBASE_MEASUREMENT_ID="G-WL54FEX8Q8"

GOOGLE_APPLICATION_CREDENTIALS="./serviceAccountKey.json"
```
4. Create database firestore in firebase
5. run the 
```bash
  npm run start
```
## Kostify Back-end Documentation

Postman Collection: [CLICK HERE](https://www.postman.com/speeding-equinox-272267/workspace/api-test-kostify/collection/30911472-8ad794c2-9d65-405f-b413-61a292d11895?action=share&creator=30911472)

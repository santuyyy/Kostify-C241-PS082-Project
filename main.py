from flask import Flask, jsonify, request
import modelling
import pandas as pd
import os

app = Flask(__name__)

# Load the dataset
data = pd.read_csv('data_kosan.csv')

# Convert the dataframe to a dictionary for easy access by ID
data_dict = data.set_index('id').T.to_dict()

@app.route('/', methods=['GET'])
def home():
    return "API is running!"

@app.route('/api/recommendations', methods=['GET'])
def get_recommendations():
    # Get data from query parameters
    harga = request.args.get('harga', 0)
    lokasi = request.args.get('lokasi', None)
    gender = request.args.get('gender', None)
    AC = int(request.args.get('AC', 0))
    Kasur = int(request.args.get('Kasur', 0))
    Lemari = int(request.args.get('Lemari', 0))
    WiFi = int(request.args.get('WiFi', 0))
    Wc_duduk = int(request.args.get('Wc_duduk', 0))
    Kamar_mandi_dalam = int(request.args.get('Kamar_mandi_dalam', 0))
    Listrik = int(request.args.get('Listrik', 0))
    
    # Get recommendations from the model
    recommendations = modelling.get_recommendations_by_input(
        harga=int(harga), 
        lokasi=lokasi, 
        gender=gender,
        AC=AC, 
        Kasur=Kasur, 
        Lemari=Lemari, 
        WiFi=WiFi, 
        Wc_duduk=Wc_duduk, 
        Kamar_mandi_dalam=Kamar_mandi_dalam, 
        Listrik=Listrik,
    )

    # Return recommendations in JSON format
    return jsonify(recommendations.to_dict(orient='records'))

@app.route('/kosan/<int:id>', methods=['GET'])
def get_kosan(id):
    kosan = data_dict.get(id)
    if kosan:
        # Filter kosan data to include only specified fields
        filtered_kosan = {
            'id': id,
            'nama_kost': kosan['nama_kost'],
            'gender': kosan['gender'],
            'alamat': kosan['alamat'],
            'harga': kosan['harga'],
            'Luas kamar': kosan['Luas kamar'],
            'deskripsi': kosan['deskripsi']
        }
        return jsonify(filtered_kosan)
    else:
        return jsonify({'error': 'Data not found'}), 404


if __name__ == '__main__':
    port = int(os.environ.get('PORT', 8080))
    app.run(host='0.0.0.0', port=port)

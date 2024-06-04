import pandas as pd
import numpy as np
import tensorflow as tf
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.preprocessing import LabelEncoder

# Load the new dataset
data = pd.read_csv('data_kosan.csv')

# Custom list of Indonesian stop words (simplified for demonstration purposes)
indonesian_stop_words = [
    "yang", "untuk", "dengan", "pada", "ini", "dan", "di", "ke", "dari", "adalah",
    "itu", "atau", "oleh", "seperti", "jika", "karena", "sudah", "dalam", "akan",
    "juga", "kami", "sangat", "hanya", "mereka", "saja", "agar", "tetapi"
]

# Combine the relevant features into a single text feature
data['combined_features'] = data.apply(lambda row: f"{'AC' if row['AC'] else ''} "
                                                   f"{'Kasur' if row['Kasur'] else ''} "
                                                   f"{'Lemari' if row['Lemari'] else ''} "
                                                   f"{'WiFi' if row['WiFi'] else ''} "
                                                   f"{'Wc duduk' if row['Wc duduk'] else ''} "
                                                   f"{'Kamar mandi dalam' if row['Kamar mandi dalam'] else ''} "
                                                   f"{'Listrik' if row['Listrik'] else ''} "
                                                   f"{row['deskripsi']}", axis=1)

# Vectorize the combined text features using TF-IDF with custom Indonesian stop words
tfidf = TfidfVectorizer(stop_words=indonesian_stop_words)
tfidf_matrix = tfidf.fit_transform(data['combined_features'])

# Encode 'lokasi_jabodetabek'
le_lokasi = LabelEncoder()
data['lokasi_encoded'] = le_lokasi.fit_transform(data['lokasi_jabodetabek'])

# Combine the TF-IDF matrix with the encoded features and 'harga'
additional_features = np.array(data[['lokasi_encoded', 'harga']])
feature_matrix = np.hstack((tfidf_matrix.toarray(), additional_features))

# Convert feature_matrix to TensorFlow tensor
feature_matrix_tf = tf.convert_to_tensor(feature_matrix, dtype=tf.float32)

# Function to get kost recommendations based on user input
def get_recommendations_by_input(harga=None, lokasi=None, AC=0, Kasur=0, Lemari=0, WiFi=0, Wc_duduk=0, Kamar_mandi_dalam=0, Listrik=0, deskripsi=None):
    # Create a query string for combined features
    facilities = {
        'AC': AC,
        'Kasur': Kasur,
        'Lemari': Lemari,
        'WiFi': WiFi,
        'Wc duduk': Wc_duduk,
        'Kamar mandi dalam': Kamar_mandi_dalam,
        'Listrik': Listrik
    }
    
    query = ' '.join([facility for facility, value in facilities.items() if value == 1])
    
    if deskripsi:
        query += ' ' + deskripsi
    
    # Vectorize the query using the same TF-IDF vectorizer
    query_tfidf = tfidf.transform([query])
    
    # Encode lokasi if provided
    lokasi_encoded = le_lokasi.transform([lokasi])[0] if lokasi else 0
    
    # Construct the feature vector for the query
    query_features = np.hstack((query_tfidf.toarray(), [[lokasi_encoded, harga]]))
    
    # Convert query_features to TensorFlow tensor
    query_features_tf = tf.convert_to_tensor(query_features, dtype=tf.float32)
    
    # Compute the cosine similarity between the query and all kosts
    query_sim = tf.linalg.matmul(query_features_tf, feature_matrix_tf, transpose_b=True)
    
    # Get the top 5 most similar kosts
    query_sim_scores = tf.argsort(query_sim, direction='DESCENDING')[0][:5].numpy()
    
    return data[['nama_kost', 'alamat', 'harga', 'Luas kamar', 'deskripsi']].iloc[query_sim_scores]

# Example: Get recommendations based on user input
user_recommendations = get_recommendations_by_input(
    harga=1000000, 
    lokasi='Depok', 
    AC=0, 
    Kasur=1, 
    Lemari=1, 
    WiFi=1, 
    Wc_duduk=0, 
    Kamar_mandi_dalam=0, 
    Listrik=1
)

print(user_recommendations)
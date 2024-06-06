import pandas as pd
import numpy as np
import tensorflow as tf
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.preprocessing import LabelEncoder, StandardScaler
from sklearn.model_selection import train_test_split
from sklearn.metrics.pairwise import cosine_similarity

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

# Normalize the feature matrix
scaler = StandardScaler()
feature_matrix = scaler.fit_transform(feature_matrix)

# Split the data into training and testing sets
X_train, X_test = train_test_split(feature_matrix, test_size=0.2, random_state=42)

# Define the neural network model
model = tf.keras.Sequential([
    tf.keras.layers.Dense(256, activation='relu', input_shape=(feature_matrix.shape[1],)),
    tf.keras.layers.Dropout(0.2),
    tf.keras.layers.Dense(128, activation='relu'),
    tf.keras.layers.Dropout(0.2),
    tf.keras.layers.Dense(feature_matrix.shape[1], activation='linear')
])

model.compile(optimizer='adam', loss='mse')

# Train the model
model.fit(X_train, X_train, epochs=5, batch_size=32, validation_split=0.2)

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
    
    # Normalize the query feature vector
    query_features = scaler.transform(query_features)
    
    # Predict the similarity scores using the neural network model
    query_pred = model.predict(query_features)
    
    # Compute the cosine similarity between the query and all kosts
    query_sim = cosine_similarity(query_pred, feature_matrix)
    
    # Get the indices of kosts that match the specified location
    if lokasi:
        location_indices = data[data['lokasi_jabodetabek'] == lokasi].index
    else:
        location_indices = data.index
    
    # Filter the similarity scores to include only kosts from the specified location
    query_sim = query_sim[:, location_indices]
    
    # Get the top 5 most similar kosts
    query_sim_scores = list(enumerate(query_sim[0]))
    query_sim_scores = sorted(query_sim_scores, key=lambda x: x[1], reverse=True)
    query_sim_scores = query_sim_scores[:10]
    kost_indices = [location_indices[i[0]] for i in query_sim_scores]
    
    return data[['nama_kost', 'alamat', 'harga', 'Luas kamar', 'deskripsi']].iloc[kost_indices]

# Set pandas display option to show full content
pd.set_option('display.max_colwidth', None)
# Gunakan image dasar yang sesuai
FROM python:3.9-slim

# Tentukan working directory
WORKDIR /app

# Salin requirements.txt dan install dependencies
COPY requirements.txt requirements.txt

RUN pip install -r requirements.txt

# Salin semua file aplikasi ke working directory
COPY . .

EXPOSE 8080

# Tentukan perintah untuk menjalankan aplikasi
CMD python3 main.py



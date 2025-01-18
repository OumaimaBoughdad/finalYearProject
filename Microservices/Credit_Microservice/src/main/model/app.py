from flask import Flask, request, jsonify
from flask_cors import CORS
import pandas as pd
import joblib

app = Flask(__name__)

CORS(app)

# Load the model
model = joblib.load('credit_risk_model.pkl')

# Load label encoders
LE_mappings = joblib.load('MODEL3_label_encoders.pkl')

@app.route('/predict', methods=['POST'])
def predict():
    client_data = request.json
    df = pd.DataFrame([client_data])

    # Apply label encoding
    for col, mapping in LE_mappings.items():
        if col in df.columns:
            df[col] = df[col].map(mapping)

    # Load model and make prediction
    prediction = model.predict(df)
    return jsonify({"prediction": int(prediction[0])})

if __name__ == '__main__':
    app.run(port=5000, debug=True)

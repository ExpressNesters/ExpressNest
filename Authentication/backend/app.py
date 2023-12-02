from flask import Flask, request, jsonify, send_file
import firebase_admin
from firebase_admin import credentials, firestore
import requests
import base64
import pyotp
from PIL import Image
from io import BytesIO
import uuid
from flask import Flask, request, jsonify, session
from flask_session import Session
from flask_cors import CORS  # Import CORS
import firebase_admin
from firebase_admin import credentials, firestore
import pyotp
import qrcode
from firebase_admin import credentials, auth
import requests
import logging

# Inside the registration function
user_uid = str(uuid.uuid4())

FIREBASE_WEB_API_KEY = 'AIzaSyDSDr_R7qMzgRE5puIOfrisFgYmvVS-XIg'
# Initialize Firebase Admin SDK
cred = credentials.Certificate("auth.json")
firebase_admin.initialize_app(cred)

# Firestore database
db = firestore.client()

app = Flask(__name__)
app.config["SESSION_PERMANENT"] = False
app.config["SESSION_TYPE"] = "filesystem"
Session(app)

# Enable CORS
CORS(app)
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

@app.route('/users', methods=['POST'])
def register_user():
    data = request.json
    # Send POST request to external service to get user ID
    logger.info(data)
    response = requests.post("http://user-app-1:8088/users", json=data)
    logger.info(response)
    if response.status_code != 201:
        return jsonify({"error": "Failed to get user ID"}), response.status_code
    logger.info(response.json())
    user_id = response.json().get("id")

    # Generate a 2FA secret
    secret = pyotp.random_base32()

    # Prepare user data for Firestore
    user_data = {
        "Username": data.get("Username"),
        "Email": data.get("Email"),
        "HashedPassword": data.get("HashedPassword"),
        "PersonalDetails": data.get("PersonalDetails"),
        "Private": data.get("Private"),
        "UserID": user_id,
        "TwoFactorSecret": secret
    }

    # Store in Firestore using email as the document ID
    db.collection('users').document(data.get("Email")).set(user_data)
    # On successful registration, save user details in session
    session['username'] = data.get('Username')
    session['email'] = data.get('Email')
    session['user_id'] = user_id  # Assuming user_id is obtained as part of registration

    return jsonify({"email": data.get("Email"), "userID": user_id, "secret": secret,"username":data.get('Username')}), 201

@app.route('/qr_code', methods=['GET'])
def qr_code():
    user_email = request.args.get('email')
    if not user_email:
        return jsonify({"error": "Email is required"}), 400

    # Retrieve user's 2FA secret from Firestore
    user_ref = db.collection('users').document(user_email)
    user_doc = user_ref.get()
    if not user_doc.exists:
        return jsonify({"error": "User not found"}), 404

    secret = user_doc.to_dict().get('TwoFactorSecret')

    # Generate QR code
    uri = pyotp.totp.TOTP(secret).provisioning_uri(name=user_email, issuer_name='expressNest')
    qr = qrcode.QRCode(
    version=1,
    error_correction=qrcode.constants.ERROR_CORRECT_L,
    box_size=10,
    border=4,
    )
    qr.add_data(uri)
    qr.make(fit=True)
    img = qr.make_image(fill_color="black", back_color="white")
    # Save QR Code to a BytesIO buffer

    buffered = BytesIO()
    img.save(buffered, format="PNG")
    img_str = base64.b64encode(buffered.getvalue()).decode()
    return jsonify({"qr_code": f"data:image/png;base64,{img_str}"})
    return send_file(buf, mimetype='image/jpeg')

@app.route('/validate_2fa', methods=['POST'])
def validate_2fa():
    data = request.json
    user_email = data.get("email")
    token = data.get("token")

    if not user_email or not token:
        return jsonify({"error": "Email and token are required"}), 400

    # Retrieve user's 2FA secret from Firestore
    user_ref = db.collection('users').document(user_email)
    user_doc = user_ref.get()
    if not user_doc.exists:
        return jsonify({"error": "User not found"}), 404

    secret = user_doc.to_dict().get('TwoFactorSecret')
    totp = pyotp.TOTP(secret)
    user_data = user_doc.to_dict()
    # Verify the 2FA token
    if totp.verify(token):
        return jsonify({"email": user_email, "username": user_data.get('Username'), "userID": user_data.get('UserID')}), 200
    else:
        return jsonify({"error": "Invalid 2FA token"}), 400



@app.route('/login', methods=['POST'])
def login():
    data = request.json
    login_type = data.get("type")

    if login_type == "email":
        return login_with_email(data)
    elif login_type == "google" or login_type == "github":
        return login_with_oauth(data, login_type)
    else:
        return jsonify({"error": "Invalid login type"}), 400

def login_with_email(data):
    logger.info("email")
    logger.info(data)
    email = data.get("email")
    password = data.get("password")  # Assume hashed password

    user_ref = db.collection('users').document(email)
    user_doc = user_ref.get()
    if user_doc.exists:
        user_data = user_doc.to_dict()
        if user_data['HashedPassword'] == password:
            # Set up session
            session['email'] = email
            session['username'] = user_data.get('Username')
            session['user_id'] = user_data.get('UserID')
            return jsonify({"email": email, "username": user_data.get('Username'), "userID": user_data.get('UserID')}), 200
        else:
            return jsonify({"error": "Invalid credentials"}), 401
    else:
        return jsonify({"error": "User not found"}), 404

def login_with_oauth(data, provider):
    id_token = data.get("idToken")

    # Verify the Firebase ID token
    logger.info("id")
    logger.info(id_token)
    try:
        if(provider=="google"):
            decoded_token = auth.verify_id_token(id_token)
            logger.info(decoded_token)
        else:
            decoded_token = auth.verify_id_token(id_token)
            github_id = decoded_token["firebase"]["identities"]["github"][0]
            github_user_url = f'https://api.github.com/users/{github_id}'
            github_response = requests.get(github_user_url)
            github_user_data = github_response.json()
            decoded_token = github_user_data
            
        email = decoded_token['email']
        logger.info(email)

        # Check if this email exists in our Firestore database
        user_ref = db.collection('users').document(email)
        user_doc = user_ref.get()
        if user_doc.exists:
            user_data = user_doc.to_dict()
            # Set up session
            session['email'] = email
            session['username'] = user_data.get('Username')
            session['user_id'] = user_data.get('UserID')
            return jsonify({"email": email, "username": user_data.get('Username'), "userID": user_data.get('UserID')}), 200
        else:
            return jsonify({"error": "User not registered"}), 404
    except Exception as e:
        logging.info(str(e))
        return jsonify({"error": str(e)}), 401




if __name__ == '__main__':
    app.run(debug=True, port=8098)

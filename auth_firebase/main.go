package main

import (
	"bytes"
	"context"
	"encoding/gob"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
	"os"
	"strconv"

	"cloud.google.com/go/firestore"
	firebase "firebase.google.com/go/v4"
	"github.com/gorilla/sessions"
	"github.com/pquerna/otp/totp"
	"golang.org/x/oauth2"
	"golang.org/x/oauth2/google"
	"google.golang.org/api/option"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

type UserInfo struct {
	Name  string `json:"name"`
	Email string `json:"email"`
}

var (
	googleOauthConfig *oauth2.Config
	firebaseApp       *firebase.App
	store             = sessions.NewCookieStore([]byte("giubb234567s@3ctgvuh"))
	firestoreClient   *firestore.Client
)

func main() {
	ctx := context.Background()

	// Firebase setup
	conf := &firebase.Config{ProjectID: os.Getenv("FIREBASE_PROJECT_ID")}
	var err error
	firebaseApp, err = firebase.NewApp(ctx, conf, option.WithCredentialsFile("auth.json"))
	if err != nil {
		log.Fatalf("error initializing firebase app: %v\n", err)
	}

	firestoreClient, err = firebaseApp.Firestore(ctx)
	if err != nil {
		log.Fatalf("error initializing firestore client: %v", err)
	}
	defer firestoreClient.Close()

	// Google OAuth Config
	googleOauthConfig = &oauth2.Config{
		RedirectURL:  "http://localhost:8092/callback",
		ClientID:     "233586076258-gp55jfbjd5803fbs5nlpbparkse5j428.apps.googleusercontent.com",
		ClientSecret: "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",
		Scopes:       []string{"https://www.googleapis.com/auth/userinfo.email", "https://www.googleapis.com/auth/userinfo.profile"},
		Endpoint:     google.Endpoint,
	}
	userData := map[string]interface{}{
		"username":        "johndoe",
		"email":           "johndoe@example.com",
		"password":        "hashed_password_here", // Ensure this is securely hashed
		"private":         false,
		"personalDetails": "Some personal details here",
		// add any other fields as needed
	}

	// Add user to Firestore
	err = addUserToFirestore("johndoe@example.com", userData)
	if err != nil {
		log.Printf("Error adding user to Firestore: %v", err)
		// handle error
	}
	mainRouter := http.NewServeMux()
	mainRouter.HandleFunc("/", handleHome)
	mainRouter.HandleFunc("/login", handleLogin)
	mainRouter.HandleFunc("/callback", handleGoogleCallback)
	mainRouter.HandleFunc("/login/google", handleGoogleLogin)
	mainRouter.HandleFunc("/api/users", handleSignup)
	mainRouter.HandleFunc("/signup", handleSignup)
	// Add other routes as needed

	log.Println("Starting server on :8080...")
	log.Fatal(http.ListenAndServe(":8080", sessionResetMiddleware(mainRouter)))
}

func addUserToFirestore(email string, userData map[string]interface{}) error {
	ctx := context.Background()

	// Get a reference to the Firestore client from a global variable or initialization function
	_, err := firestoreClient.Collection("users").Doc(email).Set(ctx, userData)
	if err != nil {
		return fmt.Errorf("failed to add user to Firestore: %v", err)
	}

	return nil
}

func createFirestoreClient(ctx context.Context) (*firestore.Client, error) {
	// Assuming you've set your Google application credentials in the environment
	// variable GOOGLE_APPLICATION_CREDENTIALS
	projectID := "auth-49d42" // Replace with your Firebase project ID

	client, err := firestore.NewClient(ctx, projectID)
	if err != nil {
		return nil, fmt.Errorf("firestore.NewClient: %v", err)
	}
	return client, nil
}

func handleGoogleLogin(w http.ResponseWriter, r *http.Request) {
	log.Println("Google login requested")
	url := googleOauthConfig.AuthCodeURL("state-token", oauth2.AccessTypeOffline)
	http.Redirect(w, r, url, http.StatusTemporaryRedirect)
}

func getUserInfo(accessToken string) (UserInfo, error) {
	resp, err := http.Get("https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + accessToken)
	if err != nil {
		return UserInfo{}, fmt.Errorf("error making request to Google API: %v", err)
	}
	defer resp.Body.Close()

	data, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return UserInfo{}, fmt.Errorf("error reading response body: %v", err)
	}

	if resp.StatusCode != http.StatusOK {
		return UserInfo{}, fmt.Errorf("received non-ok response from Google API: %s", data)
	}

	var userInfo UserInfo
	if err := json.Unmarshal(data, &userInfo); err != nil {
		return UserInfo{}, fmt.Errorf("error unmarshaling user info JSON: %v", err)
	}

	return userInfo, nil
}

func GenerateQRCodeURL(secret, email string) string {
	return fmt.Sprintf("otpauth://totp/YourAppName:%s?secret=%s&issuer=YourAppName", email, secret)
}

func handleHome(w http.ResponseWriter, r *http.Request) {
	session, err := store.Get(r, "session-name")
	if err != nil || session.Values["user"] == nil {
		log.Println("Print-3")
		log.Println(session.Values)
		http.Redirect(w, r, "/login", http.StatusFound)
		return
	}
	log.Println("Print-4")
	log.Println(session.Values)
	// User is logged in, display home page
	fmt.Fprintf(w, "Welcome, %v!", session.Values["user"])
	// Generate the QR code URL
	// userEmailInterface, ok := session.Values["email"]
	// if !ok {
	// 	// Handle the error if the email is not present in the session
	// 	http.Error(w, "User email not found in session", http.StatusBadRequest)
	// 	return
	// }

	// userEmail, ok := userEmailInterface.(string)
	// if !ok {
	// 	// Handle the error if the email is not a string
	// 	http.Error(w, "User email is not a valid string", http.StatusBadRequest)
	// 	return
	// }

	// userSecretInterface, ok1 := session.Values["secret"]
	// if !ok1 {

	// }
	// userSecret, ok2 := userSecretInterface.(string)
	// if !ok2 {

	// }
	// qrCodeURL := GenerateQRCodeURL(userSecret, userEmail)

	// // Write the QR code URL to the response
	// fmt.Fprintf(w, "QR Code URL: %s", qrCodeURL)
}

func handleLogin(w http.ResponseWriter, r *http.Request) {
	html := `<html><body><a href="/login/google">Login with Google</a></body></html>`
	w.Header().Set("Content-Type", "text/html; charset=utf-8")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(html))
}

func handleGoogleCallback(w http.ResponseWriter, r *http.Request) {
	ctx := context.Background()

	// Exchange the code for a token
	code := r.URL.Query().Get("code")
	token, err := googleOauthConfig.Exchange(ctx, code)
	if err != nil {
		http.Error(w, "Failed to exchange token: "+err.Error(), http.StatusInternalServerError)
		return
	}

	userInfo, err := getUserInfo(token.AccessToken)
	if err != nil {
		http.Error(w, "Failed to get user info: "+err.Error(), http.StatusInternalServerError)
		return
	}

	userEmail := userInfo.Email

	// Use userEmail to query Firestore
	docSnapshot, err := firestoreClient.Collection("users").Doc(userEmail).Get(ctx)
	if err != nil && status.Code(err) != codes.NotFound {
		// Handle non-NotFound errors
		http.Error(w, "Error checking user in Firestore: "+err.Error(), http.StatusInternalServerError)
		return
	}

	if !docSnapshot.Exists() {
		// User does not exist, redirect to signup or handle new user creation
		http.Redirect(w, r, "/signup", http.StatusFound)
	} else {
		// User exists, proceed to log the user in
		// ... initiate a session or other login procedures ...
		totpSecret, err := docSnapshot.DataAt("TOTPSecret")

		session, _ := store.Get(r, "session-name")
		gob.Register(UserInfo{})
		session.Values["user"] = userInfo
		session.Values["email"] = userEmail
		session.Values["secret"] = totpSecret
		log.Println("Print-1")
		log.Println(session.Values)
		err = session.Save(r, w)
		if err != nil {
			log.Printf("Error saving session: %v", err)
			// Handle the error, possibly by sending a server error response
		}

		log.Println("Print-2")
		log.Println(session.Values)
		http.Redirect(w, r, "/", http.StatusFound)
		// Redirect to home page or user dashboard

		// For now, let's display the user info as a placeholder
		//fmt.Fprintf(w, "User Info: %s\n", userInfo)
	}

}

func sessionResetMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		session, err := store.Get(r, "session-name")
		if err == nil && session.Values["user"] != nil {
			session.Options.MaxAge = 120 // Reset MaxAge
			session.Save(r, w)
		}
		next.ServeHTTP(w, r)
	})
}
func GenerateTOTPSecret(email string) (string, error) {
	key, err := totp.Generate(totp.GenerateOpts{
		Issuer:      "YourAppName", // Replace with your app's name
		AccountName: email,         // Use the user's email
	})
	if err != nil {
		return "", err
	}
	return key.Secret(), nil
}

func handleSignup(w http.ResponseWriter, r *http.Request) {
	if r.Method == http.MethodPost {
		ctx := context.Background()
		err := r.ParseForm()
		if err != nil {
			http.Error(w, "Error parsing form", http.StatusBadRequest)
			return
		}

		// Convert the 'private' form value to a boolean
		privateValue, err := strconv.ParseBool(r.FormValue("private"))
		if err != nil {
			// Handle the error if the value is not a valid boolean
			http.Error(w, "Invalid input for private field", http.StatusBadRequest)
			return
		}

		// Create user data struct with the correct type for the Private field
		userData := struct {
			Username        string `json:"username"`
			Email           string `json:"email"`
			Password        string `json:"password"`
			Private         bool   `json:"private"`
			PersonalDetails string `json:"personalDetails"`
		}{
			Username:        r.FormValue("username"),
			Email:           r.FormValue("email"),
			Password:        r.FormValue("password"), // Hash this password before storing
			Private:         privateValue,
			PersonalDetails: r.FormValue("personalDetails"),
		}
		userData2 := struct {
			Username        string `json:"username"`
			Email           string `json:"email"`
			Password        string `json:"password"` // Hash this password before storing
			Private         bool   `json:"private"`
			PersonalDetails string `json:"personalDetails"`
			TOTPSecret      string `json:"totpSecret"` // Field for the TOTP secret
		}{
			Username:        r.FormValue("username"),
			Email:           r.FormValue("email"),
			Password:        r.FormValue("password"), // Hash this password before storing
			Private:         privateValue,
			PersonalDetails: r.FormValue("personalDetails"),
			// TOTPSecret should be generated separately and not directly from the form
		}
		userTOTPSecret, err := GenerateTOTPSecret(r.FormValue("email"))
		if err != nil {
			http.Error(w, "Error generating secret: "+err.Error(), http.StatusInternalServerError)
			return
		}
		userData2.TOTPSecret = userTOTPSecret
		// Convert struct to JSON
		jsonData, err := json.Marshal(userData)
		if err != nil {
			http.Error(w, "Error creating JSON data: "+err.Error(), http.StatusInternalServerError)
			return
		}

		// Send POST request to your user creation service
		resp, err := http.Post("http://user-app-1:8088/users", "application/json", bytes.NewBuffer(jsonData))
		if err != nil {
			http.Error(w, "Error sending request to user service: "+err.Error(), http.StatusInternalServerError)
			return
		}
		defer resp.Body.Close()

		// Read the response body from your service
		responseBody, err := ioutil.ReadAll(resp.Body)
		if err != nil {
			http.Error(w, "Error reading response body: "+err.Error(), http.StatusInternalServerError)
			return
		}

		// Store the same data in Firestore
		_, err = firestoreClient.Collection("users").Doc(userData2.Email).Set(ctx, userData2)
		if err != nil {
			http.Error(w, "Error storing user data in Firestore: "+err.Error(), http.StatusInternalServerError)
			return
		}

		// Forward the response from your service to the client
		w.Header().Set("Content-Type", "application/json")
		w.Write(responseBody)
	} else if r.Method == http.MethodGet {
		// Serve a simple signup page
		html := `
    <html>
        <body>
            <form action="/api/users" method="post">
                <input type="text" name="username" placeholder="Username" required><br>
                <input type="email" name="email" placeholder="Email" required><br>
                <input type="password" name="password" placeholder="Password" required><br>
                <input type="text" name="private" placeholder="Private (true/false)" required><br>
                <input type="text" name="personalDetails" placeholder="Personal Details" required><br>
                <button type="submit">Sign Up</button>
            </form>
        </body>
    </html>`
		w.Header().Set("Content-Type", "text/html; charset=utf-8")
		w.WriteHeader(http.StatusOK)
		w.Write([]byte(html))
	} else {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
	}
}

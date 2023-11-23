package main

import (
	"context"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
	"os"

	firebase "firebase.google.com/go/v4"
	"golang.org/x/oauth2"
	"golang.org/x/oauth2/google"
	"google.golang.org/api/option"
)

var (
	googleOauthConfig *oauth2.Config
	firebaseApp       *firebase.App
)

func main() {
	ctx := context.Background()

	// Firebase setup
	conf := &firebase.Config{ProjectID: os.Getenv("FIREBASE_PROJECT_ID")}
	var err error
	firebaseApp, err = firebase.NewApp(ctx, conf, option.WithCredentialsFile("auth-49d42-firebase-adminsdk-18zi0-1c70500359.json"))
	if err != nil {
		log.Fatalf("error initializing firebase app: %v\n", err)
	}

	// Google OAuth Config
	googleOauthConfig = &oauth2.Config{
		RedirectURL:  "http://localhost:8092/callback",
		ClientID:     "233586076258-agm1v31r0ibj3scg48ac8iu4d9i2pu6l.apps.googleusercontent.com",
		ClientSecret: "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",
		Scopes:       []string{"https://www.googleapis.com/auth/userinfo.email", "https://www.googleapis.com/auth/userinfo.profile"},
		Endpoint:     google.Endpoint,
	}

	// HTTP Handlers
	http.HandleFunc("/", handleHome)
	http.HandleFunc("/login", handleGoogleLogin)
	http.HandleFunc("/callback", handleGoogleCallback)

	// Start the server
	log.Println("Starting server on :8080...")
	log.Fatal(http.ListenAndServe(":8080", nil))
}

func handleHome(w http.ResponseWriter, r *http.Request) {
	html := `<html><body><a href="/login">Login with Google</a></body></html>`
	w.Header().Set("Content-Type", "text/html; charset=utf-8")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(html))
}

func handleGoogleLogin(w http.ResponseWriter, r *http.Request) {
	url := googleOauthConfig.AuthCodeURL("state-token", oauth2.AccessTypeOffline)
	http.Redirect(w, r, url, http.StatusTemporaryRedirect)
}

func handleGoogleCallback(w http.ResponseWriter, r *http.Request) {
	code := r.URL.Query().Get("code")
	token, err := googleOauthConfig.Exchange(context.Background(), code)
	if err != nil {
		http.Error(w, "Failed to exchange token: "+err.Error(), http.StatusInternalServerError)
		return
	}

	// Example: Use the token to get user info from Google
	userInfo, err := getUserInfo(token.AccessToken)
	if err != nil {
		http.Error(w, "Failed to get user info: "+err.Error(), http.StatusInternalServerError)
		return
	}

	fmt.Fprintf(w, "User Info: %s\n", userInfo)
}

func getUserInfo(accessToken string) (string, error) {
	resp, err := http.Get("https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + accessToken)
	if err != nil {
		return "", fmt.Errorf("error making request to Google API: %v", err)
	}
	defer resp.Body.Close()

	data, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return "", fmt.Errorf("error reading response body: %v", err)
	}

	if resp.StatusCode != http.StatusOK {
		return "", fmt.Errorf("received non-ok response from Google API: %s", data)
	}

	var userInfo struct {
		Name  string `json:"name"`
		Email string `json:"email"`
	}

	if err := json.Unmarshal(data, &userInfo); err != nil {
		return "", fmt.Errorf("error unmarshaling user info JSON: %v", err)
	}

	userInfoStr := fmt.Sprintf("Name: %s, Email: %s", userInfo.Name, userInfo.Email)
	return userInfoStr, nil
}

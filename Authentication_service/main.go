package main

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"log"
	"net/http"

	_ "github.com/lib/pq"
	"golang.org/x/crypto/bcrypt"
	"golang.org/x/oauth2"
)

// Database connection constants
const (
	host     = "localhost"
	port     = 5432
	user     = "expressnest_auth"
	password = "7410"
	dbname   = "auth"
)

// OAuth2 configuration
var oauth2Config = &oauth2.Config{
	ClientID:     "Expressnest",
	ClientSecret: "F4CcBZRR0Ksdy4LOnsrBnItRNfLrR01e",
	RedirectURL:  "http://localhost:3000/callback",
	Scopes:       []string{"openid", "profile", "email"},
	Endpoint: oauth2.Endpoint{
		AuthURL:  "http://localhost:8080/auth/realms/ExpressNest/protocol/openid-connect/auth",
		TokenURL: "http://localhost:8080/auth/realms/ExpressNest/protocol/openid-connect/token",
	},
}

type User struct {
	Username string `json:"username"`
	Password string `json:"password"`
	Email    string `json:"email"`
}

func main() {
	// Set up database connection
	psqlInfo := fmt.Sprintf("host=%s port=%d user=%s password=%s dbname=%s sslmode=disable", host, port, user, password, dbname)
	db, err := sql.Open("postgres", psqlInfo)
	if err != nil {
		log.Fatal(err)
	}
	defer db.Close()

	// Test database connection
	err = db.Ping()
	if err != nil {
		log.Fatal(err)
	}

	// Set up HTTP server
	http.HandleFunc("/", handleMain)
	http.HandleFunc("/callback", handleCallback)
	http.HandleFunc("/register", func(w http.ResponseWriter, r *http.Request) {
		handleRegister(w, r, db)
	})
	http.HandleFunc("/login", func(w http.ResponseWriter, r *http.Request) {
		handleLogin(w, r, db)
	})

	fmt.Println("Server is running at http://localhost:3000/")
	log.Fatal(http.ListenAndServe(":3000", nil))
}

func handleMain(w http.ResponseWriter, r *http.Request) {
	url := oauth2Config.AuthCodeURL("state")
	http.Redirect(w, r, url, http.StatusFound)
}

func handleCallback(w http.ResponseWriter, r *http.Request) {
	ctx := context.Background()
	code := r.URL.Query().Get("code")
	if code == "" {
		http.Error(w, "Code not found", http.StatusBadRequest)
		return
	}

	token, err := oauth2Config.Exchange(ctx, code)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	userInfo, err := getUserInfo(token)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	fmt.Fprintf(w, "User Info: %+v", userInfo)
}

func getUserInfo(token *oauth2.Token) (map[string]interface{}, error) {
	client := oauth2Config.Client(context.Background(), token)
	resp, err := client.Get("http://localhost:8080/auth/realms/Nest/protocol/openid-connect/userinfo")
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	var userInfo map[string]interface{}
	err = json.NewDecoder(resp.Body).Decode(&userInfo)
	if err != nil {
		return nil, err
	}

	return userInfo, nil
}

func handleRegister(w http.ResponseWriter, r *http.Request, db *sql.DB) {
	if r.Method != "POST" {
		http.Error(w, "Only POST method is allowed", http.StatusMethodNotAllowed)
		return
	}

	var user User
	err := json.NewDecoder(r.Body).Decode(&user)
	if err != nil {
		http.Error(w, "Error decoding request body", http.StatusBadRequest)
		return
	}

	hashedPassword, err := bcrypt.GenerateFromPassword([]byte(user.Password), bcrypt.DefaultCost)
	if err != nil {
		http.Error(w, "Error hashing password", http.StatusInternalServerError)
		return
	}

	_, err = db.Exec("INSERT INTO users (username, password_hash, email) VALUES ($1, $2, $3)", user.Username, hashedPassword, user.Email)
	if err != nil {
		http.Error(w, "Error saving user", http.StatusInternalServerError)
		return
	}

	fmt.Fprintf(w, "User registered successfully")
}

func handleLogin(w http.ResponseWriter, r *http.Request, db *sql.DB) {
	if r.Method != "POST" {
		http.Error(w, "Only POST method is allowed", http.StatusMethodNotAllowed)
		return
	}

	var user User
	err := json.NewDecoder(r.Body).Decode(&user)
	if err != nil {
		http.Error(w, "Error decoding request body", http.StatusBadRequest)
		return
	}

	var storedHashedPassword string
	err = db.QueryRow("SELECT password_hash FROM users WHERE username = $1", user.Username).Scan(&storedHashedPassword)
	if err != nil {
		if err == sql.ErrNoRows {
			http.Error(w, "User not found", http.StatusUnauthorized)
		} else {
			http.Error(w, "Error querying user", http.StatusInternalServerError)
		}
		return
	}

	err = bcrypt.CompareHashAndPassword([]byte(storedHashedPassword), []byte(user.Password))
	if err != nil {
		http.Error(w, "Invalid password", http.StatusUnauthorized)
		return
	}

	fmt.Fprintf(w, "User authenticated successfully")
}

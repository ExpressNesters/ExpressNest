package main

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"strconv"
	"strings"
)
func enableCors(w *http.ResponseWriter) {
   (*w).Header().Set("Access-Control-Allow-Origin", "*")
   (*w).Header().Set("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE")
   (*w).Header().Set("Access-Control-Allow-Headers", "Accept, Content-Type, Content-Length, Accept-Encoding, X-CSRF-Token, Authorization")
}


func main() {
	fmt.Println("Follow Service Starting...")

	// Initialize MongoDB
	InitMongoDB()
	InitKafkaProducer()

	// Setup routes
	http.HandleFunc("/follow", handleFollow)
	http.HandleFunc("/unfollow", handleUnfollow)
	http.HandleFunc("/followers/", handleGetFollowers) // Note the trailing slash

	// Start the server
	fmt.Println("Listening on port 8089...")
	log.Fatal(http.ListenAndServe(":8089", nil))
}

func handleFollow(w http.ResponseWriter, r *http.Request) {
enableCors(&w)
	if r.Method != "POST" {
		http.Error(w, "Only POST method is allowed", http.StatusMethodNotAllowed)
		return
	}

	var followReq FollowRequest
	if err := json.NewDecoder(r.Body).Decode(&followReq); err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	if err := FollowUser(followReq); err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	// Send event to Kafka
	if err := SendFollowEvent(followReq, "FOLLOW"); err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(followReq)
}

func handleUnfollow(w http.ResponseWriter, r *http.Request) {
enableCors(&w)
	if r.Method != "POST" {
		http.Error(w, "Only POST method is allowed", http.StatusMethodNotAllowed)
		return
	}

	var followReq FollowRequest
	if err := json.NewDecoder(r.Body).Decode(&followReq); err != nil {
		http.Error(w, "Bad request: "+err.Error(), http.StatusBadRequest)
		return
	}

	// Remove follow relationship from the database
	if err := UnfollowUser(followReq); err != nil {
		http.Error(w, "Failed to unfollow: "+err.Error(), http.StatusInternalServerError)
		return
	}
	// Send event to Kafka
	if err := SendFollowEvent(followReq, "UNFOLLOW"); err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(followReq)
}

func handleGetFollowers(w http.ResponseWriter, r *http.Request) {
enableCors(&w)
	if r.Method != "GET" {
		http.Error(w, "Only GET method is allowed", http.StatusMethodNotAllowed)
		return
	}

	// Extracting the user ID from the URL path
	pathParts := strings.Split(r.URL.Path, "/")
	if len(pathParts) < 3 {
		http.Error(w, "User ID is required", http.StatusBadRequest)
		return
	}
	userIDStr := pathParts[2]

	// Convert userID string to int
	userID, err := strconv.Atoi(userIDStr)
	if err != nil {
		http.Error(w, "Invalid User ID", http.StatusBadRequest)
		return
	}

	followersJSON, err := GetFollowers(userID)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(followersJSON))
}

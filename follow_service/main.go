package main

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"strconv"

	"github.com/gorilla/mux"
)

func main() {
	fmt.Println("Follow Service Starting...")

	// Initialize MongoDB
	InitMongoDB()
	InitKafkaProducer()

	router := mux.NewRouter()

	// Setup routes with CORS handling for each route
	router.HandleFunc("/follow", withCors(handleFollow)).Methods("POST", "OPTIONS")
	router.HandleFunc("/unfollow", withCors(handleUnfollow)).Methods("POST", "OPTIONS")
	router.HandleFunc("/followers/{id:[0-9]+}", withCors(handleGetFollowers)).Methods("GET", "OPTIONS")
	router.HandleFunc("/followees/{id:[0-9]+}", withCors(handleGetFollowees)).Methods("GET", "OPTIONS")

	fmt.Println("Listening on port 8089...")
	log.Fatal(http.ListenAndServe(":8089", router))
}

// CORS middleware specifically for POST requests
func withCors(h http.HandlerFunc) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		setCorsHeaders(w)

		// Pre-flight request handling for POST
		if r.Method == "OPTIONS" {
			w.WriteHeader(http.StatusOK)
			return
		}

		h(w, r)
	}
}

// Set CORS headers
func setCorsHeaders(w http.ResponseWriter) {
	w.Header().Set("Access-Control-Allow-Origin", "*")
	w.Header().Set("Access-Control-Allow-Methods", "POST, GET, OPTIONS")
	w.Header().Set("Access-Control-Allow-Headers", "Content-Type")
}

// Handle the "/followees/{id}" route
func handleGetFollowees(w http.ResponseWriter, r *http.Request) {
	fmt.Println("Get request to get all followees")
	vars := mux.Vars(r)
	userID, err := strconv.Atoi(vars["id"])
	if err != nil {
		http.Error(w, "Invalid User ID", http.StatusBadRequest)
		return
	}

	followeesJSON, err := GetFollowees(userID)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(followeesJSON))
}

func handleFollow(w http.ResponseWriter, r *http.Request) {
	fmt.Println("Get request to do follow")
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
	fmt.Println("Get request to handle unfollow")
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
	fmt.Println("Get request to get all followers")
	vars := mux.Vars(r)
	userID, err := strconv.Atoi(vars["id"])
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

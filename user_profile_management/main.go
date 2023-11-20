package main

import (
    "log"
    "net/http"
    "user-profile-service/data"
    "user-profile-service/handlers"

    "github.com/gorilla/mux"
)

func main() {
    data.InitCassandra()

    r := mux.NewRouter()
    r.HandleFunc("/user", handlers.CreateUser).Methods("POST")
    r.HandleFunc("/user/{id}", handlers.GetUser).Methods("GET")
    r.HandleFunc("/user/{id}", handlers.UpdateUser).Methods("PUT")
    r.HandleFunc("/user/{id}", handlers.DeleteUser).Methods("DELETE")
    r.HandleFunc("/user/{id}/privacy", handlers.UpdatePrivacy).Methods("PUT")
    r.HandleFunc("/user/{id}/notifications", handlers.GetNotifications).Methods("GET")

    log.Println("Server is starting on port 8080...")
    log.Fatal(http.ListenAndServe(":8080", r))
}


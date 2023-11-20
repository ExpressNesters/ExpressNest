// create_user.go
package handlers

import (
    "encoding/json"
    "net/http"
    "user-profile-service/data"
    "user-profile-service/models"
    "github.com/gocql/gocql"
)

func CreateUser(w http.ResponseWriter, r *http.Request) {
    var newUser models.UserProfile
    if err := json.NewDecoder(r.Body).Decode(&newUser); err != nil {
        http.Error(w, err.Error(), http.StatusBadRequest)
        return
    }

    newUser.ID = gocql.TimeUUID() // Generating a new UUID for the user

    if err := data.Session.Query(`INSERT INTO users (id, name, email) VALUES (?, ?, ?)`, 
        newUser.ID, newUser.Name, newUser.Email).Exec(); err != nil {
        http.Error(w, err.Error(), http.StatusInternalServerError)
        return
    }

    w.WriteHeader(http.StatusCreated)
    json.NewEncoder(w).Encode(newUser)
}


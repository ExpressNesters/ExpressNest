// get_user.go
package handlers

import (
    "encoding/json"
    "net/http"
    "user-profile-service/data"
    "user-profile-service/models"
    "github.com/gorilla/mux" // Assuming you're using Gorilla Mux for routing
)

func GetUser(w http.ResponseWriter, r *http.Request) {
    vars := mux.Vars(r)
    userID := vars["id"]

    var user models.UserProfile
    if err := data.Session.Query(`SELECT id, name, email FROM users WHERE id = ?`, userID).Consistency(gocql.One).Scan(&user.ID, &user.Name, &user.Email); err != nil {
        http.Error(w, err.Error(), http.StatusNotFound)
        return
    }

    json.NewEncoder(w).Encode(user)
}


// update_user.go
package handlers

import (
    "encoding/json"
    "net/http"
    "user-profile-service/data"
    "user-profile-service/models"
    "github.com/gorilla/mux"
)

func UpdateUser(w http.ResponseWriter, r *http.Request) {
    vars := mux.Vars(r)
    userID := vars["id"]

    var updatedUser models.UserProfile
    if err := json.NewDecoder(r.Body).Decode(&updatedUser); err != nil {
        http.Error(w, err.Error(), http.StatusBadRequest)
        return
    }

    if err := data.Session.Query(`UPDATE users SET name = ?, email = ? WHERE id = ?`, 
        updatedUser.Name, updatedUser.Email, userID).Exec(); err != nil {
        http.Error(w, err.Error(), http.StatusInternalServerError)
        return
    }

    w.WriteHeader(http.StatusOK)
    json.NewEncoder(w).Encode(updatedUser)
}


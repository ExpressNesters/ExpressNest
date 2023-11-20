package handlers

import (
    "encoding/json"
    "net/http"
    "user-profile-service/data"
    "github.com/gorilla/mux"
)

func UpdatePrivacy(w http.ResponseWriter, r *http.Request) {
    vars := mux.Vars(r)
    userID := vars["id"]

    var update struct {
        Private bool `json:"private"`
    }
    if err := json.NewDecoder(r.Body).Decode(&update); err != nil {
        http.Error(w, err.Error(), http.StatusBadRequest)
        return
    }

    if err := data.Session.Query(`UPDATE users SET private = ? WHERE id = ?`, 
        update.Private, userID).Exec(); err != nil {
        http.Error(w, err.Error(), http.StatusInternalServerError)
        return
    }

    w.WriteHeader(http.StatusOK)
}


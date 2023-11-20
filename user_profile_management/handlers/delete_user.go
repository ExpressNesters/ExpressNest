// delete_user.go
package handlers

import (
    "net/http"
    "user-profile-service/data"
    "github.com/gorilla/mux"
)

func DeleteUser(w http.ResponseWriter, r *http.Request) {
    vars := mux.Vars(r)
    userID := vars["id"]

    if err := data.Session.Query(`DELETE FROM users WHERE id = ?`, userID).Exec(); err != nil {
        http.Error(w, err.Error(), http.StatusInternalServerError)
        return
    }

    w.WriteHeader(http.StatusNoContent)
}


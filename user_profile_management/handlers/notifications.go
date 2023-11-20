package handlers

import (
    "encoding/json"
    "net/http"
    "user-profile-service/data"
    "user-profile-service/models"
    "github.com/gorilla/mux"
)

func GetNotifications(w http.ResponseWriter, r *http.Request) {
    vars := mux.Vars(r)
    userID := vars["id"]

    var notifications []models.Notification
    iter := data.Session.Query(`SELECT id, user_id, content, timestamp FROM notifications WHERE user_id = ?`, userID).Iter()
    var n models.Notification
    for iter.Scan(&n.ID, &n.UserID, &n.Content, &n.Timestamp) {
        notifications = append(notifications, n)
    }
    if err := iter.Close(); err != nil {
        http.Error(w, err.Error(), http.StatusInternalServerError)
        return
    }

    json.NewEncoder(w).Encode(notifications)
}


package models

import (
    "github.com/gocql/gocql"
    "time"
)

type Notification struct {
    ID        gocql.UUID `json:"id"`
    UserID    gocql.UUID `json:"user_id"`
    Content   string     `json:"content"`
    Timestamp time.Time  `json:"timestamp"`
}


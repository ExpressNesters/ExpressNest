package models

import "github.com/gocql/gocql"

type UserProfile struct {
    ID       gocql.UUID `json:"id"`
    Name     string     `json:"name"`
    Email    string     `json:"email"`
    Private  bool       `json:"private"`
}


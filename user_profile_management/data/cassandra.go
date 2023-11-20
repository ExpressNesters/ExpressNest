package data

import (
    "log"

    "github.com/gocql/gocql"
)

var Session *gocql.Session

func InitCassandra() {
    var err error
    cluster := gocql.NewCluster("cassandra") // This should match the service name in docker-compose.yml
    cluster.Keyspace = "userprofile"
    cluster.Consistency = gocql.Quorum
    Session, err = cluster.CreateSession()
    if err != nil {
        log.Fatalf("Could not connect to Cassandra: %v", err)
    }

    // Inserting dummy data
    dummyData := []map[string]interface{}{
        {"id": gocql.TimeUUID(), "name": "John Doe", "email": "john@example.com"},
        {"id": gocql.TimeUUID(), "name": "Jane Doe", "email": "jane@example.com"},
    }

    for _, user := range dummyData {
        if err := Session.Query(`INSERT INTO users (id, name, email) VALUES (?, ?, ?)`,
            user["id"], user["name"], user["email"]).Exec(); err != nil {
            log.Printf("Failed to insert dummy data: %v", err)
        }
    }
}


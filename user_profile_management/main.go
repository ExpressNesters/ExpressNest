package main

import (
	"database/sql"
	"fmt"
	"log"
	"net/http"
	"os"
	"userprofileservice/controllers"
	"userprofileservice/handlers"

	"github.com/gorilla/mux"
	_ "github.com/lib/pq"
)

// InitDB initializes the database and creates tables if they don't exist
func InitDB(db *sql.DB) {
	createUsersTable := `
    CREATE TABLE IF NOT EXISTS users (
        id SERIAL PRIMARY KEY,
        username VARCHAR(50) UNIQUE NOT NULL,
        email VARCHAR(100) UNIQUE NOT NULL,
        hashed_password VARCHAR(255) NOT NULL,
        personal_details TEXT,
        private BOOLEAN DEFAULT false,
        created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
    );`

	_, err := db.Exec(createUsersTable)
	if err != nil {
		log.Fatalf("Error creating users table: %v", err)
	}
	// Create an index on the username column of the users table
	createIndex := `CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);`
	_, err = db.Exec(createIndex)
	if err != nil {
		log.Fatalf("Error creating index on users table: %v", err)
	}

}

func main() {
	// Initialize the database connection
	db, err := sql.Open("postgres", fmt.Sprintf("host=%s port=%s user=%s "+
		"password=%s dbname=%s sslmode=disable",
		os.Getenv("DB_HOST"), os.Getenv("DB_PORT"),
		os.Getenv("DB_USER"), os.Getenv("DB_PASSWORD"),
		os.Getenv("DB_NAME")))
	if err != nil {
		log.Fatal("Error connecting to the database: ", err)
	}
	defer db.Close()
	InitDB(db)
	// Initialize UserController with the database connection
	userController := controllers.NewUserController(db)

	// Initialize UserHandler with UserController
	userHandler := handlers.NewUserHandler(userController)

	// Create a new router
	router := mux.NewRouter()

	// Define routes
	router.HandleFunc("/users", userHandler.CreateUserHandler).Methods("POST")
	router.HandleFunc("/users/{id}", userHandler.GetUserHandler).Methods("GET")
	router.HandleFunc("/users/{id}", userHandler.UpdateUserHandler).Methods("PUT")
	router.HandleFunc("/users/{id}", userHandler.DeleteUserHandler).Methods("DELETE")
	router.HandleFunc("/users", userHandler.GetAllUsersHandler).Methods("GET")
	// Start the HTTP server
	log.Println("Starting server on :8088")
	log.Fatal(http.ListenAndServe(":8088", router))
}

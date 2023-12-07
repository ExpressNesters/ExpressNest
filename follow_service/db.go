package main

import (
	"context"
	"encoding/json"
	"fmt"
	"log"
	"os"

	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
)

var client *mongo.Client

// Define a struct to hold the response data
type FollowersResponse struct {
	UserID            int   `json:"user_id"`
	NumberOfFollowers int   `json:"number_of_followers"`
	Followers         []int `json:"followers"`
}

// Define a struct to hold the response data
type FolloweesResponse struct {
	UserID            int   `json:"user_id"`
	NumberOfFollowers int   `json:"number_of_followers"`
	Followees         []int `json:"followees"`
}

// InitMongoDB initializes the MongoDB client
func InitMongoDB() {
	var err error
	clientOptions := options.Client().ApplyURI(os.Getenv("MONGO_URI"))
	client, err = mongo.Connect(context.TODO(), clientOptions)
	if err != nil {
		log.Fatal(err)
	}

	// Check the connection
	err = client.Ping(context.TODO(), nil)
	if err != nil {
		log.Fatal(err)
	}

	fmt.Println("Connected to MongoDB!")
}

// FollowUser handles the database operation for following a user
func FollowUser(followReq FollowRequest) error {
	collection := client.Database("followService").Collection("follows")

	// Check if the follow relationship already exists
	filter := bson.M{
		"follower_id": followReq.FollowerID,
		"followee_id": followReq.FolloweeID,
	}
	exists, err := collection.CountDocuments(context.TODO(), filter)
	if err != nil {
		return err
	}
	if exists > 0 {
		return nil // Relationship already exists, no need to insert
	}

	// Create a document to insert
	doc := bson.M{
		"follower_id": followReq.FollowerID,
		"followee_id": followReq.FolloweeID,
	}

	_, err = collection.InsertOne(context.TODO(), doc)
	return err
}

// UnfollowUser handles the database operation for unfollowing a user
func UnfollowUser(followReq FollowRequest) error {
	collection := client.Database("followService").Collection("follows")

	// Define the filter for the document to remove
	filter := bson.M{
		"follower_id": followReq.FollowerID,
		"followee_id": followReq.FolloweeID,
	}

	// Check if the follow relationship exists
	exists, err := collection.CountDocuments(context.TODO(), filter)
	if err != nil {
		return err
	}
	if exists == 0 {
		return nil // Relationship does not exist, no need to delete
	}

	_, err = collection.DeleteOne(context.TODO(), filter)
	return err
}

// GetFollowers retrieves the list of follower IDs for a given user ID
func GetFollowers(userID int) (string, error) {
	collection := client.Database("followService").Collection("follows")

	filter := bson.M{"followee_id": userID}
	cursor, err := collection.Find(context.TODO(), filter)
	if err != nil {
		log.Printf("Error finding followers: %v", err)
		return "", err
	}
	defer cursor.Close(context.TODO())

	var results []bson.M
	if err = cursor.All(context.TODO(), &results); err != nil {
		log.Printf("Error getting all results: %v", err)
		return "", err
	}

	var followers []int
	for _, result := range results {
		if followerID, ok := result["follower_id"].(int32); ok {
			followers = append(followers, int(followerID))
		} else {
			log.Printf("Failed to convert follower_id to int32 for result: %v", result)
		}
	}

	// Create the response struct
	response := FollowersResponse{
		UserID:            userID,
		NumberOfFollowers: len(followers),
		Followers:         followers,
	}

	// Convert the response struct to JSON
	responseJSON, err := json.Marshal(response)
	if err != nil {
		log.Printf("Error marshalling response to JSON: %v", err)
		return "", err
	}

	return string(responseJSON), nil
}

// New Function to Get Followees
func GetFollowees(userID int) (string, error) {
	collection := client.Database("followService").Collection("follows")

	filter := bson.M{"follower_id": userID}
	cursor, err := collection.Find(context.TODO(), filter)
	if err != nil {
		log.Printf("Error finding followees: %v", err)
		return "", err
	}
	defer cursor.Close(context.TODO())

	var results []bson.M
	if err = cursor.All(context.TODO(), &results); err != nil {
		log.Printf("Error getting all results: %v", err)
		return "", err
	}

	var followees []int
	for _, result := range results {
		if followeeID, ok := result["followee_id"].(int32); ok {
			followees = append(followees, int(followeeID))
		} else {
			log.Printf("Failed to convert followee_id to int32 for result: %v", result)
		}
	}

	response := FolloweesResponse{
		UserID:            userID,
		NumberOfFollowers: len(followees), // Update the field name as per your requirement
		Followees:         followees,      // Update the field name as per your requirement
	}

	responseJSON, err := json.Marshal(response)
	if err != nil {
		log.Printf("Error marshalling response to JSON: %v", err)
		return "", err
	}

	return string(responseJSON), nil
}

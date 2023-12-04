package main

// FollowRequest represents the payload for follow and unfollow requests
type FollowRequest struct {
	FollowerID int `json:"follower_id"`
	FolloweeID int `json:"followee_id"`
}

// ErrorResponse represents a generic error response
type ErrorResponse struct {
	Error string `json:"error"`
}

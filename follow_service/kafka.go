package main

import (
	"encoding/json"
	"fmt"
	"log"
	"os"

	"github.com/confluentinc/confluent-kafka-go/kafka"
)

var kafkaProducer *kafka.Producer

// KafkaMessage represents the message to be sent to Kafka
type KafkaMessage struct {
	FolloweeID      int    `json:"followeeId"`
	FollowerID      int    `json:"followerId"`
	FollowEventType string `json:"followEventType"`
}

// InitKafkaProducer initializes the Kafka producer
func InitKafkaProducer() {
	var err error
	config := &kafka.ConfigMap{
		"bootstrap.servers": os.Getenv("KAFKA_SERVER"),
		"security.protocol": "SASL_SSL",
		"sasl.mechanisms":   "PLAIN",
		"sasl.username":     os.Getenv("KAFKA_USERNAME"),
		"sasl.password":     os.Getenv("KAFKA_PASSWORD"),
	}

	kafkaProducer, err = kafka.NewProducer(config)
	if err != nil {
		log.Fatal(err)
	}

	fmt.Println("Kafka producer connected")
}

// SendFollowEvent sends a follow or unfollow event to Kafka
func SendFollowEvent(followReq FollowRequest, eventType string) error {
	topicName := os.Getenv("KAFKA_TOPIC") // Get topic name from environment variable
	if topicName == "" {
		return fmt.Errorf("Kafka topic name is not set")
	}

	message := KafkaMessage{
		FolloweeID:      followReq.FolloweeID,
		FollowerID:      followReq.FollowerID,
		FollowEventType: eventType,
	}

	messageBytes, err := json.Marshal(message)
	if err != nil {
		return err
	}

	return kafkaProducer.Produce(&kafka.Message{
		TopicPartition: kafka.TopicPartition{Topic: &topicName, Partition: kafka.PartitionAny},
		Value:          messageBytes,
	}, nil)
}

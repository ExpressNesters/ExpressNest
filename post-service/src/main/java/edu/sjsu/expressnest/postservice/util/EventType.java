package edu.sjsu.expressnest.postservice.util;

public enum EventType {
    POST_CREATE("post-created-events"),
    POST_UPDATE("post-updated-events"),
    POST_DELETE("post-deleted-events");

    private final String topicName;

    EventType(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicName() {
        return topicName;
    }
}
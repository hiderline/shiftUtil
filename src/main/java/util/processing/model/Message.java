package util.processing.model;

public class Message {
    private final Topic topic;
    private final String payload;

    public Message(Topic topic, String payload) {
        this.topic = topic;
        this.payload = payload;
    }

    public Topic getTopic() { return topic; }
    public String getPayload() { return payload; }

    @Override
    public String toString() {
        return String.format("[%s: %s]", topic, payload);
    }
}

package util.processing.model;

public class Message {
    private final String topic;
    private final String payload;

    public Message(String topic, String payload) {
        this.topic = topic;
        this.payload = payload;
    }

    public String getTopic() { return topic; }
    public String getPayload() { return payload; }

    @Override
    public String toString() {
        return String.format("[%s: %s]", topic, payload);
    }
}

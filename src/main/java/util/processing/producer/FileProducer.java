package util.processing.producer;

import util.processing.broker.MessageBroker;
import util.processing.model.Message;
import util.processing.model.Topic;
import util.processing.model.TopicSelector;
import util.services.FileReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class FileProducer implements Runnable{
    private final MessageBroker broker;
    private final List<String> files;
    private final TopicSelector topicSelector;
    private final CountDownLatch completionLatch;
    private final FileReader reader = FileReader.getInstance();

    public FileProducer(MessageBroker broker,
                        TopicSelector topicSelector,
                        List<String> files, CountDownLatch completionLatch) {
        this.broker = broker;
        this.files = files;
        this.topicSelector = topicSelector;
        this.completionLatch = completionLatch;
    }

    @Override
    public void run() {
        List<String> lines = new ArrayList<>();
        try {
            for (String file : files) {
                lines.addAll(reader.readFileLines(file));
            }
            for (String line: lines) {
                if (!line.trim().isEmpty()) {
                    Topic topic = topicSelector.determineTopic(line);
                    broker.publish(new Message(topic, line));
                }
            }
            for(Topic topic: Topic.values()) {
                broker.publish(new Message(topic, "EOF"));
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (completionLatch != null)
                completionLatch.countDown();
        }
    }
}

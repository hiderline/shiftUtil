package util.processing.producer;

import util.processing.broker.MessageBroker;
import util.processing.model.Message;
import util.processing.model.Topic;
import util.processing.model.TopicSelector;
import util.services.FileReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileProducer implements Runnable{
    private final MessageBroker broker;
    private final List<String> files;
    private final FileReader reader = FileReader.getInstance();

    public FileProducer(MessageBroker broker, List<String> files) {
        this.broker = broker;
        this.files = files;
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
                    Topic topic = TopicSelector.getInstance().determineTopic(line);
                    broker.publish(new Message(topic, line));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

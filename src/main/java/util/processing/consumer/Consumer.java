package util.processing.consumer;

import util.processing.broker.MessageBroker;
import util.processing.model.Message;
import util.processing.model.Topic;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class Consumer implements Runnable{
    private final MessageBroker broker;
    private final Topic topic;
    private final Path outputFile;

    public Consumer(MessageBroker broker, Topic topic, Path outputFile) {
        this.broker = broker;
        this.topic = topic;
        this.outputFile = outputFile;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // Pull-модель: забираем сообщение из очереди
                String message = broker.consume(topic, 50, TimeUnit.MILLISECONDS);

                if (message != null) {

                    System.out.println(topic + ": сообщение - " + message);
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

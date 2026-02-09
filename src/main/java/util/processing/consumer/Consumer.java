package util.processing.consumer;

import util.exceptions.ExceptionHandler;
import util.processing.broker.MessageBroker;
import util.processing.model.Topic;
import util.services.FileService;
import util.statistics.StatsStrategy;
import util.statistics.StatsStrategyFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Consumer implements Runnable{
    private final MessageBroker broker;
    private final Topic topic;
    private final Path outputFile;
    private final boolean appendMode;
    private final CountDownLatch completionLatch;
    private final StatsStrategy statsStrategy;
    private final FileService fileService = FileService.getInstance();

    public Consumer(MessageBroker broker, Topic topic, Path outputFile, boolean appendMode, CountDownLatch completionLatch) {
        this.broker = broker;
        this.topic = topic;
        this.outputFile = outputFile;
        this.appendMode = appendMode;
        this.completionLatch = completionLatch;
        this.statsStrategy = StatsStrategyFactory.createForTopic(topic);
    }

    public StatsStrategy getStatsStrategy() {
        return statsStrategy;
    }

    @Override
    public void run() {
        BufferedWriter writer = null;
        boolean fileCreated = false;

        try {
            while (true) {
                // Забираем сообщение из очереди
                String message = broker.consume(topic, 100, TimeUnit.MILLISECONDS);

                if (message != null && message.equals("EOF") || Thread.currentThread().isInterrupted())
                    break;

                if (message != null) {
                    statsStrategy.update(message);

                    if (!fileCreated) {
                        writer = fileService.createWriter(outputFile, appendMode);
                        fileCreated = true;
                    }

                    writer.write(message);
                    writer.newLine();
                }
            }
        } catch (InterruptedException | IOException e) {
            ExceptionHandler.handleException(new RuntimeException(e));
        } finally {
            if (writer != null) {
                try {
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    ExceptionHandler.printInfo(topic + ": Ошибка при закрытии файла - " + e.getMessage());
                }
            }
            if (completionLatch != null)
                completionLatch.countDown();
        }
    }
}

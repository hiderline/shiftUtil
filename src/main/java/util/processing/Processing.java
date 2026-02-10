package util.processing;

import util.config.CliConfig;
import util.config.StatsLevel;
import util.exceptions.ExceptionHandler;
import util.processing.broker.MessageBroker;
import util.processing.consumer.Consumer;
import util.processing.model.Topic;
import util.processing.model.TopicSelector;
import util.processing.producer.FileProducer;
import util.services.FileService;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Processing {
    private static final int THREADS_AMOUNT = 4;
    private final CliConfig config;
    private MessageBroker broker;
    private final TopicSelector topicSelector = TopicSelector.getInstance();
    private final FileService fileService = FileService.getInstance();
    private ExecutorService executorService;
    CountDownLatch producerLatch = new CountDownLatch(1);
    CountDownLatch consumersLatch = new CountDownLatch(Topic.values().length);

    public Processing(CliConfig config) {
        this.config = config;
    }

    public void process() {
        broker = new MessageBroker();
        executorService = Executors.newFixedThreadPool(THREADS_AMOUNT);

        // Запуск consumers
        List<Consumer> consumers = createConsumers();
        for(Consumer consumer : consumers) {
            executorService.execute(consumer);
        }
        // Запуск producer
        FileProducer producer = new FileProducer(broker, topicSelector, config.getFiles(), producerLatch);
        executorService.execute(producer);

        // Ожидаем завершения работы
        waitForCompletion();

        // Вывод в консоль статистики
        if (config.getStatsLevel() != StatsLevel.NONE) {
            for (Consumer consumer : consumers) {
                System.out.println(consumer.getStatsStrategy().getFormattedStats(config.getStatsLevel()));
            }
        }

        shutdownExecutor();
    }

    private void waitForCompletion() {
        try {
            producerLatch.await();
            consumersLatch.await();
        } catch (InterruptedException e) {
            ExceptionHandler.printWarning("Обработка прервана");
            Thread.currentThread().interrupt();
            executorService.shutdownNow();
        }
    }

    private void shutdownExecutor() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private List<Consumer> createConsumers() {
        List<Consumer> consumerList = new ArrayList<>();

        for (Topic topic : Topic.values()) {
            // Полный путь к файлу
            Path outputPath = fileService.buildOutputPath(
                    config.getPathOut(),
                    config.getPrefOut(),
                    topic.getDescription() + ".txt"
            );

            consumerList.add(new Consumer(
                    broker,
                    topic,
                    outputPath,
                    config.appendMode(),
                    consumersLatch
            ));
        }
        return consumerList;
    }
}

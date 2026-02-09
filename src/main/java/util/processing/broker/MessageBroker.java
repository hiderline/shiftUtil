package util.processing.broker;
import util.processing.model.Message;
import util.processing.model.Topic;

import java.util.concurrent.*;

public class MessageBroker {
    // ConcurrentHashMap для хранения очередей по темам
    private final ConcurrentHashMap<Topic, BlockingQueue<String>> queues;
    //private final BlockingQueue<Message> queues;

    public MessageBroker() {
        this.queues = new ConcurrentHashMap<>();
    }

    // Создание/получение очереди для темы
    private BlockingQueue<String> getOrCreateQueue(Topic topic) {
        return queues.computeIfAbsent(topic, k -> new LinkedBlockingQueue<>());
    }

    // Публикация сообщения
    public void publish(Message message) throws InterruptedException {
        BlockingQueue<String> queue = getOrCreateQueue(message.getTopic());

        // Кладем сообщение в очередь соответствующей темы
        queue.put(message.getPayload());
        System.out.println("Broker: Опубликовано сообщение " + message);
    }

    // Получение сообщений из очереди (pull-модель)
    public String consume(Topic topic, long timeout, TimeUnit unit)
            throws InterruptedException {
        BlockingQueue<String> queue = getOrCreateQueue(topic);
        return queue.poll(timeout, unit);
    }
}
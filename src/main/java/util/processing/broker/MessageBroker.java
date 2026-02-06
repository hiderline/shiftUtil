package util.processing.broker;
import util.processing.model.Message;
import util.processing.model.Topic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MessageBroker {
    // ConcurrentHashMap для хранения очередей по темам
    private final ConcurrentHashMap<Topic, BlockingQueue<String>> queues;
    //private final BlockingQueue<Message> queues;
    private volatile boolean running = true;

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

    public void finish() throws InterruptedException {
        for(Topic topic: Topic.values()) {
            this.publish(new Message(topic, "EOF"));
        }
    }

    // Получение всех сообщений из очереди
    public List<String> drain(Topic topic) {
        BlockingQueue<String> queue = queues.get(topic);
        List<String> messages = new ArrayList<>();
        if (queue != null) {
            queue.drainTo(messages);
        }
        return messages;
    }

}
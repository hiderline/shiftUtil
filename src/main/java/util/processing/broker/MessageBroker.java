package util.processing.broker;
import util.processing.model.Message;
import util.processing.model.Topic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MessageBroker {
    // ConcurrentHashMap для хранения очередей по темам
    private final ConcurrentHashMap<Topic, BlockingQueue<Message>> queues;
    //private final BlockingQueue<Message> queues;
    private volatile boolean running = true;

    public MessageBroker() {
        this.queues = new ConcurrentHashMap<>();
    }

    // Создание/получение очереди для темы
    private BlockingQueue<Message> getOrCreateQueue(Topic topic) {
        return queues.computeIfAbsent(topic, k -> new LinkedBlockingQueue<>());
    }

    // Публикация сообщения
    public void publish(Topic topic, String payload) throws InterruptedException {
        Message message = new Message(topic, payload);
        BlockingQueue<Message> queue = getOrCreateQueue(topic);

        // Кладем сообщение в очередь соответствующей темы
        queue.put(message);
        System.out.println("Broker: Опубликовано сообщение " + message);
    }

    // Получение сообщений из очереди (pull-модель)
    public Message consume(Topic topic, long timeout, TimeUnit unit)
            throws InterruptedException {
        BlockingQueue<Message> queue = getOrCreateQueue(topic);
        return queue.poll(timeout, unit);
    }

    public void finish() {
        /*for(Topic topic: Topic.values()) {
            BlockingQueue<Message> eof
        }*/
    }

    // Получение всех сообщений из очереди
    public List<Message> drain(Topic topic) {
        BlockingQueue<Message> queue = queues.get(topic);
        List<Message> messages = new ArrayList<>();
        if (queue != null) {
            queue.drainTo(messages);
        }
        return messages;
    }

}
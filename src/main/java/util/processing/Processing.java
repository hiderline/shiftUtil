package util.processing;

import util.config.CliConfig;
import util.processing.broker.MessageBroker;
import util.processing.consumer.Consumer;
import util.processing.model.Message;
import util.processing.model.Topic;
import util.processing.model.TopicSelector;
import util.processing.producer.FileProducer;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Processing {
    private final CliConfig config;
    private MessageBroker broker;
    private List<Consumer> consumers;
    private FileProducer producer;
    private final TopicSelector topicSelector = TopicSelector.getInstance();

    public Processing(CliConfig config) {
        this.config = config;
    }

    public void process() {
        broker = new MessageBroker();

        consumers = new ArrayList<>();
        consumers.add(new Consumer(broker, Topic.INTEGER, Path.of(config.getPathOut())));
        for(Consumer consumer: consumers) {
            new Thread(consumer).start();
        }

        producer = new FileProducer(broker, topicSelector, config.getFiles());
        new Thread(producer).start();
        //producer.run();




        /*for (Message msg: broker.drain(Topic.INTEGER)){
            System.out.println(msg);
        }
        for (Message msg: broker.drain(Topic.FLOAT)){
            System.out.println(msg);
        }
        for (Message msg: broker.drain(Topic.STRING)){
            System.out.println(msg);
        }*/
    }
}

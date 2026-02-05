package util.processing;

import util.config.CliConfig;
import util.processing.broker.MessageBroker;
import util.processing.consumer.Consumer;
import util.processing.model.Message;
import util.processing.producer.FileProducer;

import java.util.List;

public class Processing {
    private final CliConfig config;
    private MessageBroker broker;
    private List<Consumer> consumers;
    private FileProducer producer;

    public Processing(CliConfig config) {
        this.config = config;
    }

    public void process() {
        broker = new MessageBroker();
        producer = new FileProducer(broker, config.getFiles());
        producer.run();
        for (Message msg: broker.drain("integers")){
            System.out.println(msg);
        }
        for (Message msg: broker.drain("floats")){
            System.out.println(msg);
        }
        for (Message msg: broker.drain("strings")){
            System.out.println(msg);
        }
    }
}

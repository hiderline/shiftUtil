package util.processing.consumer;

import util.processing.broker.MessageBroker;

public class Consumer implements Runnable{
    private final MessageBroker brocker;

    public Consumer(MessageBroker brocker) {
        this.brocker = brocker;
    }

    @Override
    public void run() {

    }
}

package util.processing.producer;

import util.processing.broker.MessageBroker;
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
                System.out.println("=".repeat(80));

            }
            for (String s: lines) {
                broker.publish(determineTopic(s), s);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String determineTopic(String data) {
        try {
            Integer.parseInt(data);
            return "integers";
        } catch (NumberFormatException e1) {
            try {
                Float.parseFloat(data);
                return "floats";
            } catch (NumberFormatException e2) {
                return "strings";
            }
        }
    }
}

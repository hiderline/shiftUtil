package util;

import util.exceptions.ExceptionHandler;

public class Main {
    public static void main(String[] args) throws Exception {
        args = new String[]{"-p", "prefix_", "-o", "/some/path", "-a","-f", "in1.txt", "in2.txt", "in3.txt"};
        args = new String[]{"-p","prefix_", "-o", "./some/path", "main/resources/in1.txt"};

        try {
            new ApplicationFacade().start(args);
        } catch (Exception e) {
            ExceptionHandler.handleException(e, "Ошибка при выполнении программы");
            System.exit(1);
        }

    }
}

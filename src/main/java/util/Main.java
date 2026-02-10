package util;

import util.exceptions.ExceptionHandler;

public class Main {
    public static void main(String[] args) {

        try {
            new ApplicationFacade().start(args);
        } catch (Exception e) {
            ExceptionHandler.handleException(e, "Ошибка при выполнении программы");
            System.exit(1);
        }

    }
}

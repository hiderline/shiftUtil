package util;

import util.exceptions.ExceptionHandler;

public class Main {
    public static void main(String[] args) throws Exception {
        args = new String[]{"-p", "prefix_", "-o", "/some/path", "-a","-f", "in1.txt", "in2.txt", "in3.txt"};
        args = new String[]{"-p","prefix_", "-o", "./some/path", "-f", "in1.txt","in2.txt", "in3.txt", "in4.txt"};
//        args = new String[]{"-p", "pref", "-o", "/some/path", "in1.txt"};
//        args = new String[]{"-p", "pref", "-o", "*/some/path", "-s", "in1.txt"};
//        args = new String[]{"-o", "/some/path", "in1*.txt"};
//        args = new String[]{"--help"};
//        args = new String[]{"-p", "-o", "-a", "-f", "in1.txt", "in2.txt", "in3.txt"};
        try {
            new ApplicationFacade().start(args);
        } catch (Exception e) {
            ExceptionHandler.handleException(e, "Ошибка при выполнении программы");
            System.exit(1);
        }

    }
}

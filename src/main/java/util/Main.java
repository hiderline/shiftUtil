package util;

public class Main {
    public static void main(String[] args) {
        args = new String[]{"-p", "prefix_", "-o", "/some/path", "-a", "-s", "-f", "in1.txt", "in2.txt", "in3.txt"};
        //args = new String[]{"-p", "prefix_", "-o", "/some/path", "-a", "-s", "-f", "in1.txt", "in2.txt", "in3.txt"};
        new ApplicationFacade().start(args);
    }
}

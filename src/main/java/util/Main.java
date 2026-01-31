package util;

public class Main {
    public static void main(String[] args) {
        args = new String[]{"-p", "prefix_", "-o", "/some/path", "-a","-f", "in1.txt", "in2.txt", "in3.txt"};
        args = new String[]{"-p","pref", "-o", "/some/path", "-a", "-s", "in1.txt"};
//        args = new String[]{"--help"};
//        args = new String[]{"-p", "-o", "-a", "-f", "in1.txt", "in2.txt", "in3.txt"};
        new ApplicationFacade().start(args);
    }
}

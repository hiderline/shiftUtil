package util;

import util.config.CliConfig;
import util.config.Configuration;

public class ApplicationFacade {
    private final CliConfig appConfig = CliConfig.getInstance();

    public void start(String[] args) throws Exception {
        appConfig.call();

        System.out.println("-p: " + appConfig.getPrefOut());
        System.out.println("-o: " + appConfig.getPathOut());
        System.out.println("-a: " + appConfig.appendMode());
        System.out.println("stats: " + appConfig.getStatsLevel());
        System.out.println("files: " + appConfig.getFiles());
    }
}

package util;

import util.config.CliConfig;
import util.config.Configuration;

public class ApplicationFacade {
    private final CliConfig appConfig = CliConfig.getInstance();

    public void start(String[] args) {
        appConfig.setConfig(args);

        System.out.println(appConfig.getPrefOut());
        System.out.println(appConfig.getPathOut());
        System.out.println(appConfig.appendMode());
        System.out.println(appConfig.shortStats());
        System.out.println(appConfig.fullStats());
        System.out.println(appConfig.getFiles());
    }
}

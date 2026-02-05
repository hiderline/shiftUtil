package util;

import util.config.CliConfig;
import util.processing.Processing;

public class ApplicationFacade {
    private final CliConfig appConfig = CliConfig.getInstance();
    private Processing processing;

    public void start(String[] args) throws Exception {
        appConfig.setConfig(args);
        processing = new Processing(appConfig);
        processing.process();

        System.out.println("-p: " + appConfig.getPrefOut());
        System.out.println("-o: " + appConfig.getPathOut());
        System.out.println("-a: " + appConfig.appendMode());
        System.out.println("stats: " + appConfig.getStatsLevel());
        System.out.println("files: " + appConfig.getFiles());
    }
}

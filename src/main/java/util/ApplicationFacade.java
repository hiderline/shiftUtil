package util;

import util.config.CliConfig;

public class ApplicationFacade {
    private final CliConfig appConfig = CliConfig.getInstance();

    public void start(String[] args) {
        appConfig.setConfig(args);

        System.out.println(appConfig.getPrefOut());
        System.out.println(appConfig.getPathOut());
        System.out.println(appConfig.appendMode());
        System.out.println(appConfig.getStatsLevel());
        //System.out.println(appConfig.isShortStats());
        //System.out.println(appConfig.isFullStats());
        System.out.println(appConfig.getFiles());
    }
}

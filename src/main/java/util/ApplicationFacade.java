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
    }
}

package util.config;

import com.beust.jcommander.JCommander;

public class Configuration {
    public static final Configuration instance = new Configuration();

    public void setConfig(String[] args) {
        CliConfig appConfig = new CliConfig();
        JCommander.newBuilder().addObject(appConfig).build().parse(args);

    }

    public static Configuration getInstance() {return instance;}
}

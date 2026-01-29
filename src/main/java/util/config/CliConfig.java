package util.config;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

public class CliConfig {
    public static final CliConfig instance = new CliConfig();

    private JCommander jCommander;

    @Parameter(names = {"--help", "-h"}, description = "Показать справку по использованию", help = true)
    private boolean help;

    @Parameter(names = {"-o"}, description = "Путь для выходных файлов")
    private String pathOut;

    @Parameter(names = {"-p"}, description = "Префикс имени выходных файлов")
    private String prefOut;

    @Parameter(names = {"-a"}, description = "Режим добавления в существующие файлы")
    private boolean append;

    @Parameter(names = {"-s"}, description = "Краткая статистика")
    private boolean shortStats;

    @Parameter(names = {"-f"}, description = "Полная статистика")
    private boolean fullStats;

    @Parameter(description = "Входные файлы для обработки", required = true)
    private List<String> files;

    public CliConfig() {
    }

    public static CliConfig getInstance() {return instance;}

    public boolean isHelp() {
        return help;
    }

    public String getPathOut() {
        return pathOut;
    }

    public String getPrefOut() {
        return prefOut;
    }

    public boolean appendMode() {
        return append;
    }

    public boolean shortStats() {
        return shortStats;
    }

    public boolean fullStats() {
        return fullStats;
    }

    public List<String> getFiles() {
        return files == null ? new ArrayList<>() : new ArrayList<>(this.files);
    }

    public void setConfig(String[] args) {
        JCommander.newBuilder().addObject(instance).build().parse(args);

    }
}

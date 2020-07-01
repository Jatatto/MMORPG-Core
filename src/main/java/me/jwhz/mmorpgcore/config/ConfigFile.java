package me.jwhz.mmorpgcore.config;

import me.jwhz.mmorpgcore.MMORPGCore;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigFile {

    protected MMORPGCore core = MMORPGCore.getInstance();
    private String fileName;
    private File file;
    protected YamlConfiguration yamlConfiguration;

    public ConfigFile(String fileName) {

        this.fileName = fileName;

        if (!core.getDataFolder().exists())
            core.getDataFolder().mkdir();

        file = new File(core.getDataFolder() + "/" + fileName + ".yml");

        if (!file.exists())
            if (core.getResource(fileName + ".yml") != null)
                core.saveResource(fileName + ".yml", true);
            else {

                try {

                    file.createNewFile();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        yamlConfiguration = YamlConfiguration.loadConfiguration(file);

        ConfigHandler.setPresets(this);
        ConfigHandler.reload(this);

    }

    public void save() {

        try {
            getYamlConfiguration().save(getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public File getFile() {

        return file;

    }

    public YamlConfiguration getYamlConfiguration() {

        return yamlConfiguration != null ? yamlConfiguration : YamlConfiguration.loadConfiguration(file);

    }

}

/*
 * Copyright (C) 2018 Nico Lube
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.nicolube.devcore.client.manager.config;

/**
 *
 * @author Nico Lube
 */
import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class BaseHolder {
    private File file;
    protected FileConfiguration config;
    private final String configName;
    private final String fileEnding;
    private final ConfigManager configManager;

    public BaseHolder(String configName, String fileEnding, ConfigManager configManager) {
        this.configManager = configManager;
        this.file = new File(configManager.getConfigFolder(), configName + "."+ fileEnding);
        this.fileEnding = fileEnding;
        load();
        this.configName = configName;
    }

    public final void load() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public File getFile() {
        return file;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public String getName() {
        return configName;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileEnding() {
        return fileEnding;
    }
    
    
}

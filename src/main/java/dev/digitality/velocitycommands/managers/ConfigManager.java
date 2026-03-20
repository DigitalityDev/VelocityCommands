package dev.digitality.velocitycommands.managers;

import lombok.Getter;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public final class ConfigManager {

    @Getter
    private static ConfigManager instance;

    private final Path dataDirectory;
    private final Logger logger;
    private Map<String, Object> config = new LinkedHashMap<>();

    public ConfigManager(Path dataDirectory, Logger logger) {
        instance = this;
        this.dataDirectory = dataDirectory;
        this.logger = logger;
    }

    public static void load() {
        try { Files.createDirectories(instance.dataDirectory); }
        catch (IOException e) { instance.logger.severe("Failed to create data directory"); }

        Path configFile = instance.dataDirectory.resolve("settings.yml");
        if (!Files.exists(configFile)) {
            try (InputStream in = instance.getClass().getResourceAsStream("/settings.yml")) {
                if (in != null) Files.copy(in, configFile);
                else Files.createFile(configFile);
            } catch (IOException e) {
                instance.logger.severe("Failed to copy default settings.yml");
            }
        }

        try (Reader reader = new FileReader(configFile.toFile())) {
            Yaml yaml = new Yaml();
            Map<String, Object> loaded = yaml.load(reader);
            instance.config = loaded != null ? loaded : new LinkedHashMap<>();
        } catch (IOException e) {
            instance.logger.severe("Failed to load settings.yml");
        }
    }

    public static void save() {
        Path configFile = instance.dataDirectory.resolve("settings.yml");
        DumperOptions opts = new DumperOptions();
        opts.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        opts.setPrettyFlow(true);

        try (Writer writer = new FileWriter(configFile.toFile())) {
            new Yaml(opts).dump(instance.config, writer);
        } catch (IOException e) {
            instance.logger.severe("Failed to save settings.yml");
        }
    }

    public static void reload() {
        instance.config.clear();
        load();
    }

    public static String getString(String key, String def) {
        Object val = get(key);
        return val instanceof String s ? s : def;
    }

    public static boolean getBoolean(String key, boolean def) {
        Object val = get(key);
        return val instanceof Boolean b ? b : def;
    }

    public static List<String> getStringList(String key) {
        Object val = get(key);
        if (val instanceof List<?> list)
            return list.stream().filter(o -> o instanceof String).map(o -> (String) o).toList();
        return Collections.emptyList();
    }

    public static Map<String, String> getStringMap(String key) {
        Object val = instance.config.get(key);
        if (val == null) return Collections.emptyMap();
        if (val instanceof Map<?, ?> map) {
            Map<String, String> result = new LinkedHashMap<>();
            map.forEach((k, v) -> {
                if (k instanceof String ks && v instanceof String vs) result.put(ks, vs);
            });
            return result;
        }
        return Collections.emptyMap();
    }

    public static void set(String key, Object value) {
        instance.config.put(key, value);
    }

    private static Object get(String key) {
        return instance.config.get(key);
    }
}


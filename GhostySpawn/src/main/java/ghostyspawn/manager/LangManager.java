package ghostyspawn.manager;

import ghostyspawn.GhostySpawn;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LangManager {

    private final GhostySpawn plugin;
    private FileConfiguration langConfig;

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    public LangManager(GhostySpawn plugin) {
        this.plugin = plugin;
    }

    public void load() {
        String language = plugin.getConfig().getString("language", "en");
        File langFolder = new File(plugin.getDataFolder(), "lang");

        if (!langFolder.exists()) {
            langFolder.mkdirs();
        }

        saveDefaultLang("en");
        saveDefaultLang("de");

        File langFile = new File(langFolder, language + ".yml");

        if (!langFile.exists()) {
            plugin.getLogger().warning("Language file '" + language + ".yml' not found! Falling back to 'en.yml'.");
            langFile = new File(langFolder, "en.yml");
        }

        langConfig = YamlConfiguration.loadConfiguration(langFile);

        InputStream defaultStream = plugin.getResource("lang/" + language + ".yml");
        if (defaultStream == null) {
            defaultStream = plugin.getResource("lang/en.yml");
        }
        if (defaultStream != null) {
            YamlConfiguration defaults = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(defaultStream, StandardCharsets.UTF_8));
            langConfig.setDefaults(defaults);
        }

        plugin.getLogger().info("Language loaded: " + language);
    }

    private void saveDefaultLang(String lang) {
        File langFile = new File(plugin.getDataFolder(), "lang/" + lang + ".yml");
        if (!langFile.exists()) {
            plugin.saveResource("lang/" + lang + ".yml", false);
        }
    }

    public String getMessage(String path, String... replacements) {
        String raw = langConfig.getString(path, "&cMissing message: " + path);
        String prefix = colorize(langConfig.getString("prefix", "&8[&bGhostySpawn&8] &r"));
        raw = raw.replace("{prefix}", prefix);

        for (int i = 0; i + 1 < replacements.length; i += 2) {
            raw = raw.replace(replacements[i], replacements[i + 1]);
        }

        return colorize(raw);
    }

    public static String colorize(String input) {
        if (input == null) return "";
        Matcher matcher = HEX_PATTERN.matcher(input);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, ChatColor.of("#" + matcher.group(1)).toString());
        }
        matcher.appendTail(sb);
        return ChatColor.translateAlternateColorCodes('&', sb.toString());
    }
}

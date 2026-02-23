package ghostyspawn;

import ghostyspawn.commands.AddSpawnCommand;
import ghostyspawn.commands.ReloadCommand;
import ghostyspawn.commands.SpawnCommand;
import ghostyspawn.manager.LangManager;
import ghostyspawn.manager.SpawnManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class GhostySpawn extends JavaPlugin {

    private static GhostySpawn instance;
    private LangManager langManager;
    private SpawnManager spawnManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        langManager = new LangManager(this);
        langManager.load();

        spawnManager = new SpawnManager(this);

        Objects.requireNonNull(getCommand("spawn")).setExecutor(new SpawnCommand(this));
        Objects.requireNonNull(getCommand("addspawn")).setExecutor(new AddSpawnCommand(this));
        Objects.requireNonNull(getCommand("ghostyspawn")).setExecutor(new ReloadCommand(this));

        getLogger().info("GhostySpawn v" + getPluginMeta().getVersion() + " enabled! Made by Ger_Gh0stface.");
    }

    @Override
    public void onDisable() {
        getLogger().info("GhostySpawn disabled.");
    }

    public static GhostySpawn getInstance() {
        return instance;
    }

    public LangManager getLangManager() {
        return langManager;
    }

    public SpawnManager getSpawnManager() {
        return spawnManager;
    }
}

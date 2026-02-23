package ghostyspawn.manager;

import ghostyspawn.GhostySpawn;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class SpawnManager {

    private final GhostySpawn plugin;

    public SpawnManager(GhostySpawn plugin) {
        this.plugin = plugin;
    }

    public boolean hasSpawn() {
        return plugin.getConfig().getString("spawn.world") != null
                && !plugin.getConfig().getString("spawn.world", "").isEmpty();
    }

    public Location getSpawn() {
        if (!hasSpawn()) return null;

        String worldName = plugin.getConfig().getString("spawn.world");
        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            plugin.getLogger().warning("Spawn world '" + worldName + "' not found!");
            return null;
        }

        double x = plugin.getConfig().getDouble("spawn.x");
        double y = plugin.getConfig().getDouble("spawn.y");
        double z = plugin.getConfig().getDouble("spawn.z");
        float yaw = (float) plugin.getConfig().getDouble("spawn.yaw");
        float pitch = (float) plugin.getConfig().getDouble("spawn.pitch");

        return new Location(world, x, y, z, yaw, pitch);
    }

    public void setSpawn(Location location) {
        plugin.getConfig().set("spawn.world", location.getWorld().getName());
        plugin.getConfig().set("spawn.x", location.getX());
        plugin.getConfig().set("spawn.y", location.getY());
        plugin.getConfig().set("spawn.z", location.getZ());
        plugin.getConfig().set("spawn.yaw", location.getYaw());
        plugin.getConfig().set("spawn.pitch", location.getPitch());
        plugin.saveConfig();
    }
}

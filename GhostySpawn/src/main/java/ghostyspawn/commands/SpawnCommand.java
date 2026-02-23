package ghostyspawn.commands;

import ghostyspawn.GhostySpawn;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SpawnCommand implements CommandExecutor {

    private final GhostySpawn plugin;
    private final Set<UUID> pending = new HashSet<>();

    public SpawnCommand(GhostySpawn plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getLangManager().getMessage("spawn.only-players"));
            return true;
        }

        if (!player.hasPermission("ghostyspawn.spawn")) {
            player.sendMessage(plugin.getLangManager().getMessage("spawn.no-permission"));
            return true;
        }

        if (!plugin.getSpawnManager().hasSpawn()) {
            player.sendMessage(plugin.getLangManager().getMessage("spawn.no-spawn-set"));
            return true;
        }

        if (pending.contains(player.getUniqueId())) {
            return true;
        }

        int delay = plugin.getConfig().getInt("teleport-delay", 3);
        boolean bypassDelay = player.hasPermission("ghostyspawn.bypass-delay");

        if (delay <= 0 || bypassDelay) {
            teleportNow(player);
            return true;
        }

        pending.add(player.getUniqueId());
        boolean cancelOnMove = plugin.getConfig().getBoolean("cancel-on-move", true);
        boolean showCountdown = plugin.getConfig().getBoolean("show-countdown", true);
        Location startLocation = player.getLocation().clone();

        if (showCountdown) {
            player.sendMessage(plugin.getLangManager().getMessage(
                    "spawn.teleporting", "{seconds}", String.valueOf(delay)));
        }

        new BukkitRunnable() {
            int remaining = delay;

            @Override
            public void run() {
                if (!player.isOnline()) {
                    pending.remove(player.getUniqueId());
                    cancel();
                    return;
                }

                if (cancelOnMove && hasPlayerMoved(startLocation, player.getLocation())) {
                    pending.remove(player.getUniqueId());
                    player.sendMessage(plugin.getLangManager().getMessage("spawn.cancelled"));
                    cancel();
                    return;
                }

                if (remaining <= 0) {
                    pending.remove(player.getUniqueId());
                    cancel();
                    teleportNow(player);
                    return;
                }

                if (showCountdown) {
                    player.sendMessage(plugin.getLangManager().getMessage(
                            "countdown.second", "{seconds}", String.valueOf(remaining)));
                }

                remaining--;
            }
        }.runTaskTimer(plugin, 0L, 20L);

        return true;
    }

    private void teleportNow(Player player) {
        Location spawn = plugin.getSpawnManager().getSpawn();
        if (spawn == null) {
            player.sendMessage(plugin.getLangManager().getMessage("spawn.no-spawn-set"));
            return;
        }
        player.teleport(spawn);
        player.sendMessage(plugin.getLangManager().getMessage("spawn.teleported"));
    }

    private boolean hasPlayerMoved(Location start, Location current) {
        if (!start.getWorld().equals(current.getWorld())) return true;
        return Math.abs(start.getX() - current.getX()) > 0.1
                || Math.abs(start.getY() - current.getY()) > 0.1
                || Math.abs(start.getZ() - current.getZ()) > 0.1;
    }
}

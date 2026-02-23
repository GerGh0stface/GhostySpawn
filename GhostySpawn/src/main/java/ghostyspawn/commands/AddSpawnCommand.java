package ghostyspawn.commands;

import ghostyspawn.GhostySpawn;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AddSpawnCommand implements CommandExecutor {

    private final GhostySpawn plugin;

    public AddSpawnCommand(GhostySpawn plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getLangManager().getMessage("addspawn.only-players"));
            return true;
        }

        if (!player.hasPermission("ghostyspawn.addspawn")) {
            player.sendMessage(plugin.getLangManager().getMessage("addspawn.no-permission"));
            return true;
        }

        plugin.getSpawnManager().setSpawn(player.getLocation());
        player.sendMessage(plugin.getLangManager().getMessage("addspawn.success"));
        return true;
    }
}

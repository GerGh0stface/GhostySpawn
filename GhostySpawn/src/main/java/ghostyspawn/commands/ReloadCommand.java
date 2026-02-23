package ghostyspawn.commands;

import ghostyspawn.GhostySpawn;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {

    private final GhostySpawn plugin;

    public ReloadCommand(GhostySpawn plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("ghostyspawn.reload")) {
            sender.sendMessage(plugin.getLangManager().getMessage("reload.no-permission"));
            return true;
        }

        if (args.length == 0 || !args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(plugin.getLangManager().getMessage("reload.usage"));
            return true;
        }

        plugin.reloadConfig();
        plugin.getLangManager().load();

        sender.sendMessage(plugin.getLangManager().getMessage("reload.success"));
        return true;
    }
}

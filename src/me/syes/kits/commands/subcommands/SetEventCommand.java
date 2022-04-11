package me.syes.kits.commands.subcommands;

import org.bukkit.entity.Player;

public class SetEventCommand extends SubCommand {

    @Override
    public void execute(Player p, String[] args) {

    }

    @Override
    public void help(Player p) {
        p.sendMessage("§cUsage: /event set <name>");
    }

    @Override
    public String permission() {
        return "kits.admin";
    }

}

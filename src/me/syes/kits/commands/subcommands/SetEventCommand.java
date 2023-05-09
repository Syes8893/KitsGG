package me.syes.kits.commands.subcommands;

import me.syes.kits.Kits;
import me.syes.kits.event.Event;
import org.bukkit.entity.Player;

public class SetEventCommand extends SubCommand {

    @Override
    public void execute(Player p, String[] args) {
        if(args.length < 1) {
            help(p);
            return;
        }
        if(args[0].equalsIgnoreCase("set")) {
            if(args.length < 2) {
                help(p);
                return;
            }
            Event newEvent = Kits.getInstance().getEventManager().getEvent(args[1].replace("_", " "));
            if(newEvent == null){
                p.sendMessage("§cInvalid event, valid events are Gold_Rush, Showdown, Marksman, Boss, Rambo, Koth and SkyFight.");
                return;
            }
            Kits.getInstance().getEventManager().setNextEvent(newEvent);
            p.sendMessage("§aSuccessfully set the next event to " + newEvent.getName() + ".");
        }
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

package me.jack.jteams.command;

import me.jack.jteams.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Invite implements CommandExecutor {

    private Teams instance;

    public Invite(Teams instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (cmd.getName().equalsIgnoreCase("invite")) {
//            if (sender instanceof Player) {
//                Player player = (Player) sender;

                if (args.length > 1) {
                    System.out.println("incorrect usage!");
                    return true;
                }

                Team team = instance.getPlayerTeam("admin");
                TeamMember member = team.getTeamMember("admin", team);

                System.out.println("removed from rogue!");



        }
        return true;
    }
}

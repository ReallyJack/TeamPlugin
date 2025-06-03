package me.jack.jteams.command;

import me.jack.jteams.SubTeam;
import me.jack.jteams.Team;
import me.jack.jteams.TeamMember;
import me.jack.jteams.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveRogue implements CommandExecutor {

    private Teams instance;

    public RemoveRogue(Teams instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (player.isOp()) {

                if (args.length > 0) {
                    player.sendMessage(ChatColor.RED + " Incorrect usage! /removerogue");
                    return true;
                }

                TeamMember member = instance.getPlayerTeam(player.getName()).getTeamMember(player.getName());

                if (instance.isPermanentRogue(member)) {
                    instance.releaseRogue(member);
                    instance.relseasePermanentRogue(member);

                    player.sendMessage("Rogue has been removed along with any cooldowns...");
                    instance.getPlayerTeam(player.getName()).clearPlayerPrefix(player);
                    member.resetTotalTeamChanges();
                }
            }
        }
        return false;
    }
}
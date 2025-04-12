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

public class Invite implements CommandExecutor {

    private Teams instance;

    public Invite(Teams instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] strings) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (strings.length > 1) {
                player.sendMessage(ChatColor.RED + "Incorrect usage! /invite <player>");
                return true;
            }

            SubTeam subTeam = instance.getPlayerSubTeam(player.getName());

            if (subTeam == null) {
                player.sendMessage(ChatColor.RED + "You are not in a subteam to send an invite!");
                return true;
            }

            if (sender.getName().equals(strings[0])) {
                player.sendMessage(ChatColor.RED + "You can not invite yourself to your own subteam!");
                return true;
            }

            Team targetTeam = instance.getPlayerTeam(strings[0]);

            if (targetTeam == null) {
                player.sendMessage(ChatColor.RED + "Player not found!");
                return true;
            }

            TeamMember targetMember = targetTeam.getTeamMember(strings[0]);
            
            if (subTeam.hasInvite(targetMember)) {
                player.sendMessage(ChatColor.RED + targetMember.getName() + " already has a pending invite!");
                return true;
            }

            Player targetPlayer = Bukkit.getPlayer(targetMember.getName());

            if (targetPlayer == null) {
                player.sendMessage(ChatColor.RED + "Player not online!");
                return true;
            }

            player.sendMessage("Invite sent to " + targetMember.getName());
            targetPlayer.sendMessage("You have been invited to " + subTeam.getName() + ". Type /join <subteam-name> to join!");
            subTeam.sendInvite(targetMember);

        }
        return false;
    }
}

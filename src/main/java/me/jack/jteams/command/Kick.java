package me.jack.jteams.command;

import me.jack.jteams.SubTeam;
import me.jack.jteams.Team;
import me.jack.jteams.TeamMember;
import me.jack.jteams.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Kick implements CommandExecutor {

    private Teams instance;

    public Kick(Teams instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            SubTeam subTeam = instance.getPlayerSubTeam(player.getName());

            if (player.getGameMode() == GameMode.SPECTATOR) {
                player.sendMessage(ChatColor.RED + "You have died and are not in this event!");
                return true;
            }

            if (subTeam == null) {
                player.sendMessage(ChatColor.RED + "You are not in a subteam!");
                return true;
            }

            if (args.length > 1) {
                player.sendMessage(ChatColor.RED + "Incorrect usage! You must specify a player to kick!");
                return true;
            }

            if (args[0].equals(player.getName())) {
                player.sendMessage(ChatColor.RED + "You can not kick yourself!");
                return true;
            }

            SubTeam targetSubTeam = instance.getPlayerSubTeam(args[0]);

            if (targetSubTeam == null || !targetSubTeam.getName().equals(subTeam.getName())) {
                player.sendMessage(args[0] + " is not in this subteam!");
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            Team targetTeam = instance.getPlayerTeam(args[0]);

            subTeam.removeMember(subTeam.getMember(args[0]));
            player.sendMessage(args[0] + " has been removed from the subteam!");

            if (target != null) {
                targetTeam.clearPlayerPrefix(target);
            }

            target.sendMessage("You have been kicked from " + subTeam.getName());

            TeamMember targetMember = targetTeam.getTeamMember(target.getName());
            targetMember.subtractTotalTeamChanges();

            instance.makeRogue(targetMember);

            if (targetMember.getTotalTeamChanges() == 0) {
                instance.makePermanentRogue(targetMember);
                targetTeam.setRoguePrefix(Bukkit.getPlayer(targetMember.getName()));
                player.sendMessage(ChatColor.RED + "You are now ROGUE and can not join any subteams forever!");
                instance.releaseRogue(targetMember);

            } else {
                target.sendMessage(ChatColor.RED + "You have " + targetMember.getTotalTeamChanges() + " changes left! Warning, once you reach 0, you cannot join any more subteams! Cooldown " + instance.cooldown_time + " minutes!");
            }
        }
        return false;
    }
}

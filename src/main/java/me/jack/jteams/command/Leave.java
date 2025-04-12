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

public class Leave implements CommandExecutor {

    private Teams instance;

    public Leave(Teams instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (cmd.getName().equalsIgnoreCase("leave")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (args.length > 0) {
                    player.sendMessage(ChatColor.RED + "Incorrect usage!");
                    return true;
                }

                Team team = instance.getPlayerTeam(player.getName());
                TeamMember member = team.getTeamMember(player.getName());

                if (instance.getPlayerSubTeam(player.getName()) == null) {
                    player.sendMessage(ChatColor.RED + "You are not in any subteam!");
                    return true;
                }

                SubTeam subTeam = instance.getPlayerSubTeam(member.getName());

                if (subTeam.getSubTeamMembers().size() > 1) {
                    subTeam.removeMember(member);
                    team.clearPlayerPrefix(Bukkit.getPlayer(member.getName()));
                    member.subtractTotalTeamChanges();
                    player.sendMessage("You have left the subteam " + subTeam.getName() + "!");
                    instance.makeRogue(member);

                    if (member.getTotalTeamChanges() == 0) {
                        instance.makePermanentRogue(member);
                        team.setRoguePrefix(Bukkit.getPlayer(member.getName()));
                        player.sendMessage(ChatColor.RED + "You are now ROGUE and can not join any subteams forever!");
                        instance.releaseRogue(member);
                        return true;
                    }

                    player.sendMessage(ChatColor.RED + "You have " + member.getTotalTeamChanges() + " changes left! Warning, once you reach 0, you cannot join any more subteams! Cooldown " + instance.cooldown_time + " minutes!");


                } else {

                    subTeam.removeMember(member);
                    team.removeSubTeam(subTeam);
                    team.clearPlayerPrefix(Bukkit.getPlayer(member.getName()));
                    member.subtractTotalTeamChanges();
                    player.sendMessage(ChatColor.RED + "You have disbanded the subteam " + subTeam.getName() + "!");
                    instance.makeRogue(member);

                    if (member.getTotalTeamChanges() == 0) {
                        instance.makePermanentRogue(member);
                        team.setRoguePrefix(Bukkit.getPlayer(member.getName()));
                        player.sendMessage(ChatColor.RED + "You are now ROGUE and can not join any subteams forever!");
                        instance.releaseRogue(member);
                        return true;
                    }

                    player.sendMessage(ChatColor.RED + "You have " + member.getTotalTeamChanges() + " changes left! Warning, once you reach 0, you cannot join any more subteams! Cooldown " + instance.cooldown_time + " minutes!");

                }
            }
        }
        return true;
    }
}

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

public class Join implements CommandExecutor {

    private Teams instance;

    public Join(Teams instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (cmd.getName().equalsIgnoreCase("join")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (args.length > 1) {
                    System.out.println("Incorrect usage!");
                    return true;
                }

                if (player.getGameMode() == GameMode.SPECTATOR) {
                    player.sendMessage(ChatColor.RED + "You have died and are not in this event!");
                    return true;
                }

                Team team = instance.getPlayerTeam(player.getName());

                if (team == null) {
                    player.sendMessage(ChatColor.RED + "You are not in a team!");
                    return true;
                }

                TeamMember teamMember = team.getTeamMember(player.getName());

                if (instance.isRogue(teamMember)) {
                    player.sendMessage(ChatColor.RED + "You can not join any subteams while in a cooldown! " +  (int) instance.getCooldownTime(player.getName()) + " seconds remaining.");
                    return true;
                }

                if (instance.isPermanentRogue(teamMember)) {
                    player.sendMessage(ChatColor.RED + "You can not join any subteams while rogue!");
                    return true;
                }

                SubTeam subTeam = instance.getSubTeam(args[0]);

                if (subTeam == null) {
                    player.sendMessage(ChatColor.RED + "This subteam does not exist!");
                    return true;
                }

                if (!subTeam.getInvites().contains(teamMember)) {
                    player.sendMessage(ChatColor.RED + "You do not have an invite to this subteam!");
                    return true;
                }

                SubTeam currentSubTeam = instance.getPlayerSubTeam(teamMember.getName());

                if (currentSubTeam == null) {
                    subTeam.addMember(teamMember);
                    subTeam.removeTeamInvite(teamMember);
                    team.setPlayerSubTeamPrefix(Bukkit.getPlayer(teamMember.getName()), subTeam);
                    //System.out.println("total team changes= " + teamMember.getTotalTeamChanges());
                    player.sendMessage("Added to subteam " + subTeam.getName() + "!");
                } else {

                    if (currentSubTeam.getSubTeamMembers().size() > 1) {
                        currentSubTeam.removeMember(teamMember);
                        player.sendMessage("You have left the subteam " + subTeam.getName() + "!");
                        teamMember.subtractTotalTeamChanges();
                        instance.makeRogue(teamMember);

                        if (teamMember.getTotalTeamChanges() == 0) {
                            instance.makePermanentRogue(teamMember);
                            team.setRoguePrefix(Bukkit.getPlayer(teamMember.getName()));
                            player.sendMessage(ChatColor.RED + "You are now ROGUE and can not join any subteams!");
                            instance.releaseRogue(teamMember);
                            return true;
                        }

                        player.sendMessage(ChatColor.RED + "You have " + teamMember.getTotalTeamChanges() + " changes left! Warning, once you reach 0, you cannot join any more subteams! Cooldown " + instance.cooldown_time + " minutes!");
                        team.clearPlayerPrefix(Bukkit.getPlayer(teamMember.getName()));

                    } else {

                        team.removeSubTeam(currentSubTeam);
                        currentSubTeam.removeMember(teamMember);
                        player.sendMessage("You have left the subteam " + subTeam.getName() + "!");
                        teamMember.subtractTotalTeamChanges();
                        instance.makeRogue(teamMember);

                        if (teamMember.getTotalTeamChanges() == 0) {
                            instance.makePermanentRogue(teamMember);
                            team.setRoguePrefix(Bukkit.getPlayer(teamMember.getName()));
                            player.sendMessage(ChatColor.RED + "You are now ROGUE and can not join any subteams!");
                            instance.releaseRogue(teamMember);
                            return true;
                        }

                        team.clearPlayerPrefix(Bukkit.getPlayer(teamMember.getName()));
                        player.sendMessage(ChatColor.RED + "You have " + teamMember.getTotalTeamChanges() + " changes left! Warning, once you reach 0, you cannot join any more subteams! Cooldown " + instance.cooldown_time + " minutes!");

                    }

                    subTeam.addMember(teamMember);
                    subTeam.removeTeamInvite(teamMember);
                    team.setPlayerSubTeamPrefix(Bukkit.getPlayer(teamMember.getName()), subTeam);
                    //System.out.println("total team changes= " + teamMember.getTotalTeamChanges());
                    player.sendMessage("Added to subteam " + subTeam.getName() + "!");
                }
            }
        }
        return true;
    }

}

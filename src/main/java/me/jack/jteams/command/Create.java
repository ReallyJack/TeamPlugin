package me.jack.jteams.command;

import me.jack.jteams.SubTeam;
import me.jack.jteams.Team;
import me.jack.jteams.TeamMember;
import me.jack.jteams.Teams;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Create implements CommandExecutor {

    private Teams instance;

    public Create(Teams instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (cmd.getName().equalsIgnoreCase("create")) {

            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (args.length > 1) {
                    player.sendMessage("Subteam names can not have spaces!");
                    return true;
                }

                if (args[0].length() > 16) {
                    player.sendMessage(ChatColor.RED + "Name limit reached! Name's can only have a total of 16 characters!");
                    return true;
                }

                String subTeamName = args[0];
                Team team = instance.getPlayerTeam(player.getName());
                TeamMember member = team.getTeamMember(player.getName());


                if (instance.isRogue(member)) {
                    player.sendMessage(ChatColor.RED + "You can not create any subteams while in a cooldown! "  + (int) instance.getCooldownTime(player.getName()) + " seconds remaining.");
                    return true;
                }

                if (instance.isPermanentRogue(member)) {
                    player.sendMessage(ChatColor.RED + "You can not create subteams while in rogue!");
                    return true;
                }

                if (instance.getPlayerSubTeam(player.getName()) != null) {
                    player.sendMessage(ChatColor.RED + "You are in a subteam already!");
                    return true;
                }

                if (instance.getSubTeam(args[0]) != null) {
                    player.sendMessage(ChatColor.RED + "Subteam with that name already exists!");
                    return true;
                }

                SubTeam subTeam = new SubTeam(subTeamName, team.getColor());
                team.createSubTeam(subTeam);
                team.setPlayerSubTeamPrefix(player, subTeam);
                subTeam.addMember(member);
                player.sendMessage("Subteam " + subTeamName + " has been created!");
            }
        }
        return true;
    }
}
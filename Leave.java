package me.jack.jteams.command;

import me.jack.jteams.SubTeam;
import me.jack.jteams.Team;
import me.jack.jteams.TeamMember;
import me.jack.jteams.Teams;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Leave implements CommandExecutor {

    private Teams instance;

    public Leave(Teams instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (cmd.getName().equalsIgnoreCase("leave")) {

            if (args.length > 0) {
                System.out.println("incorrect usage");
                return true;
            }

            Team team = instance.getPlayerTeam("testuser");
            TeamMember member = team.getTeamMember("testuser", team);

            SubTeam subTeam = instance.getPlayerSubTeam(member.getName());

            if (subTeam == null) {
                System.out.println("user is not in a subteam!");
                return true;
            }

            if (subTeam.getSubTeamMembers().size() > 1) {
                subTeam.removeMember(member);
                System.out.println("you have left the subteam " + subTeam.getName() + " size > 1");
            } else {

                team.removeSubTeam(subTeam);
                System.out.println("removed subteam " + subTeam.getName() + " size = 0");
            }

        }
        return true;
    }
}

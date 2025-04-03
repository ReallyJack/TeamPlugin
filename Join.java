package me.jack.jteams.command;

import me.jack.jteams.SubTeam;
import me.jack.jteams.Team;
import me.jack.jteams.TeamMember;
import me.jack.jteams.Teams;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Join implements CommandExecutor {

    private Teams instance;

    public Join(Teams instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (cmd.getName().equalsIgnoreCase("join")) {
//            if (sender instanceof Player) {
//                Player player = (Player) sender;

            if (args.length > 1) {
                System.out.println("Incorrect usage!");
                return true;
            }

            Team team = instance.getPlayerTeam("testuser");

            if (team == null) {
                System.out.println("Player is not in a team!");
                return true;
            }

            TeamMember teamMember = team.getTeamMember("testuser", team);

            if (instance.isRogue(teamMember)) {
                System.out.println("You can not join any subteams while rogue");
                return true;
            }

            SubTeam subTeam = instance.getSubTeam(args[0]);

            if (subTeam == null) {
                System.out.println("This subteam does not exist!");
                return true;
            }

            SubTeam currentSubTeam = instance.getPlayerSubTeam(teamMember.getName());

            if (currentSubTeam != null) {

                if (currentSubTeam.getSubTeamMembers().size() > 1) {
                    currentSubTeam.removeMember("testuser");
                    System.out.println("removed from " + currentSubTeam.getName() + " size > 1");
                } else {
                    team.removeSubTeam(currentSubTeam);
                    System.out.println("removed subteam " + currentSubTeam.getName() + " size = 0");

                }

                subTeam.addMember(teamMember);
                teamMember.incrementTotalTeamChanges();
                System.out.println("total team changes= " + teamMember.getTotalTeamChanges());
                System.out.println("added to subteam " + subTeam.getName());

            } else {

                subTeam.addMember(teamMember);
                teamMember.incrementTotalTeamChanges();
                System.out.println("total team changes= " + teamMember.getTotalTeamChanges());
                System.out.println("added to subteam " + subTeam.getName());
            }

            if (teamMember.getTotalTeamChanges() == 2) {
                instance.makeRogue(teamMember);
                System.out.println("ADDED TO ROGUE");

            }
        }

        return true;
    }

}

package me.jack.jteams.command;

import me.jack.jteams.Team;
import me.jack.jteams.TeamMember;
import me.jack.jteams.Teams;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Create implements CommandExecutor {

    private Teams instance;

    public Create(Teams instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (cmd.getName().equalsIgnoreCase("create")) {

            if (args[0].equalsIgnoreCase("blue")) {
                Team blue = instance.getTeam("BLUE");
                blue.createSubTeam("b", null);
                System.out.println("blue created");
                return true;
            }
            if (args[0].equalsIgnoreCase("red")) {
                Team red = instance.getTeam("RED");
                red.createSubTeam("r", new TeamMember("admin"));
                System.out.println("red created");
                return true;
            }
            if (args[0].equalsIgnoreCase("yellow")) {
                Team yellow = instance.getTeam("YELLOW");
                yellow.createSubTeam("y", new TeamMember("admin"));
                System.out.println("yellow created");
                return true;
            }
            if (args[0].equalsIgnoreCase("green")) {
                Team green = instance.getTeam("GREEN");
                green.createSubTeam("g", new TeamMember("admin"));
                System.out.println("green created");
                return true;
            }


            if (args.length > 1) {
                System.out.println("SubTeam names can not have spaces!");
                return true;
            }

            String subTeamName = args[0];
            Team team = instance.getPlayerTeam("testuser");
            TeamMember member = team.getTeamMember("testuser", team);

            if (instance.isRogue(member)) {
                System.out.println("you can not create subteams while in rogue");
                return true;
            }

            if (instance.getPlayerSubTeam(member.getName()) != null) {
                System.out.println("you are in a subteam already!");
                return true;
            }

            if (instance.getSubTeam(args[0]) != null) {
                System.out.println("subteam with that name already exists");
                return true;
            }

            team.createSubTeam(subTeamName, member);
            System.out.println(subTeamName + " created!");

        }
        return true;
    }
}
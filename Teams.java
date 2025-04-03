package me.jack.jteams;

import me.jack.jteams.command.Create;
import me.jack.jteams.command.Invite;
import me.jack.jteams.command.Join;
import me.jack.jteams.event.PlayerJoin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Teams extends JavaPlugin {

    public static List<Team> teams;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(this), this);

        getCommand("create").setExecutor(new Create(this));
        getCommand("join").setExecutor(new Join(this));
        getCommand("invite").setExecutor(new Invite(this));

        saveDefaultConfig();

        teams = new ArrayList<>();


        ConfigurationSection teamSection = getConfig().getConfigurationSection("teams");

        for (String keys : teamSection.getKeys(false)) {
            ConfigurationSection teamNames = teamSection.getConfigurationSection(keys);
            String name = teamNames.getString("name");
            ChatColor color = ChatColor.getByChar(teamNames.getString("color"));

            Team team = new Team(name, color);
            teams.add(team);
        }


        for (Team t : teams) {
            System.out.println(t.getSubTeams() + " " + t.getMembers() + " " + t.getColor() + " " + t.getName());
            t.addPlayerToTeam("admin", "RED");
        }

        //addPlayerToTeam("admin", "RED");

        System.out.println(getPlayerTeam("admin") + " team");
    }

    @Override
    public void onDisable() {

    }

    public Team getPlayerTeam(String playerName) {
        for (Team team : teams) {
            for (TeamMember member : team.getMembers()) {
                if (member.getName().equalsIgnoreCase(playerName)) {
                    return team;
                }
            }
        }
        return null;
    }


    public Team getTeam(String teamName) {
        for (Team team : teams) {
            if (team.getName().equalsIgnoreCase(teamName)) {
                return team;
            }
        }
        return null;
    }

    public SubTeam getPlayerSubTeam(TeamMember member) {
        for (Team team : teams) {
            for (SubTeam subTeam : team.getSubTeams()) {
                for (TeamMember members : subTeam.getSubTeamMembers()) {
                    if (members.getName().equals(member.getName())) {
                        return subTeam;
                    }
                }
            }
        }
        return null;
    }

    public List<Team> getTeams() {
        return teams;
    }


}

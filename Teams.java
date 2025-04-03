package me.jack.jteams;

import me.jack.jteams.command.Create;
import me.jack.jteams.command.Invite;
import me.jack.jteams.command.Join;
import me.jack.jteams.command.Leave;
import me.jack.jteams.event.PlayerJoin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Teams extends JavaPlugin implements CommandExecutor {

    public List<Team> teams;
    public static List<TeamMember> rogueMembers = new ArrayList<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(this), this);

        getCommand("create").setExecutor(new Create(this));
        getCommand("join").setExecutor(new Join(this));
        getCommand("invite").setExecutor(new Invite(this));
        getCommand("leave").setExecutor(new Leave(this));

        getCommand("print").setExecutor(this);
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

        //addPlayerToTeam("admin", "RED");
        teams.getFirst().addPlayerToTeam("testuser");
        System.out.println(getPlayerTeam("testuser").getName() + " team");
    }

    @Override
    public void onDisable() {

    }

    public void print() {
        for (Team t : teams) {
            System.out.println(t.getName() + "\n" + t.getMembers() + "\n");
            for (SubTeam st : t.getSubTeams()) {
                System.out.println(st.getName() + " : " + st.getSubTeamMembers());
            }
        }
    }

    public void makeRogue(TeamMember member) {

        rogueMembers.add(member);

    }

    public void releaseRogue(TeamMember member) {

        if (!rogueMembers.contains(member)) {
            return;
        }
        rogueMembers.remove(member);
    }


    public boolean isRogue(TeamMember member) {
        return rogueMembers.contains(member);
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

    public SubTeam getPlayerSubTeam(String playerName) {
        for (Team team : teams) {
            for (SubTeam subTeam : team.getSubTeams()) {
                for (TeamMember members : subTeam.getSubTeamMembers()) {
                    if (members.getName().equals(playerName)) {
                        return subTeam;
                    }
                }
            }
        }
        return null;
    }


    public SubTeam getSubTeam(String name) {
        for (Team team : teams) {
            for (SubTeam subTeam : team.getSubTeams()) {
                if (subTeam.getName().equals(name)) {
                    return subTeam;
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

    public List<Team> getTeams() {
        return teams;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (cmd.getName().equalsIgnoreCase("print")) {
            print();
        }
        return true;
    }

}

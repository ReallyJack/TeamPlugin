package me.jack.jteams;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Team {

    private String name;
    private ChatColor color;
    private int total_score;
    private List<TeamMember> members;
    private List<SubTeam> subTeams;
    private Location spawn;

    public Team(String name, ChatColor color, Location spawn) {
        this.name = name;
        this.color = color;
        this.spawn = spawn;
        this.total_score = 0;
        this.members = new ArrayList<>();
        this.subTeams = new ArrayList<>();
    }

    public TeamMember getTeamMember(String playerName) {
        for (TeamMember member : getMembers()) {
            if (member.getName().equalsIgnoreCase(playerName)) {
                return member;
            }
        }
        return null;
    }

    public void setPlayerSubTeamPrefix(Player player, SubTeam subTeam) {
        player.setDisplayName(subTeam.getColor() + "[" + subTeam.getName().strip() + "] " + color + player.getName() + ChatColor.WHITE);
        player.setPlayerListName(subTeam.getColor() + "[" + subTeam.getName().strip() + "] " + color + player.getName());
    }

    public void clearPlayerPrefix(Player player) {
        player.setDisplayName(color + player.getName() + ChatColor.WHITE);
        player.setPlayerListName(color + player.getName());
    }

    public void setRoguePrefix(Player player) {
        player.setDisplayName(ChatColor.GRAY + "[ROGUE] " + color + player.getName() + ChatColor.WHITE);
        player.setPlayerListName(ChatColor.GRAY + "[ROGUE] " + color + player.getName() + ChatColor.WHITE);
    }

    public void addPlayerToTeam(String playerName) {
        members.add(new TeamMember(playerName));
    }

    public void createSubTeam(SubTeam subTeam) {
        subTeams.add(subTeam);
    }

    public void removeSubTeam(SubTeam subTeam) {
        subTeams.remove(subTeam);
    }


    public Location getSpawn() {
        return spawn;
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }

    public int getTotalScore() {
        return total_score;
    }

    public List<TeamMember> getMembers() {
        return members;
    }

    public List<SubTeam> getSubTeams() {
        return subTeams;
    }

}
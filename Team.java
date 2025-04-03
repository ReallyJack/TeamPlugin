package me.jack.jteams;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Team {

    private String name;
    private ChatColor color;
    private int total_score;
    private List<TeamMember> members;
    private List<SubTeam> subTeams;

    public Team(String name, ChatColor color) {
        this.name = name;
        this.color = color;

        this.total_score = 0;
        this.members = new ArrayList<>();
        this.subTeams = new ArrayList<>();
    }

    public TeamMember getTeamMember(String playerName, Team team) {
        for (TeamMember member : team.getMembers()) {
            if (member.getName().equalsIgnoreCase(playerName)) {
                return member;
            }
        }
        return null;
    }

    public void removeMember(String name) {
        Iterator<TeamMember> member = getMembers().iterator();

        while (member.hasNext()) {
            if (member.next().getName().equals(name)) {
                member.remove();
                System.out.println("Removed from TEAM " + getName());
            } else {
                System.out.println("member not found in this team");
            }
        }
    }

    public void addPlayerToTeam(String playerName) {

        members.add(new TeamMember(playerName));

    }

    public void createSubTeam(String name, TeamMember member) {

        SubTeam newSubTeam = new SubTeam(name);
        subTeams.add(newSubTeam);
        newSubTeam.addMember(member);
    }

    public void removeSubTeam(SubTeam subTeam) {
        subTeams.remove(subTeam);
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
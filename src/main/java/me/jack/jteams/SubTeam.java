package me.jack.jteams;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class SubTeam {

    private String name;
    private List<TeamMember> members;
    private List<TeamMember> invites;
    private ChatColor color;

    public SubTeam(String name, ChatColor color) {

        this.name = name;
        this.color = color;
        this.members = new ArrayList<>();
        this.invites = new ArrayList<>();
    }

    public void sendInvite(TeamMember member) {
        invites.add(member);
    }

    public void removeTeamInvite(TeamMember teamMember) {
        invites.remove(teamMember);
    }

    public boolean hasInvite(TeamMember member) {
        return invites.contains(member);
    }

    public void addMember(TeamMember member) {
        members.add(member);
    }

    public TeamMember getMember(String playerName) {

        for (TeamMember member : members) {
            if (member.getName().equals(playerName)) {
                return member;
            }
        }
        return null;
    }

    public void removeMember(TeamMember member) {
        members.remove(member);
    }

    public String getName() {
        return name;
    }

    public List<TeamMember> getSubTeamMembers() {
        return members;
    }

    public ChatColor getColor() {
        return color;
    }

    public List<TeamMember> getInvites() {
        return invites;
    }

}

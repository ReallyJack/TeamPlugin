package me.jack.jteams;

import java.util.ArrayList;
import java.util.List;

public class SubTeam {

    private String name;
    private List<TeamMember> members;
    private List<TeamInvite> invites;

    public SubTeam(String name) {
        this.name = name;

        this.members = new ArrayList<>();
        this.invites = new ArrayList<>();
    }

    public void sendTeamInvite(TeamMember member) {

    }

    public void removeTeamInviteExpired() {

    }

    public void addMember(TeamMember member) {

        members.add(member);

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

    public List<TeamInvite> getInvites() {
        return invites;
    }

}

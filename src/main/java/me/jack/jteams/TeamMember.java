package me.jack.jteams;

public class TeamMember {

    private String name;
    private int total_team_change;

    public TeamMember(String name) {
        this.name = name;
        this.total_team_change = 3;
    }

    public String getName() {
        return name;
    }

    public int getTotalTeamChanges() {
        return total_team_change;
    }

    public void subtractTotalTeamChanges() {
        this.total_team_change -= 1;
    }

    public void resetTotalTeamChanges() {
        this.total_team_change = 3;
    }

}

package me.jack.jteams;

public class TeamMember {

    private String name;
    private int kills;
    private int deaths;
    private int total_team_change;

    public TeamMember(String name) {
        this.name = name;
        this.kills = 0;
        this.deaths = 0;
        this.total_team_change = 0;
    }

    public String getName() {
        return name;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getTotalTeamChanges() {
        return total_team_change;
    }

    public int incrementKills() {
        return this.kills += 1;
    }

    public int incrementDeaths() {
        return this.kills += 1;
    }

    public void incrementTotalTeamChanges() {
        this.total_team_change += 1;
    }


}

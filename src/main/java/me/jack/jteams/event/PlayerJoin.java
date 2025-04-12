package me.jack.jteams.event;

import me.jack.jteams.SubTeam;
import me.jack.jteams.Team;
import me.jack.jteams.TeamMember;
import me.jack.jteams.Teams;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    private Teams instance;

    public PlayerJoin(Teams instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Team team = instance.getPlayerTeam(player.getName());

        //TeamMember member = team.getTeamMember(player.getName());

        team.clearPlayerPrefix(player);

        if (instance.getPlayerSubTeam(player.getName()) == null) return;

        SubTeam subTeam = instance.getPlayerSubTeam(player.getName());

        team.setPlayerSubTeamPrefix(player, subTeam);

        player.teleport(team.getSpawn());
    }


}

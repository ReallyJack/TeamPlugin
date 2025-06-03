package me.jack.jteams.event;

import me.jack.jteams.Team;
import me.jack.jteams.Teams;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawn implements Listener {

    private Teams instance;

    public PlayerRespawn(Teams instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        Team team = instance.getPlayerTeam(player.getName());

        event.setRespawnLocation(team.getSpawn());

    }
}

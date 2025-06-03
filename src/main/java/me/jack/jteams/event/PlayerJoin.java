package me.jack.jteams.event;

import me.jack.jteams.SubTeam;
import me.jack.jteams.Team;
import me.jack.jteams.TeamMember;
import me.jack.jteams.Teams;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Random;

public class PlayerJoin implements Listener {

    private Teams instance;

    public PlayerJoin(Teams instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Team team = instance.getPlayerTeam(player.getName());

        if (team == null) {


            int randomNum = (int)(Math.random() * 4);

                if (randomNum == 0) {
                    Team redTeam = instance.getTeam("RED");
                    redTeam.addPlayerToTeam(player.getName());
                } else if (randomNum == 1) {
                    Team blueTeam = instance.getTeam("BLUE");
                    blueTeam.addPlayerToTeam(player.getName());

                } else if (randomNum == 2) {
                    Team yellowTeam = instance.getTeam("YELLOW");
                    yellowTeam.addPlayerToTeam(player.getName());

                } else {
                    Team greenTeam = instance.getTeam("GREEN");
                    greenTeam.addPlayerToTeam(player.getName());

                }

        }
        //TeamMember member = team.getTeamMember(player.getName());

        team.clearPlayerPrefix(player);
        player.teleport(team.getSpawn());

        if (instance.getPlayerSubTeam(player.getName()) == null) return;

        SubTeam subTeam = instance.getPlayerSubTeam(player.getName());

        team.setPlayerSubTeamPrefix(player, subTeam);

    }


}

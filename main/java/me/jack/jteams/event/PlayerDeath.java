package me.jack.jteams.event;

import me.jack.jteams.SubTeam;
import me.jack.jteams.Team;
import me.jack.jteams.TeamMember;
import me.jack.jteams.Teams;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {

    private Teams instance;

    public PlayerDeath(Teams instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        boolean hardcore_mode = instance.hardcore_mode;

        if (!hardcore_mode) return;

        Player player = (Player) event.getEntity();
        Team team = instance.getPlayerTeam(event.getEntity().getName());

        if (instance.getPlayerSubTeam(player.getName()) != null) {
            SubTeam subTeam = instance.getPlayerSubTeam(player.getName());
            TeamMember member = subTeam.getMember(player.getName());

            if (subTeam.getSubTeamMembers().size() > 1) {
                subTeam.removeMember(member);
            } else {
                subTeam.removeMember(member);
                team.removeSubTeam(subTeam);
            }

            team.clearPlayerPrefix(player);
        }

        player.setGameMode(GameMode.SPECTATOR);

        player.sendMessage(ChatColor.RED + "You have died and have been removed from the event!");

    }
}

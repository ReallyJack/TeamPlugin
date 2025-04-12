package me.jack.jteams.event;

import me.jack.jteams.SubTeam;
import me.jack.jteams.Teams;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamage implements Listener {

    private Teams instance;

    public PlayerDamage(Teams instance) {
        this.instance = instance;
    }

    @EventHandler
    public void PlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Player attacker = (Player) event.getDamager();

            SubTeam attackerSubTeam = instance.getPlayerSubTeam(attacker.getName());

            if (attackerSubTeam == null) return;

            SubTeam playerSubTeam = instance.getPlayerSubTeam(player.getName());

            if (playerSubTeam != null) {

                if (playerSubTeam.getName().equals(attackerSubTeam.getName())) {
                    event.setCancelled(true);
                }
            }
        }
    }

}

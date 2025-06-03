package me.jack.jteams;

import me.jack.jteams.command.*;
import me.jack.jteams.event.PlayerDamage;
import me.jack.jteams.event.PlayerDeath;
import me.jack.jteams.event.PlayerJoin;
import me.jack.jteams.event.PlayerRespawn;
import org.bukkit.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Teams extends JavaPlugin implements CommandExecutor {

    public Map<String, Long> rogueMembers;
    public List<String> permanentRogueMembers;
    public List<Team> teams;

    public int cooldown_time;
    public boolean hardcore_mode;

    int loop_counter = 0;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(this), this);

        getCommand("create").setExecutor(new Create(this));
        getCommand("invite").setExecutor(new Invite(this));
        getCommand("join").setExecutor(new Join(this));
        getCommand("leave").setExecutor(new Leave(this));
        getCommand("teamkick").setExecutor(new Kick(this));
        getCommand("removerogue").setExecutor(new RemoveRogue(this));

        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDamage(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(this), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawn(this), this);

        saveDefaultConfig();

        this.rogueMembers = new HashMap<>();
        this.permanentRogueMembers = new ArrayList<>();
        this.teams = new ArrayList<>();

        ConfigurationSection settingsSection = getConfig().getConfigurationSection("settings");

        cooldown_time = settingsSection.getInt("cooldown-time");
        hardcore_mode = settingsSection.getBoolean("hardcore");

        ConfigurationSection topTeamSection = getConfig().getConfigurationSection("team");

        Set<String> keys = topTeamSection.getKeys(false);

        for (String teamKey : keys) {
            ConfigurationSection currentTeamSection = topTeamSection.getConfigurationSection(teamKey);

            String name = currentTeamSection.getString("name");
            ChatColor color = ChatColor.getByChar(currentTeamSection.getString("color"));

            double x = currentTeamSection.getDouble("spawn-location.x");
            double y = currentTeamSection.getDouble("spawn-location.y");
            double z = currentTeamSection.getDouble("spawn-location.z");
            float pitch = currentTeamSection.getLong("spawn-location.pitch");
            float yaw = currentTeamSection.getLong("spawn-location.yaw");

            Location location = new Location(getServer().getWorlds().get(0), x, y, z, yaw, pitch);

            Team team = new Team(name, color, location);

            loop_counter++;
            teams.add(team);

            List<String> members = currentTeamSection.getStringList("members");

            Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

            org.bukkit.scoreboard.Team scoreboardTeam = scoreboard.getTeam(name);

            if (scoreboardTeam == null) {

                scoreboardTeam = scoreboard.registerNewTeam(team.getName() + ".scoreboard");

            }
            
            for (String member : members) {
                team.addPlayerToTeam(member);

                scoreboardTeam.addEntry(member);
                scoreboardTeam.setColor(color);
                scoreboardTeam.setDisplayName(color + member);
            }

        }

        loop_counter = 0;


        for (Team team : teams) {



            loop_counter++;
            ConfigurationSection currentTeamSection = topTeamSection.getConfigurationSection(team.getName());


            ConfigurationSection subTeamSection = currentTeamSection.getConfigurationSection("subteams");

            if (subTeamSection == null) continue;

            for (String subTeamName : subTeamSection.getKeys(false)) {

                SubTeam subTeam = new SubTeam(subTeamName, team.getColor());
                team.createSubTeam(subTeam);

                List<String> subTeamMembers = subTeamSection.getStringList(subTeamName);

                for (String m : subTeamMembers) {
                    Team mTeam = getPlayerTeam(m);

                    subTeam.addMember(mTeam.getTeamMember(m));
                }
            }
        }

        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (pl.getGameMode() == GameMode.SPECTATOR) {
                pl.setGameMode(GameMode.SURVIVAL);

            }

            Team team = getPlayerTeam(pl.getName());

            if (team == null) continue;

            team.clearPlayerPrefix(pl);

            pl.teleport(team.getSpawn());

            if (getPlayerSubTeam(pl.getName()) == null) continue;
            SubTeam subTeam = getPlayerSubTeam(pl.getName());

            team.setPlayerSubTeamPrefix(pl, subTeam);

        }

        runRogueTask();
    }

    @Override
    public void onDisable() {
        /*
        ConfigurationSection teamSection = getConfig().getConfigurationSection("team");


        for (String keys : teamSection.getKeys(false)) {
            ConfigurationSection teamNameSection = teamSection.getConfigurationSection(keys);
            teamNameSection.set("members", null);
            teamNameSection.set("subteams.name", null);
            teamNameSection.set("subteams.members", null);
        }

        for (String keys : teamSection.getKeys(false)) {
            ConfigurationSection teamNameSection = teamSection.getConfigurationSection(keys);

            for (Team team : teams) {

                if (team.getName().equals(teamNameSection.get("name"))) {

                    List<String> teamMemberList = new ArrayList<>();


                    for (TeamMember teamMember : team.getMembers()) {
                        teamMemberList.add(teamMember.getName());

                    }
                    teamNameSection.set("members", teamMemberList);

                    Map<String, List<String>> subTeamMap = new HashMap<>();
                    List<String> subTeamMemberList = new ArrayList<>();

                    for (SubTeam subTeam : team.getSubTeams()) {

                        for (TeamMember subTeamMember : subTeam.getSubTeamMembers()) {
                            subTeamMemberList.add(subTeamMember.getName());
                            subTeamMap.put(subTeam.getName(), subTeamMemberList);

                        }

                    }
                    teamNameSection.set("subteams", subTeamMap);
                }
            }
        }

        */
    }

    public void runRogueTask() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                Iterator<Map.Entry<String, Long>> iterator = rogueMembers.entrySet().iterator();

                while (iterator.hasNext()) {
                    Map.Entry<String, Long> entries = iterator.next();
                    String name = entries.getKey();
                    long timeInMillis = System.currentTimeMillis();
                    long timeInRogue = entries.getValue();


                    if (timeInRogue + ((cooldown_time * 60L) * 1000) <= timeInMillis) {
                        iterator.remove();
                        Player p = Bukkit.getPlayer(name);
                        Team t = getPlayerTeam(name);

                        if (p != null) {
                            t.clearPlayerPrefix(p);
                            p.sendMessage("Change cooldown has ended and you may now join subteams! You have " + t.getTeamMember(p.getName()).getTotalTeamChanges() + " changes remaining!");
                        }
                    }
                }
            }
        }, 0, 20L);
    }

    public void makeRogue(TeamMember member) {
        rogueMembers.put(member.getName(), System.currentTimeMillis());
    }

    public void makePermanentRogue(TeamMember member) {
        member.resetTotalTeamChanges();
        permanentRogueMembers.add(member.getName());
    }

    public void relseasePermanentRogue(TeamMember member) {
        permanentRogueMembers.remove(member.getName());
    }

    public void releaseRogue(TeamMember member) {
        rogueMembers.remove(member.getName());
    }

    public boolean isRogue(TeamMember member) {
        return rogueMembers.containsKey(member.getName());
    }

    public boolean isPermanentRogue(TeamMember member) {
        return permanentRogueMembers.contains(member.getName());
    }

    public long getCooldownTime(String playerName) {
        long currentTimeMillis = System.currentTimeMillis();
        int cooldownTimeInSeconds = cooldown_time * 60;
        long cooldownTimeSecondsToMillis = TimeUnit.SECONDS.toMillis(cooldownTimeInSeconds);

        for (String name : rogueMembers.keySet()) {
            if (name.equals(playerName)) {
                long storedTimeStamp = rogueMembers.get(playerName);
                long elapsedTime = currentTimeMillis - storedTimeStamp;

                if (elapsedTime <= cooldownTimeSecondsToMillis) {
                    return TimeUnit.MILLISECONDS.toSeconds(cooldownTimeSecondsToMillis - elapsedTime);
                }
            }
        }
        return 0L;
    }

    public Team getPlayerTeam(String playerName) {
        for (Team team : teams) {
            for (TeamMember member : team.getMembers()) {
                if (member.getName().equalsIgnoreCase(playerName)) {
                    return team;
                }
            }
        }
        return null;
    }

    public SubTeam getPlayerSubTeam(String playerName) {
        for (Team t : teams) {
            for (SubTeam subTeam : t.getSubTeams()) {
                for (TeamMember members : subTeam.getSubTeamMembers()) {
                    if (members.getName().equals(playerName)) {
                        return subTeam;
                    }
                }
            }
        }
        return null;
    }


    public SubTeam getSubTeam(String name) {
        for (Team team : teams) {
            for (SubTeam subTeam : team.getSubTeams()) {
                if (subTeam.getName().equals(name)) {
                    return subTeam;
                }
            }
        }
        return null;
    }

    public Team getTeam(String teamName) {
        for (Team team : teams) {
            if (team.getName().equalsIgnoreCase(teamName)) {
                return team;
            }
        }
        return null;
    }

    public List<Team> getTeams() {
        return teams;
    }


}

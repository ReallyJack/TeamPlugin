package me.jack.jteams;

import net.minecraft.util.TimeUtil;

import java.util.concurrent.TimeUnit;

public class TeamInvite {

    private String receiver;
    private long timer;

    public TeamInvite(String receiver) {
        this.receiver = receiver;
        this.timer = TimeUnit.SECONDS.toSeconds(10);
    }

    public String getReceiver() {
        return receiver;
    }

    public long getTimer() {
        return timer;
    }
}

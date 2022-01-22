package de.snap20lp.antispeed;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class PlayerFlagEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private int flags;
    private boolean isCancelled;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public PlayerFlagEvent(Player player,int flags) {
        this.player = player;
        this.flags = flags;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public int getBeforeFlags() {
        return flags;
    }
    public int getCurrentFlags() {
        return flags+=1;
    }
    public Player getPlayer() {
        return player;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}

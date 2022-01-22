package de.snap20lp.antispeed;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerSetBackEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private boolean isCancelled;

    public boolean isCancelled() {
        return isCancelled;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public PlayerSetBackEvent(Player player) {
        this.player = player;
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

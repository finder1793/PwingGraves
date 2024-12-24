package com.pwing.graves.api.events;

import com.pwing.graves.respawn.RespawnPoint;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RespawnPointCreateEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player creator;
    private final RespawnPoint point;
    private boolean cancelled;

    public RespawnPointCreateEvent(Player creator, RespawnPoint point) {
        this.creator = creator;
        this.point = point;
    }

    public Player getCreator() {
        return creator;
    }

    public RespawnPoint getPoint() {
        return point;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

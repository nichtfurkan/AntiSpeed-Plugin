package de.snap20lp.antispeed;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.yaml.snakeyaml.tokens.BlockEndToken;

public class PlayerThread implements Runnable, Listener {

    private final Thread thread;
    private final Player player;
    private Location lastPlayerLocation;
    private int misses = 0, missTimer,checkTimer;
    private boolean isRunning = false;

    public Player getPlayer() {
        return player;
    }

    public PlayerThread(Player player) {
        this.thread = new Thread(this, "PlayerThread");
        this.player = player;
        this.thread.start();
        Bukkit.getConsoleSender().sendMessage("§e["+player.getName()+"] Started new Thread!");
    }

    private void startMissTimer() {
        isRunning = true;
        missTimer = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {
            if (misses == 0) {
                Bukkit.getScheduler().cancelTask(missTimer);
                isRunning = false;
            } else {
                misses -= 1;
            }
        }, 20 * 4, 20 * 6);
    }

    public void stopThread() {
        if(isRunning) {
            Bukkit.getScheduler().cancelTask(missTimer);
        }
        Bukkit.getScheduler().cancelTask(checkTimer);
        this.thread.interrupt();
    }

    @Override
    public void run() {
        checkTimer = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {
            if (lastPlayerLocation != null && checkIfPlayerIsValid(player)) {
                if (checkPlayerFailing()) {
                    PlayerFlagEvent playerFlagEvent = new PlayerFlagEvent(player,misses);
                    Bukkit.getPluginManager().callEvent(playerFlagEvent);
                    if(!playerFlagEvent.isCancelled()) {
                        misses++;
                        Bukkit.getConsoleSender().sendMessage("§c["+player.getName()+"] got Flagged");
                        if (misses >= 6) {
                            PlayerKickCheatingEvent playerKickCheatingEvent = new PlayerKickCheatingEvent(player);
                            Bukkit.getPluginManager().callEvent(playerKickCheatingEvent);
                            if(!playerKickCheatingEvent.isCancelled()) {
                                Bukkit.getConsoleSender().sendMessage("§c[" + player.getName() + "] got Kicked for Cheating!");
                                player.kickPlayer("§cPlease stop cheating!");
                            }
                        }
                        if (misses >= 3) {
                            PlayerSetBackEvent playerSetBackEvent = new PlayerSetBackEvent(player);
                            Bukkit.getPluginManager().callEvent(playerSetBackEvent);
                            if(!playerSetBackEvent.isCancelled()) {
                                player.teleport(lastPlayerLocation);
                            }
                        }
                    }
                    if (!isRunning) {
                        startMissTimer();
                    }
                }
                lastPlayerLocation = null;
            } else {
                lastPlayerLocation = player.getLocation();
            }
        }, 20, 20);
    }


    private boolean checkPlayerFailing() {
        double distance = lastPlayerLocation.distance(player.getLocation());
        if (distance < 7.37 && !containsPotionEffect(PotionEffectType.SPEED)) {
            return false;
        } else if (distance < 7.79 && containsPotionEffectWithLevel(PotionEffectType.SPEED, 0)) {
            return false;
        } else if (distance < 7.9 && containsPotionEffectWithLevel(PotionEffectType.SPEED, 1)) {
            return false;
        } else if (distance < 9.6 && getBlockUnder(Material.ICE,4) || distance < 9.6 && getBlockUnder(Material.PACKED_ICE,4)) {
            return false;
        } else if(lastPlayerLocation.getBlockY() > player.getLocation().getBlockY()+3) {
            return false;
        }
        return true;
    }

    private boolean getBlockUnder(Material material,int limit) {
        for(int i = 0; i < limit; i++) {
            if(player.getLocation().subtract(0,i,0).getBlock().getType().equals(material)) {
                return true;
            }
        }
        return false;
    }



    private boolean containsPotionEffect(PotionEffectType type) {
        boolean contains = false;
        for (PotionEffect activePotionEffect : player.getActivePotionEffects()) {
            contains = activePotionEffect.getType().equals(type);
        }
        return contains;
    }

    private boolean containsPotionEffectWithLevel(PotionEffectType type, int level) {
        boolean contains = false;
        for (PotionEffect activePotionEffect : player.getActivePotionEffects()) {
            if (activePotionEffect.getType().equals(type) && activePotionEffect.getAmplifier() == level) {
                contains = true;
            }
        }
        return contains;
    }

    private boolean checkIfPlayerIsValid(Player player) {
        return player.getGameMode() != GameMode.CREATIVE && !player.isOp() && !player.hasPermission("antispeed.bypass");
    }
}

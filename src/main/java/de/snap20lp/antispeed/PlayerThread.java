package de.snap20lp.antispeed;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerThread implements Runnable {

    private final Thread thread;
    private final Player player;
    private Location lastPlayerLocation;
    private int misses = 0, missTimer;
    private boolean isRunning = false;


    public PlayerThread(Player player) {
        this.thread = new Thread(this, "PlayerThread");
        this.player = player;
        this.thread.start();
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

    @Override
    public void run() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {
            if (lastPlayerLocation != null && checkIfPlayerIsValid(player)) {
                if (checkPlayerFailing()) {
                    misses++;
                    if (misses >= 6) {
                        player.kickPlayer("§cPlease stop cheating!");
                    }
                    if (misses >= 3) {
                        player.teleport(lastPlayerLocation.add(0, 1, 0));
                        player.damage(0);
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
        } else return lastPlayerLocation.getBlockY() <= player.getLocation().getBlockY() + 3;
    }

    private boolean containsPotionEffect(PotionEffectType type) {
        boolean contains = false;
        for (PotionEffect activePotionEffect : player.getActivePotionEffects()) {
            contains = activePotionEffect.getType().equals(type);
            System.out.println(contains);
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
        return player.getGameMode() != GameMode.CREATIVE && player.isOp() && player.hasPermission("antispeed.bypass");
    }
}

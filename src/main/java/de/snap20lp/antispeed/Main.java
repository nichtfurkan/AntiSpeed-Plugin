package de.snap20lp.antispeed;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class Main extends JavaPlugin implements Listener {

    private final ArrayList<PlayerThread> playerThreads = new ArrayList<>();

    public static Main getInstance() {
        return Main.getPlugin(Main.class);
    }

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("Started AntiSpeed-1.0");
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getOnlinePlayers().forEach(element -> playerThreads.add(new PlayerThread(element.getPlayer())));
    }

    @Override
    public void onDisable() {
        playerThreads.forEach(element -> element.getThread().interrupt());
        playerThreads.clear();
        System.gc();
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {
        playerThreads.forEach(element -> {
            if (element.getPlayer().getUniqueId().equals(event.getPlayer().getUniqueId())) {
                element.getThread().interrupt();
            }
        });
    }

    @EventHandler
    public void on(PlayerJoinEvent event) {
        playerThreads.add(new PlayerThread(event.getPlayer()));
    }

}

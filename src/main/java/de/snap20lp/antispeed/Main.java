package de.snap20lp.antispeed;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    public static Main getInstance() {
        return Main.getPlugin(Main.class);
    }

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("Started AntiSpeed-1.0");
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void on(PlayerJoinEvent event) {
        new PlayerThread(event.getPlayer());
    }

}

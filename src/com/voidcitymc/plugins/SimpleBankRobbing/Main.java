package com.voidcitymc.plugins.SimpleBankRobbing;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	private static Main instance;
	public static Main getInstance() {
		return instance;
	}
	
    // Fired when plugin is first enabled
    @Override
    public void onEnable() {
    	instance = this;
    	this.getServer().getPluginManager().registerEvents((Listener)new InteractEvent(), (Plugin)this);
    	
        Metrics metrics = new Metrics(this, 8362);
        this.getConfig().options().copyDefaults(true);
    	saveConfig();
    	
    }
    // Fired when plugin is disabled
    @Override
    public void onDisable() {
    }
    
    
}

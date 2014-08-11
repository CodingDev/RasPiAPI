package net.renepreuss.RasPiAPI.API;

import java.util.Properties;

import net.renepreuss.RasPiAPI.PluginManager;
import net.renepreuss.RasPiAPI.RasPiAPI;
import net.renepreuss.RasPiAPI.Board.RaspberryPi;

public abstract class RasPiPlugin extends Plugin{
	
	private PluginLogger pluginLogger;
	private RaspberryPi raspberryPi;
	private PluginManager pluginManager;

	public void onEnable(){
		getLogger().info("Plugin was enabled by default method.");
	}
	
	public void onDisable(){
		getLogger().info("Plugin was disabled by default method.");
	}
	

	public void initialize(RaspberryPi raspberryPi, PluginManager pluginManager, PluginLogger pluginLogger) {
		this.pluginLogger = pluginLogger;
		this.raspberryPi = raspberryPi;
		this.pluginManager = pluginManager;
	}
	
	public RaspberryPi getRaspberryPi(){
		return raspberryPi;
	}
	
	public PluginConfig getConfig(){
		return null;
	}
	
	public PluginLogger getLogger(){
		return pluginLogger;
	}
	
	public PluginManager getPluginManager(){
		return pluginManager;
	}
}

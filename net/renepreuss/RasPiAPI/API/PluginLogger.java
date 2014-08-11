package net.renepreuss.RasPiAPI.API;

public class PluginLogger {
	Plugin plugin;
	
	public PluginLogger(Plugin plugin) {
		this.plugin = plugin;
	}

	public void info(String message){
		System.out.println("[RasPiAPI] [" + plugin.getName() + "] [INFO] " + message);
	}
	
	
}

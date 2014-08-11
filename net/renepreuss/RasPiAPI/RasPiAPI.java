package net.renepreuss.RasPiAPI;

import net.renepreuss.RasPiAPI.Board.RaspberryPi;

public class RasPiAPI {
	private static RaspberryPi board;
	private static PluginManager pluginManager;
	private static SystemLogger systemLogger;
	
	public static void main(String args[]) throws InterruptedException{
		board = new RaspberryPi();
		pluginManager = new PluginManager();
		systemLogger = new SystemLogger();
		
		
		pluginManager.loadRasPiPlugins();
		
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		t.start();
	}

	public static PluginManager getPluginManager(){
		return pluginManager;
	}
	
	public void getConfig(){
		
	}

	public static SystemLogger getLogger() {
		return systemLogger;
	}

	public static boolean isRunning() {
		return true;
	}

	public static RaspberryPi getRaspberryPi() {
		return board;
	}
	
}

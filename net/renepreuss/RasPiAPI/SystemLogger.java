package net.renepreuss.RasPiAPI;

public class SystemLogger {
	public void info(String message){
		System.out.println("[RasPiAPI] [SYSTEM] [INFO] " + message);
	}
	
	public void warning(String message){
		System.out.println("[RasPiAPI] [SYSTEM] [WARNING] " + message);
	}
}

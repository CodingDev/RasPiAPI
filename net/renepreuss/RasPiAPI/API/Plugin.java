package net.renepreuss.RasPiAPI.API;

import java.util.Properties;

public class Plugin {
	private Properties props;
	private boolean enabled;

	public String[] getAuthors(){
		String[] authors = getPluginDescription().getProperty("authors").split(";");
		return authors;
	}
	
	public Plugin getPlugin(){
		return this;
	}
	
	public String getName(){
		return props.getProperty("name");
	}

	public String getVersion() {
		return props.getProperty("version");
	}

	public void setEnabled(boolean b) {
		this.enabled = b;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public String getAuthor() {
		return getAuthors()[0];
	}
	
	public void setPluginDescription(Properties props) {
		this.props = props;
	}
	
	public Properties getPluginDescription(){
		return props;
	}
}

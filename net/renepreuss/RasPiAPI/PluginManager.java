package net.renepreuss.RasPiAPI;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import net.renepreuss.RasPiAPI.API.RasPiPlugin;
import net.renepreuss.RasPiAPI.API.PluginLogger;

public class PluginManager {
	private List<RasPiPlugin> loadedPlugins = new ArrayList<RasPiPlugin>();
	private List<RasPiPlugin> pending = new ArrayList<RasPiPlugin>();
	private Map<String, URL> jarMap = new HashMap<String, URL>();

	public void loadRasPiPlugins() {
		File RasPiPluginDir = new File("plugins");

		if (!RasPiPluginDir.exists()) {
			RasPiPluginDir.mkdirs();
			return;
		}
		
		for (File file : RasPiPluginDir.listFiles()) {
			if (!file.isDirectory() && !file.getName().startsWith(".")) {
				try {
					loadRasPiPlugin(file);
				} catch (PluginException e) {
					RasPiAPI.getLogger().warning(e.getMessage());
				}
			}
		}
		if (RasPiAPI.isRunning()) {
			for (RasPiPlugin p : pending) {
				try {
					enableRasPiPlugin(p);
				} catch (Exception e) {
					RasPiAPI.getLogger().warning("Error initializing plugin " + p.getName());
					e.printStackTrace();
				}
			}
		}
		RasPiAPI.getLogger().info("Loaded " + loadedPlugins.size() + " plugins!");
	}

	public void unloadRasPiPlugins() {
		for (RasPiPlugin RasPiPlugin : loadedPlugins) {
			RasPiAPI.getLogger().warning("Disabling " + RasPiPlugin.getName() + " v" + RasPiPlugin.getVersion());
			disableRasPiPlugin(RasPiPlugin);
		}
	}

	public void loadRasPiPlugin(File file) throws PluginException {
		if (file == null)
			throw new PluginException("RasPiPlugin cannot be null!");

		RasPiPlugin RasPiPlugin = null;
		try {
			RasPiPlugin = getRasPiPlugin(file);
		} catch (Exception e) {
			RasPiAPI.getLogger().warning("Failed to load file " + file.getName() + "! " + e.getMessage());
			e.printStackTrace();
			return;
		}

		RasPiPlugin.initialize(RasPiAPI.getRaspberryPi(), this, new PluginLogger(RasPiPlugin));
		StringBuffer sb = new StringBuffer();
		try {
			jarMap.put(RasPiPlugin.getName(), file.toURI().toURL());
		} catch (MalformedURLException e) {
			RasPiAPI.getLogger().warning("failed to load plugin " + RasPiPlugin.getName() + "! " + e.getMessage());
			e.printStackTrace();
			return;
		}
		sb.append("Loading " + RasPiPlugin.getName() + " v" + RasPiPlugin.getVersion());
		if (RasPiPlugin.getAuthor() != null) {
			sb.append(" by " + RasPiPlugin.getAuthor());
		}
		RasPiAPI.getLogger().info(sb.toString());
		pending.add(RasPiPlugin);
	}

	@SuppressWarnings("unchecked")
	private RasPiPlugin getRasPiPlugin(File file) throws PluginException, NoSuchMethodException, SecurityException, IOException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Properties props = new Properties();
		if (!file.getName().endsWith(".jar"))
			throw new PluginException("File must be a jar file!");
		JarFile jFile = new JarFile(file.getPath());
		URL[] urls = {file.toURI().toURL()};
		URLClassLoader cl = new URLClassLoader(urls, getClass().getClassLoader());
		Enumeration<JarEntry> e = jFile.entries();
		Class<? extends RasPiPlugin> clazz = null;
		try {
			InputStream is = cl.getResourceAsStream("plugin.properties");
			props.load(is);
			String main = props.getProperty("main");
			try {
				Class<?> c = cl.loadClass(main);
				if (c.getSuperclass() == RasPiPlugin.class) {
					clazz = (Class<? extends RasPiPlugin>) c;
					while (e.hasMoreElements()) {
						JarEntry entry = e.nextElement();
						if (entry.isDirectory() || !entry.getName().endsWith(".class"))
							continue;
						String className = entry.getName().substring(0, entry.getName().length() - 6);
						className = className.replace('/', '.');
						try {
							cl.loadClass(className);
						} catch (Exception ex) {
							RasPiAPI.getLogger().warning("Error loading RasPiPlugin class from " + className);
						}
					}
				} else {
					throw new PluginException("File plugin.properties main class for " + file.getName() + " is invalid!");
				}
			} catch (Exception ex) {
				RasPiAPI.getLogger().warning(ex.getMessage());
			}
		} catch (Exception ex) {
			while (e.hasMoreElements()) {
				JarEntry entry = e.nextElement();
				if (entry.isDirectory() || !entry.getName().endsWith(".class"))
					continue;
				String className = entry.getName().substring(0, entry.getName().length() - 6);
				className = className.replace('/', '.');
				try {
					Class<?> c = cl.loadClass(className);
					if (c.getSuperclass() == RasPiPlugin.class) {
						clazz = (Class<? extends RasPiPlugin>) c;
					}
				} catch (Exception e1) {
					RasPiAPI.getLogger().warning("Error loading RasPiPlugin class from " + className);
				}
			}
		}
		jFile.close();
		cl.close();
		if (clazz == null)
			throw new PluginException("RasPiPlugin " + file.getName().replace(".jar", "") + " does not contain a main class!");
		Constructor<? extends RasPiPlugin> constructor = clazz.asSubclass(RasPiPlugin.class).getConstructor();
		RasPiPlugin RasPiPlugin = constructor.newInstance();
		RasPiPlugin.setPluginDescription(props);
		return RasPiPlugin;
	}

	public List<RasPiPlugin> getRasPiPlugins() {
		return loadedPlugins;
	}

	public void enableRasPiPlugin(RasPiPlugin RasPiPlugin) {
		try {
			RasPiAPI.getLogger().info("Enable Plugin...");
			RasPiPlugin.setEnabled(true);
			RasPiPlugin.onEnable();
		} catch (Exception e) {

		}
		loadedPlugins.add(RasPiPlugin);
	}

	public void disableRasPiPlugin(RasPiPlugin RasPiPlugin) {
		try {
			RasPiPlugin.setEnabled(false);
			RasPiPlugin.onDisable();
		} catch (Exception e) {
			e.printStackTrace();
		}
		loadedPlugins.remove(RasPiPlugin);
	}

	public URLClassLoader getClassLoader(RasPiPlugin RasPiPlugin) {
		URLClassLoader cl = new URLClassLoader(new URL[]{jarMap.get(RasPiPlugin.getName())});
		return cl;
	}
}

package com.calclab.emite.client.plugin;

public interface PluginManager {
	public void install(String name, Plugin plugin);

	public void uninstall(String name);
}

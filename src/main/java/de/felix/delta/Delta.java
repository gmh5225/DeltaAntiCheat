package de.felix.delta;

import cc.funkemunky.api.utils.Color;
import cc.funkemunky.api.utils.MiscUtils;
import de.felix.delta.check.CheckManager;
import de.felix.delta.data.DataManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Delta extends JavaPlugin {
    private DeltaPlugin deltaPlugin;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        deltaPlugin = DeltaPlugin.getInstance();
        deltaPlugin.checkManager = new CheckManager(getConfig());
        deltaPlugin.dataManager = new DataManager();
        deltaPlugin.enable(this);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        deltaPlugin.disable(this);
        super.onDisable();
    }
}

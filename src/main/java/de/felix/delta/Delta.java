package de.felix.delta;

import cc.funkemunky.api.utils.Color;
import cc.funkemunky.api.utils.MiscUtils;
import de.felix.delta.data.DataManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Delta extends JavaPlugin {
    private DeltaPlugin deltaPlugin;

    @Override
    public void onEnable() {
        deltaPlugin = DeltaPlugin.getInstance();
        deltaPlugin.dataManager = new DataManager();
        MiscUtils.printToConsole(Color.Gray + "(" + Color.Pink + Color.Strikethrough + "DataManagerAutism Delta" + Color.Gray + ") " + Color.Green + "Initialized DataManager! " + deltaPlugin.dataManager);
        deltaPlugin.enable(this);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        deltaPlugin.disable(this);
        super.onDisable();
    }
}

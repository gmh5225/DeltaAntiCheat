package de.felix.delta;

import cc.funkemunky.api.Atlas;
import cc.funkemunky.api.utils.Color;
import cc.funkemunky.api.utils.MiscUtils;
import de.felix.delta.check.CheckManager;
import de.felix.delta.command.DeltaCommand;
import de.felix.delta.data.DataManager;
import de.felix.delta.listener.DataListener;
import de.felix.delta.listener.PacketListenerDelta;
import de.felix.delta.listener.bukkit.ConnectionListener;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

public class DeltaPlugin {

    public static DeltaPlugin instance;
    public final String antiCheat_name = "Delta";
    public DeltaCommand deltaCommand;
    public DataManager dataManager;
    public DataListener dataListener;

    public CheckManager checkManager;

    public Delta deltaInstance;

    public PacketListenerDelta packetListenerDelta;

    public static DeltaPlugin getInstance() {
        if (instance == null) {
            instance = new DeltaPlugin();
        }
        return instance;
    }


    public void enable(Delta delta) {
        this.deltaInstance = delta;
        MiscUtils.printToConsole(Color.Gray + "(" + Color.Dark_Gray + antiCheat_name + Color.Gray + ") " + Color.Green + "Enabled!");
        this.deltaCommand = new DeltaCommand();
        delta.getCommand("Delta").setExecutor(this.deltaCommand);
        Atlas.getInstance().initializeScanner(delta.getClass(), delta);
        delta.getServer().getPluginManager().registerEvents(new DataListener(), delta);
        delta.getServer().getPluginManager().registerEvents(new ConnectionListener(), delta);
        this.dataListener = new DataListener();
        this.packetListenerDelta = new PacketListenerDelta();
    }

    public void disable(Delta delta) {
        MiscUtils.printToConsole(Color.Gray + "(" + Color.Dark_Gray + antiCheat_name + Color.Gray + ") " + Color.Red + "Disabled!");
        HandlerList.unregisterAll(delta);
        Bukkit.getScheduler().cancelTasks(delta);
    }

}
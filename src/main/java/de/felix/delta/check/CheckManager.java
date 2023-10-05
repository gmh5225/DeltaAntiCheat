package de.felix.delta.check;

import de.felix.delta.data.DataHolder;
import org.bukkit.configuration.file.FileConfiguration;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Stream;

public class CheckManager {
    public ArrayList<Check> checks = new ArrayList<>();
    private final FileConfiguration configuration;
    public CheckManager(final FileConfiguration configuration) {
        this.configuration = configuration;
    }

    public void addChecks(final DataHolder dataHolder) {
        if (dataHolder.addedChecks.isEmpty()) {
            final Reflections reflections = new Reflections("de.felix.delta.check.list");
            final Set<Class<? extends Check>> classes = reflections.getSubTypesOf(Check.class);
            final Stream<Class<? extends Check>> stream = classes.stream();

            stream.forEach(aClass -> {
                try {
                    Check check = aClass.getConstructor(DataHolder.class).newInstance(dataHolder);
                    if (dataHolder.addCheck(check)) {
                        checks.add(check);
                    }
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public int getValueInt(CheckType check, String valueName) {
        return configuration.getConfigurationSection(check.name().toLowerCase()).getInt(valueName);
    }

    public double getValueDouble(CheckType check, String valueName) {
        System.out.println(configuration.getConfigurationSection(check.name().toLowerCase()).getDouble(valueName) + " " + valueName);
        return configuration.getConfigurationSection(check.name().toLowerCase()).getDouble(valueName);
    }

    public Set<Check> getChecks(DataHolder dataHolder) {
        return dataHolder.addedChecks;
    }

}

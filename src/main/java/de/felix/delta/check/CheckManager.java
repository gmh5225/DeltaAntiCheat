package de.felix.delta.check;

import de.felix.delta.data.DataHolder;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Stream;

public class CheckManager {
    public ArrayList<Check> checks = new ArrayList<>();

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
                        System.out.println("Added check " + aClass.getSimpleName() + " to " + dataHolder.player.getName());
                    }
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public Set<Check> getChecks(DataHolder dataHolder) {
        return dataHolder.addedChecks;
    }

}

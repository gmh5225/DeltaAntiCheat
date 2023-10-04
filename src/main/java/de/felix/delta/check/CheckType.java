package de.felix.delta.check;

public enum CheckType {

    COMBAT("Combat"), MOVEMENT("Movement"), ROTATION("Rotation"), OTHER("Other");

    final String checkName;

    CheckType(String checkName) {
        this.checkName = checkName;
    }
}

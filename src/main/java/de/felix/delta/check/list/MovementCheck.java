package de.felix.delta.check.list;

import de.felix.delta.check.Check;
import de.felix.delta.check.CheckInfo;
import de.felix.delta.check.CheckType;
import de.felix.delta.check.alert.TagBuilder;
import de.felix.delta.data.DataHolder;
import net.minecraft.server.v1_8_R3.Packet;

@CheckInfo(checkName = "Movement", checkType = CheckType.MOVEMENT, punishable = false)
public class MovementCheck extends Check {

    private long tickTime;

    public MovementCheck(DataHolder dataHolder) {
        super(dataHolder);
    }


    //Akward check just for testing purposes
    @Override
    protected void handle(Packet<?> packet) {
        final TagBuilder emptyTagBuilder = new TagBuilder();
        if (getDataHolder().movementData.movementStorage.getCurrentPosition().getY() == getDataHolder().movementData.movementStorage.getPointBehindTick(0, false).getY()) {
            if (tickTime == 0) {
                tickTime = System.currentTimeMillis();
            } else {
                if (System.currentTimeMillis() - tickTime > 1000) {
                    getAlertBuilder().setAlertMessage("Long time no see").runAlert(emptyTagBuilder);
                }
            }
        } else {
            tickTime = 0;
        }
    }
}

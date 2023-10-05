package de.felix.delta.data.datas;

import de.felix.delta.data.RegistrableDataHolder;
import de.felix.delta.util.MovementStorage;
import org.bukkit.entity.Entity;
import java.util.UUID;

public class EnemyData extends RegistrableDataHolder {
    private long lastAttackTime;
    public Entity enemy;
    public final MovementStorage movementStorage = new MovementStorage(); //Safes the lasts positions of the enemy
    private static final long resetTime = 300L; //Time in ms until the enemy is reset

    public EnemyData(UUID holder) {
        super(holder);
    }

    public void processHit(final Entity entity) {
        lastAttackTime = System.currentTimeMillis();
        this.enemy = entity;
    }

    public void process() {
        if (enemy == null) {
            movementStorage.getPointHistory().clear();
            movementStorage.setCurrentPosition(null);
            movementStorage.setLastPosition(null);
        }
        if (enemy == null) return;

      //  movementStorage.setLastPosition(movementStorage.getCurrentPosition());
       // movementStorage.addMovementPoint(enemy.getLocation().toVector());
        if (lastAttackTime + resetTime < System.currentTimeMillis())
            enemy = null;
    }

    private long tillLastAttack() {
        return System.currentTimeMillis() - lastAttackTime;
    }
}

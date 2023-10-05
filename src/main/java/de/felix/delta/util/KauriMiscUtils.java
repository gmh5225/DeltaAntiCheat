//https://github.com/funkemunky/Kauri/blob/master/Impl/src/main/java/dev/brighten/anticheat/utils/MiscUtils.java#L81
package de.felix.delta.util;

import cc.funkemunky.api.utils.BlockUtils;
import cc.funkemunky.api.utils.Materials;
import cc.funkemunky.api.utils.world.types.SimpleCollisionBox;
import net.minecraft.server.v1_8_R3.MathHelper;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Optional;

public class KauriMiscUtils {

    public static boolean isInMaterialBB(World world, SimpleCollisionBox entityBox, int bitmask) {
        final int startX = MathHelper.floor(entityBox.xMin);
        final int startY = MathHelper.floor(entityBox.yMin);
        final int startZ = MathHelper.floor(entityBox.zMin);
        final int endX = MathHelper.floor(entityBox.xMax + 1D);
        final int endY = MathHelper.floor(entityBox.yMax + 1D);
        final int endZ = MathHelper.floor(entityBox.zMax + 1D);

        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                for (int z = startZ; z < endZ; z++) {
                    final Location loc = new Location(world, x, y, z);
                    final Optional<org.bukkit.block.Block> op = BlockUtils.getBlockAsync(loc);

                    if (op.isPresent()) {
                        if (Materials.checkFlag(op.get().getType(), bitmask))
                            return true;
                    }
                }
            }
        }
        return false;
    }
}

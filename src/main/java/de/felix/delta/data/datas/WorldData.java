//https://github.com/funkemunky/Kauri/blob/master/Impl/src/main/java/dev/brighten/anticheat/data/classes/BlockInformation.java
package de.felix.delta.data.datas;

import cc.funkemunky.api.tinyprotocol.api.ProtocolVersion;
import cc.funkemunky.api.utils.*;
import cc.funkemunky.api.utils.world.BlockData;
import cc.funkemunky.api.utils.world.CollisionBox;
import cc.funkemunky.api.utils.world.types.SimpleCollisionBox;
import de.felix.delta.DeltaPlugin;
import de.felix.delta.data.DataHolder;
import de.felix.delta.data.RegistrableDataHolder;
import de.felix.delta.util.KauriMiscUtils;
import net.minecraft.server.v1_8_R3.MathHelper;
import org.bukkit.*;
import org.bukkit.block.Block;

import java.util.*;

public class WorldData extends RegistrableDataHolder {
    private final UUID uuid;

    private int startX, startY, startZ, endX, endY, endZ;

    public boolean onClimbable, onSlab, onStairs, onHalfBlock, inLiquid, inLava, inWater, inWeb, onSlime, onIce,
            onSoulSand, blocksAbove, collidesVertically, bedNear, collidesHorizontally, blocksNear, inBlock, miscNear,
            collidedWithEntity, roseBush, inPortal, blocksBelow, fenceNear,
            pistonNear, fenceBelow, inScaffolding, inHoney, serverGround, nearGround, worldLoaded, lServerGround;
    public float currentFriction, fromFriction;


    public final List<SimpleCollisionBox> aboveCollisions = Collections.synchronizedList(new ArrayList<>()),
            belowCollisions = Collections.synchronizedList(new ArrayList<>());

    public final List<Block> blocks = Collections.synchronizedList(new ArrayList<>());
    private static final EnumMap<Material, XMaterial> matchMaterial = new EnumMap<>(Material.class);

    private final Material cobweb = XMaterial.COBWEB.parseMaterial(),
            rosebush = XMaterial.ROSE_BUSH.parseMaterial(),
            scaffolding = XMaterial.SCAFFOLDING.parseMaterial(),
            honey = XMaterial.HONEY_BLOCK.parseMaterial();

    static {
        for (Material mat : Material.values()) {
            matchMaterial.put(mat, XMaterial.matchXMaterial(mat));
        }
    }

    public static XMaterial getXMaterial(Material material) {
        return matchMaterial.getOrDefault(material, null);
    }

    public WorldData(UUID holder) {
        super(holder);
        this.uuid = holder;
    }

    public void runCollisionCheck() {
        final DataHolder dataHolder = DeltaPlugin.getInstance().dataManager.getDataHolder(Bukkit.getPlayer(uuid));

        if (dataHolder.movementData.movementStorage.getLastPosition() == null) return;

        blocks.clear();
        final SimpleCollisionBox waterBox = dataHolder.box.copy().expand(0, -.38, 0);

        final SimpleCollisionBox lavaBox = dataHolder.box.copy().expand(-.1f, -.4f, -.1f);

        final double verticalMovement = dataHolder.getMovementData().getMovementStorage().getCurrentPosition().getY() - dataHolder.getMovementData().getMovementStorage().getLastPosition().getY();

        final double horizontalMovement = MathUtils.hypot(dataHolder.getMovementData().getMovementStorage().getCurrentPosition().getX() - dataHolder.getMovementData().getMovementStorage().getLastPosition().getX(), dataHolder.getMovementData().getMovementStorage().getCurrentPosition().getZ() - dataHolder.getMovementData().getMovementStorage().getLastPosition().getZ());

        resetFlags();

        limitMovements(verticalMovement, horizontalMovement);

        calculateCollisionBoxes(dataHolder, verticalMovement, horizontalMovement);

        checkLiquid(dataHolder, waterBox, lavaBox);

        final SimpleCollisionBox normalBox = dataHolder.box.copy();

        inLiquid = inWater || inLava;

        worldLoaded = true;
        lServerGround = serverGround;
        synchronized (belowCollisions) {
            belowCollisions.clear();
        }
        synchronized (aboveCollisions) {
            aboveCollisions.clear();
        }
        final World world = dataHolder.player.getWorld();
        int it = 9 * 9;
        start:
        for (int chunkx = startX >> 4; chunkx <= endX >> 4; ++chunkx) {
            int cx = chunkx << 4;

            for (int chunkz = startZ >> 4; chunkz <= endZ >> 4; ++chunkz) {
                if (!world.isChunkLoaded(chunkx, chunkz)) {
                    worldLoaded = false;
                    continue;
                }
                Chunk chunk = world.getChunkAt(chunkx, chunkz);
                if (chunk != null) {
                    int cz = chunkz << 4;
                    int xstart = Math.max(startX, cx);
                    int xend = Math.min(endX, cx + 16);
                    int zstart = Math.max(startZ, cz);
                    int zend = Math.min(endZ, cz + 16);

                    for (int x = xstart; x <= xend; ++x) {
                        for (int z = zstart; z <= zend; ++z) {
                            for (int y = Math.max(-50, startY); y <= endY; ++y) {
                                if (it-- <= 0) {
                                    break start;
                                }
                                if (y > 400 || y < -50) continue;
                                Block block = chunk.getBlock(x & 15, y, z & 15);
                                final Material type = block.getType();
                                if (type != Material.AIR) {
                                    blocks.add(block);

                                    CollisionBox blockBox = BlockData.getData(type)
                                            .getBox(block, dataHolder.playerVersion);

                                    if (blockBox.isCollided(normalBox)) {
                                        if (type.equals(cobweb))
                                            inWeb = true;
                                        else if (type.equals(scaffolding)) inScaffolding = true;
                                        else if (type.equals(honey)) inHoney = true;
                                    }

                                    if (type.equals(rosebush))
                                        roseBush = true;

                                    if (normalBox.copy().offset(0, 0.6f, 0).isCollided(blockBox))
                                        blocksAbove = true;

                                    if (normalBox.copy().expand(1, -0.0001, 1).isIntersected(blockBox))
                                        blocksNear = true;

                                    if (normalBox.copy().expand(0.1, 0, 0.1)
                                            .offset(0, 1, 0).isCollided(blockBox)) {
                                        synchronized (aboveCollisions) {
                                            blockBox.downCast(aboveCollisions);
                                        }
                                    }

                                    if (normalBox.copy().expand(0.1, 0, 0.1).offset(0, -1, 0)
                                            .isCollided(blockBox)) {
                                        synchronized (belowCollisions) {
                                            blockBox.downCast(belowCollisions);
                                        }

                                        if (Materials.checkFlag(type, Materials.FENCE)
                                                || Materials.checkFlag(type, Materials.WALL)) {
                                            fenceBelow = true;
                                        }
                                    }

                                    if (normalBox.copy().expand(0.25, 0, 0.25).offset(0, -0.8, 0).isCollided(blockBox) && (Materials.checkFlag(type, Materials.FENCE) || Materials.checkFlag(type, Materials.WALL)))
                                        fenceNear = true;


                                    if (Materials.checkFlag(type, Materials.SOLID)) {
                                        final SimpleCollisionBox groundBox = normalBox.copy().offset(0, -.49, 0).expandMax(0, -1.2, 0);

                                        final XMaterial blockMaterial = getXMaterial(type);

                                        if (normalBox.copy().expand(0.4, 0, 0.4).expandMin(0, -1, 0).isIntersected(blockBox))
                                            blocksBelow = true;

                                        if (normalBox.isIntersected(blockBox)) inBlock = true;

                                        SimpleCollisionBox box = dataHolder.box.copy();

                                        box.expand(Math.abs(dataHolder.movementData.movementStorage.getCurrentPosition().getX() - dataHolder.movementData.movementStorage.getLastPosition().getX()) + 0.1, -0.001, Math.abs(dataHolder.movementData.movementStorage.getCurrentPosition().getZ() - dataHolder.movementData.movementStorage.getLastPosition().getZ()) + 0.1);
                                        if (blockBox.isCollided(box))
                                            collidesHorizontally = true;

                                        box = dataHolder.box.copy();
                                        box.expand(0, 0.1, 0);

                                        if (blockBox.isCollided(box))
                                            collidesVertically = true;

                                        if (groundBox.copy().expandMin(0, -0.8, 0).expand(0.2, 0, 0.2).isIntersected(blockBox))
                                            nearGround = true;

                                        if (groundBox.isCollided(blockBox)) {
                                            serverGround = true;

                                            if (blockMaterial != null)
                                                switch (blockMaterial) {
                                                    case ICE:
                                                    case BLUE_ICE:
                                                    case FROSTED_ICE:
                                                    case PACKED_ICE: {
                                                        onIce = true;
                                                        break;
                                                    }
                                                    case SOUL_SAND: {
                                                        onSoulSand = true;
                                                        break;
                                                    }
                                                    case SLIME_BLOCK: {
                                                        onSlime = true;
                                                        break;
                                                    }
                                                }
                                        }
                                        if (verticalMovement > 0 && dataHolder.playerVersion.isBelow(ProtocolVersion.V1_14) && Materials.checkFlag(type, Materials.LADDER) && normalBox.copy().expand(0.2f, 0, 0.2f).isCollided(blockBox))
                                            onClimbable = true;

                                        if (blockMaterial != null) {
                                            switch (blockMaterial) {
                                                case PISTON:
                                                case PISTON_HEAD:
                                                case MOVING_PISTON:
                                                case STICKY_PISTON: {
                                                    if (normalBox.copy().expand(0.5, 0.5, 0.5).isCollided(blockBox))
                                                        pistonNear = true;
                                                    break;
                                                }
                                            }
                                        }

                                        if (groundBox.copy().expand(0.5, 0.6, 0.5).isCollided(blockBox)) {
                                            if (Materials.checkFlag(type, Materials.SLABS))
                                                onSlab = true;
                                            else if (Materials.checkFlag(type, Materials.STAIRS))
                                                onStairs = true;
                                            else if (blockMaterial != null)
                                                switch (blockMaterial) {
                                                    case CAKE:
                                                    case BREWING_STAND:
                                                    case FLOWER_POT:
                                                    case PLAYER_HEAD:
                                                    case PLAYER_WALL_HEAD:
                                                    case SKELETON_SKULL:
                                                    case CREEPER_HEAD:
                                                    case DRAGON_HEAD:
                                                    case ZOMBIE_HEAD:
                                                    case ZOMBIE_WALL_HEAD:
                                                    case CREEPER_WALL_HEAD:
                                                    case DRAGON_WALL_HEAD:
                                                    case WITHER_SKELETON_SKULL:
                                                    case LANTERN:
                                                    case SKELETON_WALL_SKULL:
                                                    case WITHER_SKELETON_WALL_SKULL:
                                                    case SNOW: {
                                                        miscNear = true;
                                                        break;
                                                    }
                                                    case BLACK_BED:
                                                    case BLUE_BED:
                                                    case BROWN_BED:
                                                    case CYAN_BED:
                                                    case GRAY_BED:
                                                    case GREEN_BED:
                                                    case LIME_BED:
                                                    case MAGENTA_BED:
                                                    case ORANGE_BED:
                                                    case PINK_BED:
                                                    case PURPLE_BED:
                                                    case RED_BED:
                                                    case WHITE_BED:
                                                    case YELLOW_BED:
                                                    case LIGHT_BLUE_BED:
                                                    case LIGHT_GRAY_BED: {
                                                        bedNear = true;
                                                        break;
                                                    }
                                                }
                                        }
                                    } else if (blockBox.isCollided(normalBox)) {
                                        XMaterial blockMaterial = getXMaterial(type);

                                        if (blockMaterial != null)
                                            switch (blockMaterial) {
                                                case END_PORTAL:
                                                case NETHER_PORTAL: {
                                                    inPortal = true;
                                                    break;
                                                }
                                            }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void calculateCollisionBoxes(final DataHolder dataHolder, final double verticalMovement, final double horizontalMovement) {
        final int startX = Location.locToBlock(dataHolder.getMovementData().getMovementStorage().getCurrentPosition().getX() - 1 - horizontalMovement);
        final int endX = Location.locToBlock(dataHolder.getMovementData().getMovementStorage().getCurrentPosition().getX() + 1 + horizontalMovement);
        final int startY = Location.locToBlock(dataHolder.getMovementData().getMovementStorage().getCurrentPosition().getY() - Math.max(1, 1 + Math.abs(verticalMovement)));
        final int endY = Location.locToBlock(dataHolder.getMovementData().getMovementStorage().getCurrentPosition().getY() + Math.max(2.4, 2.4 + Math.abs(verticalMovement)));
        final int startZ = Location.locToBlock(dataHolder.getMovementData().getMovementStorage().getCurrentPosition().getZ() - 1 - horizontalMovement);
        final int endZ = Location.locToBlock(dataHolder.getMovementData().getMovementStorage().getCurrentPosition().getZ() + 1 + horizontalMovement);

        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.endX = endX;
        this.endY = endY;
        this.endZ = endZ;

        final SimpleCollisionBox waterBox = calculateCollisionBox(dataHolder, -0.38, startX, endX, startY, endY, startZ, endZ);
        final SimpleCollisionBox lavaBox = calculateCollisionBox(dataHolder, -0.4, startX, endX, startY, endY, startZ, endZ);

        waterBox.expand(0, -0.38, 0);
        lavaBox.expand(-0.1f, -0.4f, -0.1f);

        floorCollisionBox(waterBox);
        floorCollisionBox(lavaBox);
    }

    private SimpleCollisionBox calculateCollisionBox(final DataHolder dataHolder, final double yOffset, final int startX, final int endX, final int startY, final int endY, final int startZ, final int endZ) {
        final SimpleCollisionBox box = dataHolder.getBox().copy();
        box.xMin = Location.locToBlock(box.xMin + startX);
        box.yMin = Location.locToBlock(box.yMin + startY + yOffset);
        box.zMin = Location.locToBlock(box.zMin + startZ);
        box.xMax = Location.locToBlock(box.xMax + endX);
        box.yMax = Location.locToBlock(box.yMax + endY + yOffset);
        box.zMax = Location.locToBlock(box.zMax + endZ);
        return box;
    }

    private void floorCollisionBox(SimpleCollisionBox box) {
        box.xMin = MathHelper.floor(box.xMin);
        box.yMin = MathHelper.floor(box.yMin);
        box.zMin = MathHelper.floor(box.zMin);
        box.xMax = MathHelper.floor(box.xMax + 1.);
        box.yMax = MathHelper.floor(box.yMax + 1.);
        box.zMax = MathHelper.floor(box.zMax + 1.);
    }


    private void resetFlags() {
        onClimbable = serverGround = nearGround = fenceBelow
                = inScaffolding = inHoney = fenceNear
                = onSlab = onStairs = onHalfBlock = inLiquid = inLava = inWater = inWeb = onSlime = pistonNear
                = onIce = onSoulSand = blocksAbove = collidesVertically = bedNear = collidesHorizontally =
                blocksNear = inBlock = miscNear = collidedWithEntity = blocksBelow = inPortal = false;
    }

    private void limitMovements(double verticalMovement, double horizontalMovement) {
        if (verticalMovement > 10) verticalMovement = 10;
        else if (verticalMovement < -10) verticalMovement = -10;
        if (horizontalMovement > 10) horizontalMovement = 10;
    }

    private void checkLiquid(DataHolder dataHolder, SimpleCollisionBox waterBox, SimpleCollisionBox lavaBox) {
        inWater = KauriMiscUtils.isInMaterialBB(dataHolder.player.getWorld(), waterBox, Materials.WATER);
        inLava = KauriMiscUtils.isInMaterialBB(dataHolder.player.getWorld(), lavaBox, Materials.LAVA);
    }
}

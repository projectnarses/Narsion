package org.narses.entities.thrownitementity;

import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;

public class ThrownItemEntity extends LivingEntity {

    private BiConsumer<Instance, Point> hitBlock;
    private Consumer<Entity> hitEntity;
    private boolean isMoving;
    private final ItemStack itemStack;
    private long previousTickAge;
    private final ArmorStandMeta meta;

    private Vec spinVector;

    public ThrownItemEntity(Pos spawnPosition, ItemStack itemStack, double force) {
        super(EntityType.ARMOR_STAND);
        this.itemStack = itemStack;
        // TODO: Rewrite collision
//        this.setBoundingBox(new OffsetBoundingBox(this, 0.1f, 0.1f, 0.1f, 0.0f, 1.8f, 0.0f));
        this.setInvisible(true);
        this.isMoving = true;
        this.position = spawnPosition;
        Random random = new Random();

        // Set meta
        this.meta = (ArmorStandMeta) getEntityMeta();

        meta.setHeadRotation(new Vec(60, 0, 0));
        this.setHelmet(itemStack);

        final Vec direction = spawnPosition.direction();

        // Velocity
        this.setVelocity(direction.normalize().mul(10 * force));

        // Spin
        this.spinVector = new Vec(
                random.nextDouble() - 0.5,
                random.nextDouble() - 0.5,
                random.nextDouble() - 0.5
        ).normalize().mul(10);
    }

    public void setHitBlock(BiConsumer<Instance, Point> hitBlock) {
        this.hitBlock = hitBlock;
    }

    public void setHitEntity(Consumer<Entity> hitEntity) {
        this.hitEntity = hitEntity;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public Vec getSpinVector() {
        return this.spinVector;
    }

    public boolean isMoving() {
        return this.isMoving;
    }

    public void setMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }

    @Override
    public void update(long time) {
        super.update(time);

        if (this.isMoving) {
            // Rotation
            final double delta = (time - this.previousTickAge) / 50.0;

            if ((delta < 5) && (delta > 0)) {
                meta.setHeadRotation(meta.getHeadRotation().add(this.spinVector.mul(delta)));
            }


            // TODO: Rewrite collision
//            if (hitEntity != null) {
//                for (final Entity entity : this.instance.getEntities()) {
//                    if ((this.getBoundingBox().intersect(entity)) && (entity.getClass() != ThrownItemEntity.class)) {
//                        this.hitEntity.accept(entity);
//                    }
//                }
//            }

            Pos checkPos = this.position.add(0f, 0.4999f, 0f);

            // Get block at position
            final Block block = this.getInstance()
                    .getBlock(checkPos);

            if (hitBlock != null && !block.equals(Block.AIR)) {
                this.hitBlock.accept(this.instance, checkPos);
            }
        }

        // Set the tick age of this tick.
        this.previousTickAge = time;
    }
}

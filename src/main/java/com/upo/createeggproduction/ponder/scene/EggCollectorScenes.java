package com.upo.createeggproduction.ponder.scene;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import com.upo.createeggproduction.ModBlocks;
import com.upo.createeggproduction.content.block_entities.EggCollectorBlockEntity;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

public class EggCollectorScenes {
    public static void processing(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("egg_producer", "Use an Egg Producer to easily mass-produce eggs.");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();

        scene.idle(10);
        BlockPos center = util.grid().at(2, 0, 2);
        BlockPos chickenSpawnPos = util.grid().at(2, 1, 2);
        Vec3 spawnVec = util.vector().centerOf(chickenSpawnPos);

        scene.world().createEntity(w -> {
            Chicken chickenEntity = EntityType.CHICKEN.create(w);
            Vec3 v = util.vector().topOf(center);
            chickenEntity.setPosRaw(spawnVec.x(), spawnVec.y(), spawnVec.z());
            chickenEntity.setYRot(chickenEntity.yRotO = 180);
            return chickenEntity;
        });
        scene.idle(20);
        scene.overlay().showControls(util.vector().centerOf(center.above(2)), Pointing.DOWN, 40).rightClick()
                .withItem(ModBlocks.EMPTY_EGG_COLLECTOR_BLOCK_ITEM.get().getDefaultInstance());
        scene.idle(10);
        scene.overlay().showText(50)
                .text("Right-click a Chicken with the empty empty egg producer to capture it")
                .pointAt(util.vector().blockSurface(center.above(2), Direction.WEST))
                .placeNearTarget();
        scene.idle(50);
        scene.world().modifyEntities(Chicken.class, Entity::discard);
        scene.idle(20);

        scene.world().showSection(util.select().fromTo(0, 1, 0, 3, 1, 4), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().fromTo(2, 2, 3, 2, 2, 3), Direction.DOWN);
        scene.world().modifyBlockEntity(util.grid().at(2, 1, 2),
                EggCollectorBlockEntity.class,
                SmartBlockEntity::markVirtual);
        scene.idle(20);
        scene.world().setKineticSpeed(util.select().everywhere(), -64);
        scene.overlay().showText(50)
                .text("Connect the power from the bottom……")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(util.vector().centerOf(2, 1, 2));
        scene.idle(80);

        scene.world().showSection(util.select().fromTo(1, 2, 3, 1, 2, 3), Direction.DOWN);
        scene.world().showSection(util.select().fromTo(2, 2, 2, 2, 2, 2), Direction.DOWN);
        scene.world().showSection(util.select().fromTo(3, 2, 3, 4, 2, 2), Direction.DOWN);
        scene.world().showSection(util.select().fromTo(4, 1, 1, 4, 3, 1), Direction.DOWN);
        scene.idle(10);

        BlockPos seedAppearBeltPos = util.grid().at(0, 1, 3);
        BlockPos seedEntryBeltPos = util.grid().at(1, 1, 3);
        BlockPos eggExitBeltPos = util.grid().at(2, 1, 2);


        for (int i = 0; i < 4; i++) {
            scene.world().createItemOnBelt(seedAppearBeltPos, Direction.WEST, new ItemStack(Items.WHEAT_SEEDS));
            scene.idle(10);
            scene.world().flapFunnel(seedEntryBeltPos.above(), true);
            scene.world().removeItemsFromBelt(seedEntryBeltPos);
        }
        scene.idle(10);

        scene.world().propagatePipeChange(util.grid().at(4, 2, 1));

        scene.overlay().showText(50)
                .text("Supply seeds and water.")
                .placeNearTarget()
                .pointAt(util.vector().centerOf(2, 2, 3));

        scene.idle(50);

        for (int i = 0; i < 8; i++) {
            scene.idle(8);
            scene.world().removeItemsFromBelt(eggExitBeltPos);
            scene.world().flapFunnel(seedEntryBeltPos.above(), false);
            scene.world().createItemOnBelt(eggExitBeltPos, Direction.SOUTH, new ItemStack(Items.EGG));
        }

        scene.overlay().showText(100)
                .text("And the eggs will be produced constantly.")
                .placeNearTarget()
                .pointAt(util.vector().centerOf(2, 1, 2));

        scene.markAsFinished();

    }

}

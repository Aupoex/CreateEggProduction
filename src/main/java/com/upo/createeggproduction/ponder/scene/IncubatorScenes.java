package com.upo.createeggproduction.ponder.scene;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class IncubatorScenes {
    public static void processing(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("egg_incubating", "Harness Your Eggs!");
        scene.configureBasePlate(0, 0, 4);
        scene.showBasePlate();

        scene.idle(15);

        BlockPos blaze = util.grid().at(2, 1, 2);

        scene.world().setBlock(util.grid().at(2, 1, 2), AllBlocks.BLAZE_BURNER.getDefaultState()
                .setValue(BlazeBurnerBlock.HEAT_LEVEL, BlazeBurnerBlock.HeatLevel.SMOULDERING), true);
        scene.world().showSection(util.select().position(blaze), Direction.DOWN);
        scene.idle(5);
        scene.overlay().showText(40)
                .text("To harness eggs,you need a Blaze Burner.")
                .pointAt(util.vector().blockSurface(blaze, Direction.WEST))
                .placeNearTarget();
        scene.idle(50);

        scene.world().showSection(util.select().fromTo(0, 1, 0, 3, 2, 3), Direction.DOWN);
        scene.idle(15);

        scene.overlay().showText(40)
                .text("Put your Incubator on it.")
                .pointAt(util.vector().blockSurface(blaze, Direction.WEST))
                .placeNearTarget();
        scene.world().setKineticSpeed(util.select().everywhere(), -64);
        scene.idle(45);


        BlockPos eggAppearBeltPos = util.grid().at(0, 1, 3);
        BlockPos eggEntryBeltPos = util.grid().at(2, 1, 3);
        BlockPos ExitBeltPos = util.grid().at(2, 1, 1);


        for (int i = 0; i < 4; i++) {
            scene.world().createItemOnBelt(eggAppearBeltPos, Direction.WEST, new ItemStack(Items.EGG));
            scene.world().removeItemsFromBelt(eggEntryBeltPos);
            scene.idle(1);
            scene.world().removeItemsFromBelt(eggEntryBeltPos);
            scene.idle(2);
            scene.world().removeItemsFromBelt(eggEntryBeltPos);
            scene.idle(3);
            scene.world().removeItemsFromBelt(eggEntryBeltPos);
            scene.idle(4);
            scene.world().removeItemsFromBelt(eggEntryBeltPos);
            scene.idle(5);
            scene.world().removeItemsFromBelt(eggEntryBeltPos);
        }
        scene.idle(20);



        scene.overlay().showText(35)
                .text("Supply eggs.")
                .pointAt(util.vector().blockSurface(blaze, Direction.WEST))
                .placeNearTarget();
        scene.idle(45);


        scene.overlay().showText(75)
                .text("When inactive, the Blaze Burner hatches eggs into adult chickens.")
                .pointAt(util.vector().blockSurface(blaze.above(1), Direction.WEST))
                .attachKeyFrame()
                .placeNearTarget();

        Vec3 spawnVec = util.vector().centerOf(ExitBeltPos);
        scene.world().createEntity(w -> {
            Chicken chickenEntity = EntityType.CHICKEN.create(w);
            Vec3 v = util.vector().topOf(ExitBeltPos);
            chickenEntity.setPosRaw(spawnVec.x(), spawnVec.y(), spawnVec.z());
            chickenEntity.setYRot(chickenEntity.yRotO = 180);
            return chickenEntity;
        });

        scene.idle(70);
        scene.world().modifyEntities(Chicken.class, Entity::discard);
        scene.idle(10);

        scene.world().setBlock(util.grid().at(2, 1, 2), Blocks.AIR.defaultBlockState(), false);
        scene.world().setBlock(util.grid().at(2, 1, 2), AllBlocks.BLAZE_BURNER.getDefaultState()
                .setValue(BlazeBurnerBlock.HEAT_LEVEL, BlazeBurnerBlock.HeatLevel.KINDLED), true);
        scene.overlay().showText(100)
                .text("When active, the Blaze Burner produces cooked chicken and feathers.")
                .pointAt(util.vector().blockSurface(blaze.above(1), Direction.WEST))
                .placeNearTarget();


        scene.world().createItemOnBelt(ExitBeltPos, Direction.UP, new ItemStack(Items.FEATHER));
        scene.idle(10);
        scene.world().createItemOnBelt(ExitBeltPos, Direction.UP, new ItemStack(Items.COOKED_CHICKEN));
        scene.idle(10);
        scene.world().createItemOnBelt(ExitBeltPos, Direction.UP, new ItemStack(Items.FEATHER));
        scene.idle(10);
        scene.world().createItemOnBelt(ExitBeltPos, Direction.UP, new ItemStack(Items.COOKED_CHICKEN));
        scene.idle(10);
        scene.world().createItemOnBelt(ExitBeltPos, Direction.UP, new ItemStack(Items.FEATHER));
        scene.idle(10);
        scene.world().createItemOnBelt(ExitBeltPos, Direction.UP, new ItemStack(Items.COOKED_CHICKEN));
        scene.idle(10);

        scene.idle(70);


        scene.world().setBlock(util.grid().at(2, 1, 2), Blocks.AIR.defaultBlockState(), false);
        scene.world().setBlock(util.grid().at(2, 1, 2), AllBlocks.BLAZE_BURNER.getDefaultState()
                .setValue(BlazeBurnerBlock.HEAT_LEVEL, BlazeBurnerBlock.HeatLevel.SEETHING), true);
        scene.overlay().showText(100)
                .text("When superheated, the Blaze Burner produces charcoal and bones.")
                .pointAt(util.vector().blockSurface(blaze.above(1), Direction.WEST))
                .placeNearTarget();


        scene.world().createItemOnBelt(ExitBeltPos, Direction.UP, new ItemStack(Items.CHARCOAL));
        scene.idle(10);
        scene.world().createItemOnBelt(ExitBeltPos, Direction.UP, new ItemStack(Items.BONE));
        scene.idle(10);
        scene.world().createItemOnBelt(ExitBeltPos, Direction.UP, new ItemStack(Items.CHARCOAL));
        scene.idle(10);
        scene.world().createItemOnBelt(ExitBeltPos, Direction.UP, new ItemStack(Items.BONE));
        scene.idle(10);
        scene.world().createItemOnBelt(ExitBeltPos, Direction.UP, new ItemStack(Items.CHARCOAL));
        scene.idle(10);
        scene.world().createItemOnBelt(ExitBeltPos, Direction.UP, new ItemStack(Items.BONE));
        scene.idle(10);
        scene.world().createItemOnBelt(ExitBeltPos, Direction.UP, new ItemStack(Items.CHARCOAL));
        scene.idle(10);
        scene.world().createItemOnBelt(ExitBeltPos, Direction.UP, new ItemStack(Items.BONE));
        scene.idle(10);

        scene.idle(70);

        scene.markAsFinished();

    }

}

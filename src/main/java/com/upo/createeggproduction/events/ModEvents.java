package com.upo.createeggproduction.events;

import com.simibubi.create.AllDamageTypes;
import com.upo.createeggproduction.CreateEggProduction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.damagesource.DamageSource;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.bus.api.SubscribeEvent;

@EventBusSubscriber(modid = CreateEggProduction.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ModEvents {

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Chicken)) {
            return;
        }
        DamageSource source = event.getSource();
        boolean killedBySaw = source.is(AllDamageTypes.SAW);
        if (killedBySaw) {
            if (entity.level().random.nextFloat() < 0.5f) {
                ItemStack extraDrop = new ItemStack(Items.CHICKEN);
                ItemEntity itemEntity = new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), extraDrop);
                itemEntity.setDeltaMovement(entity.level().random.nextGaussian() * 0.05, 0.2 + entity.level().random.nextGaussian() * 0.05, entity.level().random.nextGaussian() * 0.05);
                event.getDrops().add(itemEntity);
            }
        }
    }
}


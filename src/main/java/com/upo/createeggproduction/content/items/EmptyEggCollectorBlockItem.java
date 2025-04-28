package com.upo.createeggproduction.content.items;

import com.upo.createeggproduction.ModBlocks;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class EmptyEggCollectorBlockItem extends BlockItem {

    public EmptyEggCollectorBlockItem(Block block, Properties properties) {
        super(block, properties);
    }
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
        if (entity instanceof Chicken chicken) {
            Level level = player.level();
            level.playSound(null, chicken.getX(), chicken.getY(), chicken.getZ(), SoundEvents.CHICKEN_EGG, SoundSource.PLAYERS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
            if (!level.isClientSide) {
                ItemStack filledStack = new ItemStack(ModBlocks.EGG_COLLECTOR_BLOCK.get());
                boolean giveSuccess = false;
                if (stack.getCount() == 1 && !player.getAbilities().instabuild) {
                    player.setItemInHand(hand, filledStack.copy());
                    giveSuccess = true;
                }
                else {
                    if (player.getInventory().add(filledStack.copy())) {
                        giveSuccess = true;
                        if (!player.getAbilities().instabuild) {
                            stack.shrink(1);
                        }
                    } else {
                        giveSuccess = false;
                    }
                }
                if (giveSuccess) {
                    chicken.discard();
                } else {
                    return InteractionResult.FAIL;
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }
}

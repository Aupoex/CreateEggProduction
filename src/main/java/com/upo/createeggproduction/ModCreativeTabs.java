package com.upo.createeggproduction;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {


    public static final DeferredRegister<CreativeModeTab> TAB_REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateEggProduction.MODID);


    public static final RegistryObject<CreativeModeTab> MAIN_TAB = TAB_REGISTER.register("main_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModBlocks.INCUBATOR_BLOCK.get()))
                    .title(Component.translatable("creativetab.createeggproduction.main_tab"))
                    .build()
    );


    public static void register(IEventBus modEventBus) {

        TAB_REGISTER.register(modEventBus);
    }
}



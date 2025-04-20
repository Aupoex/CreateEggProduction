package com.upo.createeggproduction;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TAB_REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateEggProduction.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB = TAB_REGISTER.register("main_tab",
            () -> CreativeModeTab.builder()
                    .icon(ModBlocks.EGG_COLLECTOR_BLOCK::asStack)
                    .title(Component.translatable("creativetab.createeggproduction.main_tab"))
                    .build()
    );
    public static CreativeModeTab getBaseTab() {
        return MAIN_TAB.get();
    }
    public static void register(IEventBus modEventBus) {
        TAB_REGISTER.register(modEventBus);
    }
}
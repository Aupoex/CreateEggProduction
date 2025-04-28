package com.upo.createeggproduction.ponder;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import com.upo.createeggproduction.CreateEggProduction;
import com.upo.createeggproduction.ModBlocks;
import net.minecraft.world.item.Item;

public class ModPonderTags {

    public static final ResourceLocation EGG_COLLECTOR_TAG_ID =
            ResourceLocation.fromNamespaceAndPath(CreateEggProduction.MODID, "egg_collector");

    public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {


        helper.registerTag(EGG_COLLECTOR_TAG_ID)
                .addToIndex()
                .item(ModBlocks.EGG_COLLECTOR_BLOCK.get())
                .title("Egg Collector")
                .description("Ponder scenes for the Egg Collector")
                .register();


        PonderTagRegistrationHelper<RegistryEntry<?, ?>> ENTRY_HELPER = helper.withKeyFunction(RegistryEntry::getId);
        ENTRY_HELPER.addToTag(EGG_COLLECTOR_TAG_ID)
                .add(ModBlocks.EGG_COLLECTOR_BLOCK);

    }


    public static void registerNormal(PonderTagRegistrationHelper<ResourceLocation> helper) {

        PonderTagRegistrationHelper<Item> HELPER = helper.withKeyFunction(BuiltInRegistries.ITEM::getKey);
        HELPER.addToTag(EGG_COLLECTOR_TAG_ID)
                .add(ModBlocks.EMPTY_EGG_COLLECTOR_BLOCK_ITEM.get());
        HELPER.addToTag(EGG_COLLECTOR_TAG_ID)
                .add(ModBlocks.INCUBATOR_BLOCK_ITEM.get());


    }

}

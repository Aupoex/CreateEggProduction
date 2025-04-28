package com.upo.createeggproduction.ponder;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.upo.createeggproduction.ponder.scene.EggCollectorScenes;
import com.upo.createeggproduction.ponder.scene.IncubatorScenes;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import com.upo.createeggproduction.ModBlocks;
import net.minecraft.world.item.Item;



public class ModPonderScenes {

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {

        PonderSceneRegistrationHelper<RegistryEntry<?, ?>> ENTRY_HELPER = helper.withKeyFunction(RegistryEntry::getId);

        ENTRY_HELPER.forComponents(ModBlocks.EGG_COLLECTOR_BLOCK)
                .addStoryBoard("egg_collector_processing", EggCollectorScenes::processing,ModPonderTags.EGG_COLLECTOR_TAG_ID);

    }

    public static void registerNormal(PonderSceneRegistrationHelper<ResourceLocation> helper) {

        PonderSceneRegistrationHelper<Item> HELPER = helper.withKeyFunction(BuiltInRegistries.ITEM::getKey);

        HELPER.forComponents(ModBlocks.EMPTY_EGG_COLLECTOR_BLOCK_ITEM.get())
                .addStoryBoard("egg_collector_processing", EggCollectorScenes::processing,ModPonderTags.EGG_COLLECTOR_TAG_ID);

        HELPER.forComponents(ModBlocks.INCUBATOR_BLOCK_ITEM.get())
                .addStoryBoard("egg_incubator_processing", IncubatorScenes::processing,ModPonderTags.EGG_COLLECTOR_TAG_ID);

    }

}

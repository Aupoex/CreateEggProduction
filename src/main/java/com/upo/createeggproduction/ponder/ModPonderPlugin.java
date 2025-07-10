package com.upo.createeggproduction.ponder;

import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import com.upo.createeggproduction.CreateEggProduction;


public class ModPonderPlugin implements PonderPlugin {

    @Override
    public @NotNull String getModId() { return CreateEggProduction.MODID; }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        ModPonderScenes.register(helper);
        ModPonderScenes.registerNormal(helper);
    }
    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        ModPonderTags.register(helper);
        ModPonderTags.registerNormal(helper);
    }
}


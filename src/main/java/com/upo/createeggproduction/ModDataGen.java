package com.upo.createeggproduction;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.providers.RegistrateDataProvider;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

public class ModDataGen {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CreateRegistrate registrate = CreateEggProduction.registrate();
        RegistrateDataProvider registrateDataProvider = new RegistrateDataProvider(
                registrate,
                CreateEggProduction.MODID,
                event
        );
        generator.addProvider(
                event.includeServer(),
                registrate.setDataProvider(registrateDataProvider)
        );
    }
}
package com.upo.createeggproduction.integration.ponder;

import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.fml.ModList;
import com.upo.createeggproduction.ponder.ModPonderPlugin;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

public class PonderHelper {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final boolean IS_PONDER_LOADED = ModList.get().isLoaded("ponderjs");
    public static void registerPlugin() {
        if (IS_PONDER_LOADED) {
            LOGGER.info("PonderJS detected, attempting to register ModPonderPlugin...");
            try {
                PonderIndex.addPlugin(ModPonderPlugin.class.getDeclaredConstructor().newInstance());
                LOGGER.info("Successfully registered ModPonderPlugin with PonderIndex.");
            } catch (Exception e) {
                LOGGER.error("Failed to register ModPonderPlugin with PonderIndex!", e);
            }
        } else {
            LOGGER.info("PonderJS not detected, skipping Ponder plugin registration.");
        }
    }
}



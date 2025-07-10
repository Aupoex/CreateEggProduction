package com.upo.createeggproduction.integration.ponder;

import net.createmod.ponder.foundation.PonderIndex;
import com.upo.createeggproduction.ponder.ModPonderPlugin;
import net.minecraftforge.fml.ModList;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

public class PonderHelper {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final boolean IS_CREATE_LOADED = ModList.get().isLoaded("create");

    public static void registerPlugin() {
        if (!IS_CREATE_LOADED) {
            return;
        }

    }
}



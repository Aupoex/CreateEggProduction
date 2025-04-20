package com.upo.createeggproduction;

import com.simibubi.create.api.stress.BlockStressValues;
import net.minecraft.world.level.block.Block;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

public class ModStressValues {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void registerAll() {
        LOGGER.debug("Attempting to register stress values...");
        try {

            Block block = ModBlocks.EGG_COLLECTOR_BLOCK.get();
            BlockStressValues.IMPACTS.register(
                    block,
                    () -> 4.0
            );
            LOGGER.info("Successfully registered stress impact for: {}", block.getDescriptionId());
        } catch (Exception e) {

            LOGGER.error("Failed to register stress values!", e);
        }
    }
}
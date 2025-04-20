package com.upo.createeggproduction;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

public class ModPartials {
    private static final Logger LOGGER = LogUtils.getLogger(); // 添加 Logger

    public static final PartialModel EGG_COLLECTOR_SHAFT_INPUT = block("ecb_sha");

    private static PartialModel block(String path) {
        return PartialModel.of(
                ResourceLocation.fromNamespaceAndPath(CreateEggProduction.MODID, "block/" + path)
        );
    }
    public static void init() {
        LOGGER.debug("ModPartials initialized (ensuring class loading).");
    }
}

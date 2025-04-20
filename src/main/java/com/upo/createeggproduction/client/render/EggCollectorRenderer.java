package com.upo.createeggproduction.client.render;

import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.upo.createeggproduction.content.block_entities.EggCollectorBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

public class EggCollectorRenderer extends KineticBlockEntityRenderer<EggCollectorBlockEntity> {
    private static final Logger LOGGER = LogUtils.getLogger();

    public EggCollectorRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    @Override
    protected void renderSafe(EggCollectorBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        Level level = be.getLevel();
        boolean flywheelSupported = level != null && VisualizationManager.supportsVisualization(level);
        if (flywheelSupported) {
            return;
        }
         LOGGER.debug("Flywheel NOT supported, attempting fallback render for {} (currently none implemented).", be.getBlockPos());
    }
}


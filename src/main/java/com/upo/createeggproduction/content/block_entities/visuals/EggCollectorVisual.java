package com.upo.createeggproduction.content.block_entities.visuals;

import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.instance.Instancer;
import dev.engine_room.flywheel.api.instance.InstancerProvider;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visual.LightUpdatedVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import com.upo.createeggproduction.ModPartials;
import com.upo.createeggproduction.content.block_entities.EggCollectorBlockEntity;
import java.util.function.Consumer;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;


public class EggCollectorVisual extends KineticBlockEntityVisual<EggCollectorBlockEntity> implements SimpleDynamicVisual, LightUpdatedVisual {

    private static final Logger LOGGER = LogUtils.getLogger();
    protected RotatingInstance shaftInputInstance;

    public EggCollectorVisual(VisualizationContext context, EggCollectorBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);
        initializeVisual(partialTick);
    }

    private void initializeVisual(float partialTick) {
        try {
            if (ModPartials.EGG_COLLECTOR_SHAFT_INPUT == null) {
                LOGGER.error("PartialModel EGG_COLLECTOR_SHAFT_INPUT is null!");
                return;
            }
            var rotorModelGetter = Models.partial(ModPartials.EGG_COLLECTOR_SHAFT_INPUT);

            InstancerProvider ip = instancerProvider();
            if (ip == null) {
                return;
            }
            Instancer<RotatingInstance> instancer = ip.instancer(AllInstanceTypes.ROTATING, rotorModelGetter);
            if (instancer == null) {
                return;
            }
            shaftInputInstance = instancer.createInstance();
            if (shaftInputInstance == null) {
                return;
            }
            shaftInputInstance.setup(blockEntity)
                    .setPosition(getVisualPosition())
                    .setChanged();
            updateLight(partialTick);
        } catch (Exception e) {
            if (shaftInputInstance != null) {
                shaftInputInstance.delete();
                shaftInputInstance = null;
            }
        }
    }
    @Override
    public void beginFrame(DynamicVisual.Context context) {
        if (shaftInputInstance == null) return;
        shaftInputInstance.setup(blockEntity)
                .setChanged();
    }
    @Override
    public void updateLight(float partialTick) {
        if (shaftInputInstance != null) {
            relight(getVisualPosition(), shaftInputInstance);
        }
    }
    @Override
    protected void _delete() {
        LOGGER.debug("Deleting visual instance...");
        if (shaftInputInstance != null) {
            shaftInputInstance.delete();
            shaftInputInstance = null;
        }
    }
    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        if (shaftInputInstance != null) {
            consumer.accept(shaftInputInstance);
        }
    }
}
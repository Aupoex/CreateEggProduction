package com.upo.createeggproduction.compat.jei;

import com.upo.createeggproduction.compat.jei.recipes.ChickenCapturingRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import com.upo.createeggproduction.CreateEggProduction;
import com.upo.createeggproduction.ModBlocks;
import com.upo.createeggproduction.compat.jei.recipes.EggProductionRecipe;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import java.util.List;
import java.util.Optional;

@JeiPlugin
public class CreateEggProductionJEIPlugin implements IModPlugin {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final ResourceLocation PLUGIN_UID =
            ResourceLocation.fromNamespaceAndPath(CreateEggProduction.MODID, "jei_plugin");
    public static final ResourceLocation EGG_PRODUCTION_TYPE_ID =
            ResourceLocation.fromNamespaceAndPath(CreateEggProduction.MODID, "egg_production");
    @Override
    public ResourceLocation getPluginUid() { return PLUGIN_UID; }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new EggProductionCategory(registration.getJeiHelpers().getGuiHelper()),
                new ChickenCapturingCategory(registration.getJeiHelpers().getGuiHelper())
        );
    }
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        try {
            IJeiHelpers jeiHelpers = registration.getJeiHelpers();
            Optional<RecipeType<?>> maybeRecipeType =
                    jeiHelpers.getRecipeType(EGG_PRODUCTION_TYPE_ID);
            if (maybeRecipeType.isPresent()) {
                RecipeType<?> unknownRecipeType = maybeRecipeType.get();
                if (unknownRecipeType.getRecipeClass().equals(EggProductionRecipe.class)) {
                    @SuppressWarnings("unchecked")
                    RecipeType<EggProductionRecipe> eggProductionRecipeType = (RecipeType<EggProductionRecipe>) unknownRecipeType;
                    List<EggProductionRecipe> eggProductionRecipes = EggProductionRecipe.getRecipes();
                    if (eggProductionRecipes.isEmpty()) {
                    }
                    registration.addRecipes(eggProductionRecipeType, eggProductionRecipes);
                } else {
                    return;
                }
            } else {
                return;
            }

            Optional<RecipeType<?>> maybeCapturingType = jeiHelpers.getRecipeType(ChickenCapturingCategory.TYPE.getUid());
            if(maybeCapturingType.isPresent() && maybeCapturingType.get().getRecipeClass().equals(ChickenCapturingRecipe.class)) {
                @SuppressWarnings("unchecked")
                RecipeType<ChickenCapturingRecipe> capturingRecipeType = (RecipeType<ChickenCapturingRecipe>) maybeCapturingType.get();
                List<ChickenCapturingRecipe> capturingRecipes = ChickenCapturingRecipe.getRecipes();
                registration.addRecipes(capturingRecipeType, capturingRecipes);
                LOGGER.debug("Added {} ChickenCapturing recipes.", capturingRecipes.size());
            }
        } catch (Exception e) {
            LOGGER.error("Failed to register JEI recipes or info for {}!", CreateEggProduction.MODID, e);
        }
    }
    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {

        try {
            registration.addRecipeCatalyst(
                    new ItemStack(ModBlocks.EGG_COLLECTOR_BLOCK.get()),
                    EggProductionCategory.TYPE
            );
        } catch (Exception e) {
            LOGGER.error("Failed to register JEI catalysts for {}!", CreateEggProduction.MODID, e);
        }
    }
}

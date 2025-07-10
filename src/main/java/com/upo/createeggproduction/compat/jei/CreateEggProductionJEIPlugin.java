package com.upo.createeggproduction.compat.jei;

import com.upo.createeggproduction.compat.jei.recipes.ChickenCapturingRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import com.upo.createeggproduction.CreateEggProduction;
import com.upo.createeggproduction.ModBlocks;
import com.upo.createeggproduction.compat.jei.recipes.EggProductionRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import java.util.List;
import java.util.Optional;

@JeiPlugin
public class CreateEggProductionJEIPlugin implements IModPlugin {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final ResourceLocation PLUGIN_UID =
            ResourceLocation.fromNamespaceAndPath(CreateEggProduction.MODID, "jei_plugin");

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new EggProductionCategory(registration.getJeiHelpers().getGuiHelper()),
                new ChickenCapturingCategory(registration.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {

        List<EggProductionRecipe> eggProductionRecipes = EggProductionRecipe.getRecipes();
        List<ChickenCapturingRecipe> chickenCapturingRecipes = ChickenCapturingRecipe.getRecipes();

        registration.addRecipes(EggProductionCategory.TYPE, eggProductionRecipes);
        registration.addRecipes(ChickenCapturingCategory.TYPE, chickenCapturingRecipes);

        LOGGER.debug("Registered {} Egg Production recipes for JEI.", eggProductionRecipes.size());
        LOGGER.debug("Registered {} Chicken Capturing recipes for JEI.", chickenCapturingRecipes.size());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {

        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.EGG_COLLECTOR_BLOCK.get()),
                EggProductionCategory.TYPE
        );
        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.EMPTY_EGG_COLLECTOR_BLOCK.get()),
                ChickenCapturingCategory.TYPE
        );
    }
}

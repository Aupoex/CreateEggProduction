package com.upo.createeggproduction.compat.jei.recipes;

import com.upo.createeggproduction.CreateEggProduction;
import com.upo.createeggproduction.ModBlocks;
import com.upo.createeggproduction.compat.jei.ChickenCapturingCategory;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import java.util.List;

public class ChickenCapturingRecipe {

    private final ItemStack inputItem = new ItemStack(ModBlocks.EMPTY_EGG_COLLECTOR_BLOCK_ITEM.get());
    private final ItemStack outputItem = new ItemStack(ModBlocks.EGG_COLLECTOR_BLOCK.get());
    private final ItemStack catalystChicken = new ItemStack(Items.CHICKEN_SPAWN_EGG);
    private final ResourceLocation id;

    public ChickenCapturingRecipe(ResourceLocation id) {this.id = id;}
    public ResourceLocation getId() { return id; }
    public ItemStack getInputItem() { return inputItem; }
    public ItemStack getOutputItem() { return outputItem; }
    public ItemStack getCatalystChicken() { return catalystChicken; }

    public RecipeType<?> getJeiRecipeType() {
        return ChickenCapturingCategory.TYPE;
    }
    public static List<ChickenCapturingRecipe> getRecipes() {
        return List.of(
                new ChickenCapturingRecipe(
                        ResourceLocation.fromNamespaceAndPath(CreateEggProduction.MODID, "chicken_capturing")
                )
        );
    }
}

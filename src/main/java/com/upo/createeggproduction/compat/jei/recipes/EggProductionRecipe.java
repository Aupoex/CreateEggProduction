package com.upo.createeggproduction.compat.jei.recipes;

import com.upo.createeggproduction.CreateEggProduction;
import com.simibubi.create.foundation.fluid.FluidIngredient; // Create 的 FluidIngredient
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class EggProductionRecipe {

    @Nullable
    private final FluidIngredient fluidIngredient;
    private final int fluidAmount = 100; // mB
    private final Ingredient seedIngredient;
    private final int seedAmount = 2;
    private final ItemStack result; // 输出物品
    private final int processingTime = 20; // ticks
    private final ResourceLocation id; // 配方 ID

    public EggProductionRecipe(@Nullable FluidIngredient fluid, Ingredient seeds, ItemStack result, ResourceLocation id) {
        this.fluidIngredient = fluid;
        this.seedIngredient = seeds;
        this.result = result;
        this.id = id;
    }

    public ResourceLocation getId() { return id; }
    @Nullable public FluidIngredient getFluidIngredient() { return fluidIngredient; }
    public int getFluidAmount() { return fluidAmount; }
    public Ingredient getSeedIngredient() { return seedIngredient; }
    public int getSeedAmount() { return seedAmount; }
    public ItemStack getResultItem() { return result; }
    public int getProcessingTime() { return processingTime; }

    public static List<EggProductionRecipe> getRecipes() {
        List<EggProductionRecipe> recipes = new ArrayList<>();
        TagKey<Item> seedTag = ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "seeds"));
        Ingredient seeds = Ingredient.of(seedTag);
        FluidIngredient water = FluidIngredient.fromFluid(Fluids.WATER, 100);
        ItemStack eggOutput = new ItemStack(Items.EGG, 1);
        recipes.add(new EggProductionRecipe(
                water,
                seeds,
                eggOutput,
                ResourceLocation.fromNamespaceAndPath(CreateEggProduction.MODID, "egg_production_jei")
        ));
        return recipes;
    }
}

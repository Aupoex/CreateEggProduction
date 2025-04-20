package com.upo.createeggproduction.compat.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.upo.createeggproduction.CreateEggProduction;
import com.upo.createeggproduction.compat.jei.recipes.EggProductionRecipe;
import com.upo.createeggproduction.ModBlocks;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.createmod.catnip.gui.element.GuiGameElement;
import java.util.Optional;

public class EggProductionCategory implements IRecipeCategory<EggProductionRecipe> {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final RecipeType<EggProductionRecipe> TYPE =
            RecipeType.create(
                    CreateEggProduction.MODID,
                    "egg_production",
                    EggProductionRecipe.class
            );
    private final Component title;
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable slotDrawable;
    private final IGuiHelper guiHelper;

    public EggProductionCategory(IGuiHelper guiHelper) {
        this.guiHelper = guiHelper;
        this.title = Component.translatable("jei.createeggproduction.recipe.egg_production");
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.EGG_COLLECTOR_BLOCK.get()));
        this.slotDrawable = guiHelper.getSlotDrawable();
        this.background = guiHelper.createBlankDrawable(130, 60);
        this.jeiArrow = guiHelper.drawableBuilder(AllGuiTextures.JEI_ARROW.getLocation(),
                        AllGuiTextures.JEI_ARROW.getStartX(), AllGuiTextures.JEI_ARROW.getStartY(),
                        AllGuiTextures.JEI_ARROW.getWidth(), AllGuiTextures.JEI_ARROW.getHeight())
                .build();
    }
    @Override public RecipeType<EggProductionRecipe> getRecipeType() { return TYPE; }
    @Override public Component getTitle() { return title; }
    @Override public IDrawable getBackground() { return background; }
    @Override public IDrawable getIcon() { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, EggProductionRecipe recipe, IFocusGroup focuses) {
        LOGGER.trace("Setting JEI layout for recipe: {}", recipe.getId());
        IIngredientType<FluidStack> fluidType = NeoForgeTypes.FLUID_STACK;

        builder.addSlot(RecipeIngredientRole.INPUT, 15, 9)
                .addIngredients(recipe.getSeedIngredient())
                .setBackground(slotDrawable, -1, -1);
        //      .addTooltipCallback((view, tips) -> tips.add(Component.literal("x" + recipe.getSeedAmount())));
        LOGGER.trace("Added seed input slot.");

        builder.addSlot(RecipeIngredientRole.INPUT, 15, 34)
                .addIngredients(fluidType, recipe.getFluidIngredient().getMatchingFluidStacks())
                .setFluidRenderer(1000, true, 16, 16)
                .setBackground(slotDrawable, -1, -1);

        builder.addSlot(RecipeIngredientRole.OUTPUT, 100, 22)
                .addItemStack(recipe.getResultItem())
                .setBackground(slotDrawable, -1, -1);
        LOGGER.trace("Added egg output slot.");
    }
    private void addFluidAmountTooltip(IRecipeSlotView recipeSlotView, ITooltipBuilder tooltip) {
        Optional<FluidStack> displayed = recipeSlotView.getDisplayedIngredient(NeoForgeTypes.FLUID_STACK);
        if (displayed.isEmpty() || displayed.get().isEmpty()) return;
        FluidStack fluidStack = displayed.get();
        tooltip.add(Component.literal(fluidStack.getAmount() + " mB"));
    }
    @Override
    public void draw(EggProductionRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        int arrowX = 45;
        int arrowY = 30;
        jeiArrow.draw(guiGraphics, arrowX, arrowY);
        ItemStack machineStack = new ItemStack(ModBlocks.EGG_COLLECTOR_BLOCK.get());
        int iconX = arrowX + (jeiArrow.getWidth() - 16) / 2;
        int iconY = arrowY - 22;
        GuiGameElement.of(machineStack)
                .scale(1.4f)
                .render(guiGraphics, iconX, iconY);
        Font font = Minecraft.getInstance().font;
        float referenceRPM = 16f;
        int baseWork = recipe.getProcessingTime();
        if (baseWork <= 0 || referenceRPM <= 0) {
            Component errorText = Component.literal("Invalid Config?").withStyle(ChatFormatting.RED);
            guiGraphics.drawString(font, errorText, 45, 5, 0xFF808080, false);
            return;
        }
        float ticksPerItemAtRefSpeed = baseWork / referenceRPM;
        float itemsPerSecondAtRefSpeed = 20.0f / ticksPerItemAtRefSpeed;
        String efficiencyString = String.format("%.2f", itemsPerSecondAtRefSpeed);
        Component efficiencyText = Component.translatable(
                "jei.createeggproduction.recipe.efficiency",
                (int)referenceRPM,
                efficiencyString
        ).withStyle(ChatFormatting.GRAY);

        int efficiencyX = arrowX + (jeiArrow.getWidth() - font.width(efficiencyText)) / 2;
        int efficiencyY = arrowY + jeiArrow.getHeight() + 20;
        //guiGraphics.drawString(font, efficiencyText, efficiencyX, efficiencyY, 0xFF808080, false);
    }
    private final IDrawable jeiArrow;

}
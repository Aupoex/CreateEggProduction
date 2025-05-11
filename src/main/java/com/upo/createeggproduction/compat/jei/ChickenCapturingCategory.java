package com.upo.createeggproduction.compat.jei;

import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import com.upo.createeggproduction.CreateEggProduction;
import com.upo.createeggproduction.compat.jei.recipes.ChickenCapturingRecipe; 
import com.upo.createeggproduction.ModBlocks; 

public class ChickenCapturingCategory implements IRecipeCategory<ChickenCapturingRecipe> {
    public static final RecipeType<ChickenCapturingRecipe> TYPE =
            RecipeType.create(CreateEggProduction.MODID, "chicken_capturing", ChickenCapturingRecipe.class);
    private final Component title;
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable slotDrawable;
    public ChickenCapturingCategory(IGuiHelper guiHelper) {
        this.title = Component.translatable("jei.createeggproduction.recipe.chicken_capturing");
        this.background = guiHelper.createBlankDrawable(100, 40);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.EMPTY_EGG_COLLECTOR_BLOCK_ITEM.get()));
        this.slotDrawable = guiHelper.getSlotDrawable();
        this.jeiArrow = guiHelper.drawableBuilder(AllGuiTextures.JEI_ARROW.getLocation(),
                        AllGuiTextures.JEI_ARROW.getStartX(), AllGuiTextures.JEI_ARROW.getStartY(),
                        AllGuiTextures.JEI_ARROW.getWidth(), AllGuiTextures.JEI_ARROW.getHeight())
                .build();
    }
    @Override public RecipeType<ChickenCapturingRecipe> getRecipeType() { return TYPE; }
    @Override public Component getTitle() { return this.title; }
    @Override public IDrawable getBackground() { return this.background; }
    @Override public IDrawable getIcon() { return this.icon; }
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ChickenCapturingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 12)
                .addItemStack(recipe.getInputItem())
                .setBackground(slotDrawable, -1, -1);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 74, 12)
                .addItemStack(recipe.getOutputItem())
                .setBackground(slotDrawable, -1, -1);
    }
    @Override
    public void draw(ChickenCapturingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        int arrowX = 30;
        int arrowY = 13;
        jeiArrow.draw(guiGraphics, arrowX, arrowY);
        Component transformText = Component.translatable("jei.createeggproduction.recipe.capture");
        Font font = Minecraft.getInstance().font;
        int textWidth = font.width(transformText);
        int textX = arrowX + (jeiArrow.getWidth() - textWidth) / 2;
        int textY = arrowY - 10;
        guiGraphics.drawString(font, transformText, textX, textY, 0xFF808080, false); 
    }
    private final IDrawable jeiArrow;
}

package com.sirsquidly.palebloom.compat;

import com.sirsquidly.palebloom.init.JTPGBlocks;
import com.sirsquidly.palebloom.paleBloom;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

public class PolleneheadHybridizeCategory implements IRecipeCategory<BasicRecipeWrapper>
{
    public static final String UID = paleBloom.MOD_ID + ".pollenhead_hybridizations";

    private static final ResourceLocation BACKGROUND = new ResourceLocation(paleBloom.MOD_ID, "textures/gui/hybridize_recipe.png");
    private final IDrawable background;
    private final IDrawable icon;
    private final String name;

    public PolleneheadHybridizeCategory(IGuiHelper guiHelper)
    {
        this.background = guiHelper.createDrawable(BACKGROUND, 0, 0, 100, 40);
        //this.background = guiHelper.createBlankDrawable(120, 40);
        icon = guiHelper.createDrawableIngredient(new ItemStack(JTPGBlocks.POLLENHEAD));
        this.name = I18n.translateToLocal("jei.palebloom.category.pollenhead_hybridization");
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, BasicRecipeWrapper wrapper, IIngredients ingredients)
    {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
        stacks.init(0, true, 9, 17);
        stacks.init(1, false, 74, 17);

        stacks.set(ingredients);
    }

    public IDrawable getBackground() { return background; }

    public IDrawable getIcon() {
        return icon;
    }

    public String getTitle() { return name; }

    public String getModName() { return paleBloom.MOD_ID; }

    public String getUid() { return UID; }
}
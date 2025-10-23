package com.sirsquidly.palebloom.compat;

import com.sirsquidly.palebloom.config.ConfigParser;
import com.sirsquidly.palebloom.init.JTPGBlocks;
import com.sirsquidly.palebloom.util.IncenseThornsNBTRecipe;
import com.sirsquidly.palebloom.util.PaleMossCloakGraftingRecipe;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.plugins.vanilla.crafting.ShapedRecipesWrapper;
import mezz.jei.plugins.vanilla.crafting.ShapelessRecipeWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;
import java.util.List;

@JEIPlugin
public class CompatJEI implements IModPlugin
{
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry)
    {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(new PolleneheadHybridizeCategory(guiHelper));
    }

    @Override
    public void register(IModRegistry registry)
    {
        IJeiHelpers helpers = registry.getJeiHelpers();

        addInformation(registry);

        registry.handleRecipes(IncenseThornsNBTRecipe.class, recipe -> new ShapedRecipesWrapper(helpers, recipe.getInternalRecipe()), VanillaRecipeCategoryUid.CRAFTING);
        registry.handleRecipes(PaleMossCloakGraftingRecipe.class, recipe -> new ShapelessRecipeWrapper<>(helpers, recipe.getInternalRecipe()), VanillaRecipeCategoryUid.CRAFTING);

        registry.handleRecipes(BasicRecipeWrapper.class, r -> r, PolleneheadHybridizeCategory.UID);
        registry.addRecipes(getPollenheadHybrids(), PolleneheadHybridizeCategory.UID);

        // Optional: Add a catalyst (block that shows the category)
        registry.addRecipeCatalyst(new ItemStack(JTPGBlocks.POLLENHEAD), PolleneheadHybridizeCategory.UID);



        IIngredientBlacklist ingredientBlacklist = registry.getJeiHelpers().getIngredientBlacklist();
        /* Hide the dang Double Slabs from JEI! */
		ingredientBlacklist.addIngredientToBlacklist(new ItemStack(JTPGBlocks.PALE_OAK_SLAB_D));
        ingredientBlacklist.addIngredientToBlacklist(new ItemStack(JTPGBlocks.RESIN_BRICKS_SLAB_D));
    }

    /** Attaches JEI Descriptions to the items which it is helpful for. */
    public void addInformation(IModRegistry registry)
    {
        registry.addIngredientInfo(new ItemStack(JTPGBlocks.INCENSE_THORNS), VanillaTypes.ITEM, "jei.palebloom.incense_thorns.desc");
        registry.addIngredientInfo(new ItemStack(JTPGBlocks.PALE_OAK_HOLLOW), VanillaTypes.ITEM, "jei.palebloom.pale_oak_hollow.desc");
        registry.addIngredientInfo(new ItemStack(JTPGBlocks.POLLENHEAD), VanillaTypes.ITEM, "jei.palebloom.pollenhead.desc");
        registry.addIngredientInfo(new ItemStack(JTPGBlocks.RESIN_BULB), VanillaTypes.ITEM, "jei.palebloom.resin_bulb.desc");
    }

    /** Parses the config-defined Pollenhead conversions and puts them into the basic recipe wrapper (This -> That) */
    public static List<BasicRecipeWrapper> getPollenheadHybrids()
    {
        List<BasicRecipeWrapper> recipes = new ArrayList<>();
        List<IBlockState> from = ConfigParser.blockPollenheadHybridFROM;
        List<IBlockState> to = ConfigParser.blockPollenheadHybridTO;

        for (int i = 0; i < from.size(); i++)
        {
            ItemStack input = new ItemStack(from.get(i).getBlock(), 1, getValidMeta(Item.getItemFromBlock(from.get(i).getBlock()), from.get(i).getBlock().getMetaFromState(from.get(i))));
            ItemStack output = new ItemStack(to.get(i).getBlock(), 1, getValidMeta(Item.getItemFromBlock(to.get(i).getBlock()), to.get(i).getBlock().getMetaFromState(to.get(i))));
            recipes.add(new BasicRecipeWrapper(input, output));
        }
        return recipes;
    }

    /** Check if the given block metadata converts properly to the item! */
    private static int getValidMeta(Item item, int meta)
    {
        NonNullList<ItemStack> subItems = NonNullList.create();
        item.getSubItems(CreativeTabs.SEARCH, subItems);
        for (ItemStack stack : subItems)
        {
            if (stack.getMetadata() == meta) return meta;
        }
        return 0;
    }
}
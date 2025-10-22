package com.sirsquidly.palebloom.util;

import com.google.gson.JsonObject;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class IncenseThornsNBTRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{
    private final ItemStack ingredient;
    private final ItemStack result;
    private final String potionEffect;
    private final ShapedRecipes internal;

    public IncenseThornsNBTRecipe(ItemStack ingredientIn, ItemStack resultIn, String potionEffectIn)
    {
        this.ingredient = ingredientIn;
        this.result = resultIn;
        this.potionEffect = potionEffectIn;

        NonNullList<net.minecraft.item.crafting.Ingredient> ingredients = NonNullList.withSize(9, net.minecraft.item.crafting.Ingredient.EMPTY);

        ingredients.set(1, net.minecraft.item.crafting.Ingredient.fromStacks(ingredient));
        ingredients.set(3, net.minecraft.item.crafting.Ingredient.fromStacks(ingredient));
        ingredients.set(4, net.minecraft.item.crafting.Ingredient.fromStacks(result));
        ingredients.set(5, net.minecraft.item.crafting.Ingredient.fromStacks(ingredient));
        ingredients.set(7, net.minecraft.item.crafting.Ingredient.fromStacks(ingredient));

        /*
        * Immediately establish the NBT of the output item.
        * This is done here to avoid copied code in `getCraftingResult` and `getRecipeOutput` , and works better
        * with JEI.
        */
        ItemStack outputWithNBT = result.copy();
        NBTTagCompound nbt = result.hasTagCompound() ? result.getTagCompound() : new NBTTagCompound();
        nbt.setString("potion", potionEffect);
        outputWithNBT.setTagCompound(nbt);

        this.internal = new ShapedRecipes( "incense_effect", 3, 3, ingredients, outputWithNBT);
    }

    public ItemStack getCraftingResult(InventoryCrafting inv) { return internal.getCraftingResult(inv); }

    public ItemStack getRecipeOutput() { return internal.getRecipeOutput(); }

    public boolean matches(InventoryCrafting inv, World worldIn) { return internal.matches(inv, worldIn); }

    public NonNullList<Ingredient> getIngredients() { return internal.getIngredients(); }

    public String getGroup() { return internal.getGroup(); }

    public boolean canFit(int width, int height) { return internal.canFit(width, height); }

    /** Used for JEI. */
    public ShapedRecipes getInternalRecipe() { return internal; }

    public static class Factory implements IRecipeFactory
    {
        @Override
        public IRecipe parse(JsonContext context, JsonObject json)
        {
            ItemStack ingredient = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "ingredient"), context);
            ItemStack result = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);
            String potion = json.get("potion").getAsString();

            return new IncenseThornsNBTRecipe(ingredient, result, potion);
        }
    }
}
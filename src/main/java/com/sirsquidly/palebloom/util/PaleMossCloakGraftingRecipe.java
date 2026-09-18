package com.sirsquidly.palebloom.util;

import com.google.gson.JsonObject;
import com.sirsquidly.palebloom.common.item.ItemPaleMossCloak;
import com.sirsquidly.palebloom.init.JTPGItems;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
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
public class PaleMossCloakGraftingRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{
    private final ItemStack scionIngredient;
    private final ItemStack bindingIngredient;
    private final String scionEffect;
    private final ShapelessRecipes internal;

    public PaleMossCloakGraftingRecipe(ItemStack scionIngredientIn, ItemStack bindingIngredientIn, String scionEffectIn)
    {
        this.scionIngredient = scionIngredientIn;
        this.bindingIngredient = bindingIngredientIn;
        this.scionEffect = scionEffectIn;

        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(Ingredient.fromStacks(scionIngredient));
        ingredients.add(Ingredient.fromStacks(bindingIngredient));
        ingredients.add(Ingredient.fromStacks(new ItemStack(JTPGItems.PALE_MOSS_CLOAK)));

        /*
         * Immediately establish the NBT of the output item.
         * This is done here to avoid copied code in `getCraftingResult` and `getRecipeOutput` , and works better
         * with JEI.
         */
        ItemStack outputWithNBT = createOutputItem(new ItemStack(JTPGItems.PALE_MOSS_CLOAK), scionEffectIn);

        this.internal = new ShapelessRecipes( "pale_moss_cloak_grafting", outputWithNBT, ingredients);
    }

    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        for (int k = 0; k < inv.getSizeInventory(); ++k)
        {
            ItemStack stack = inv.getStackInSlot(k);
            if (!stack.isEmpty() && stack.getItem() == JTPGItems.PALE_MOSS_CLOAK)
            {
                return createOutputItem(stack.copy(), scionEffect);
            }
        }

        return createOutputItem(new ItemStack(JTPGItems.PALE_MOSS_CLOAK), scionEffect);
    }

    /** Returns an itemstack with the Scion Ability attached to an open slot. */
    private static ItemStack createOutputItem(ItemStack baseCloak, String scionEffect)
    {
        ItemStack result = baseCloak.copy();
        NBTTagCompound nbt = result.hasTagCompound() ? result.getTagCompound() : new NBTTagCompound();

        ItemPaleMossCloak cloak = (ItemPaleMossCloak) JTPGItems.PALE_MOSS_CLOAK;
        int slots = cloak.getAbilitySlots(result);

        for (int i = 1; i <= slots; i++)
        {
            String key = "slot" + i;
            if (!nbt.hasKey(key))
            { nbt.setString(key, scionEffect); break; }
        }

        result.setTagCompound(nbt);
        return result;
    }

    public ItemStack getRecipeOutput()
    { return createOutputItem(new ItemStack(JTPGItems.PALE_MOSS_CLOAK), scionEffect); }

    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        if (!internal.matches(inv, worldIn)) return false;

        ItemStack cloakStack = ItemStack.EMPTY;
        for (int i = 0; i < inv.getSizeInventory(); i++)
        {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() == JTPGItems.PALE_MOSS_CLOAK)
            {
                cloakStack = stack;
                break;
            }
        }

        if (cloakStack.isEmpty()) return false;

        ItemPaleMossCloak cloak = (ItemPaleMossCloak) JTPGItems.PALE_MOSS_CLOAK;
        int slots = cloak.getAbilitySlots(cloakStack);

        NBTTagCompound nbt = cloakStack.getTagCompound();
        if (nbt == null) return slots > 0;

        for (int i = 1; i <= slots; i++)
        {
            if (!nbt.hasKey("slot" + i)) return true;
        }

        return false;
    }

    public NonNullList<Ingredient> getIngredients() { return internal.getIngredients(); }

    public String getGroup() { return internal.getGroup(); }

    public boolean canFit(int width, int height) { return internal.canFit(width, height); }

    /** Used for JEI. */
    public ShapelessRecipes getInternalRecipe() { return internal; }

    public static class Factory implements IRecipeFactory
    {
        @Override
        public IRecipe parse(JsonContext context, JsonObject json)
        {
            ItemStack scionIngredient = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "scion_ingredient"), context);
            ItemStack bindingIngredient = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "binding_ingredient"), context);
            String scionEffect = json.get("scion_effect").getAsString();

            return new PaleMossCloakGraftingRecipe(scionIngredient, bindingIngredient, scionEffect);
        }
    }
}
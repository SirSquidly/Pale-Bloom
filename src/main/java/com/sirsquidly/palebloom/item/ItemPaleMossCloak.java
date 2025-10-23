package com.sirsquidly.palebloom.item;

import com.sirsquidly.palebloom.client.model.armor.ModelPalePoncho;
import com.sirsquidly.palebloom.paleBloom;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.*;

/** Functions are preformed using Events within 'CommonEvents'.
 *      These include:
 * `onCreeperTargetMossCloakWearer` = Triggers when a Creeper targets an entity wearing a Pale Moss Cloak, thus canceling
 * `onMossCloakWearerHarmed` = Triggers when an entity wearing a Pale Moss Cloak is harmed, thus calling a Reaping Willow
 * */
public class ItemPaleMossCloak extends ItemArmor
{
    private static final UUID chestplateUUID = UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E");

    private final String PALE_MOSS_CLOAK_TEXTURE = paleBloom.MOD_ID + ":textures/models/pale_moss_cloak/pale_moss_cloak.png";

    public int damageReduceAmount;

    public ItemPaleMossCloak(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn)
    {
        super(materialIn, renderIndexIn, equipmentSlotIn);
        setMaxStackSize(1);
    }

    public void onArmorTick(World world, EntityPlayer player, ItemStack stack)
    {
        if (world.isRemote) return;

        Map<String, String> scionAbilities = getCachedScionList(stack);

        if (scionAbilities.containsValue("pale_oak_sapling"))
        {
            double dx = player.posX - player.prevPosX;
            double dz = player.posZ - player.prevPosZ;
            boolean isMoving = (dx * dx + dz * dz) > 0.0001D;

            if (!isMoving && player.onGround)
            {
                player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 21, 1));
            }
        }
        if (scionAbilities.containsValue("sucker_roots") && stack.getItemDamage() > 0 && player.getHealth() > 1)
        {
            if (player.ticksExisted % 40 == 0)
            {
                player.attackEntityFrom(DamageSource.CACTUS, 1.0F);
                stack.setItemDamage(stack.getItemDamage() - 2);
            }
        }
        if (scionAbilities.containsValue("live_root"))
        {
            String targetSlot = getScionSlot(scionAbilities, "live_root");
            if (targetSlot != null)
            {
                stack.addAttributeModifier("generic.armor", new AttributeModifier(chestplateUUID, "Live Root Modifier", 6, 0), EntityEquipmentSlot.CHEST);

                NBTTagCompound nbt = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
                nbt.setString(targetSlot, "live_root.preformed");
                stack.setTagCompound(nbt);
            }
        }
    }

    /** Simply returns a cache of every ability on this item. */
    public Map<String, String> getCachedScionList(ItemStack stack)
    {
        Map<String, String> scions = new HashMap<>();
        if (stack == null || !stack.hasTagCompound()) return scions;
        NBTTagCompound nbt = stack.getTagCompound();

        for (String key : nbt.getKeySet())
        {
            if (key.startsWith("slot"))
            {
                String ability = nbt.getString(key);
                if (!ability.isEmpty()) scions.put(key, ability);
            }
        }

        return scions;
    }

    /** Feed this `getCachedScionList` + the Scion name to get the exact slot it is in. */
    public String getScionSlot(Map<String, String> cachedMap, String scionIn)
    {
        String targetSlot = null;
        for (Map.Entry<String, String> entry : cachedMap.entrySet())
        {
            if (scionIn.equals(entry.getValue()))
            {
                targetSlot = entry.getKey();
                break;
            }
        }
        return targetSlot;
    }

    public boolean hasSlottedAbility(ItemStack stack, String ability)
    {
        Map<String, String> itemAbilities = getCachedScionList(stack);
        return itemAbilities.containsValue(ability);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default)
    { return new ModelPalePoncho(0.0F); }

    @Override
    @SideOnly(Side.CLIENT)
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
    { return PALE_MOSS_CLOAK_TEXTURE; }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.GRAY + I18n.format("description.palebloom.pale_moss_cloak.base_desc"));

        NBTTagCompound nbttagcompound = stack.getTagCompound();

        if (nbttagcompound != null && nbttagcompound.hasKey("slot1"))
        {
            tooltip.add(TextFormatting.BLUE + net.minecraft.util.text.translation.I18n.translateToLocal("description.palebloom.pale_moss_cloak." + nbttagcompound.getString("slot1")));
        }
        else
        { tooltip.add(TextFormatting.GRAY + net.minecraft.util.text.translation.I18n.translateToLocal("description.palebloom.pale_moss_cloak.slot_empty")); }

        if (nbttagcompound != null && nbttagcompound.hasKey("slot2"))
        {
            tooltip.add(TextFormatting.BLUE + net.minecraft.util.text.translation.I18n.translateToLocal("description.palebloom.pale_moss_cloak." + nbttagcompound.getString("slot2")));
        }
        else
        { tooltip.add(TextFormatting.GRAY + net.minecraft.util.text.translation.I18n.translateToLocal("description.palebloom.pale_moss_cloak.slot_empty")); }
    }
}
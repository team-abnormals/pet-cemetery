package com.teamabnormals.pet_cemetery.common.item;

import com.teamabnormals.pet_cemetery.core.PetCemetery;
import com.teamabnormals.pet_cemetery.core.other.PCUtil;
import com.teamabnormals.pet_cemetery.core.other.tags.PCEntityTypeTags;
import com.teamabnormals.pet_cemetery.core.registry.PCDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

public class PetCollarItem extends Item {

	public PetCollarItem(Properties properties) {
		super(properties);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
		CompoundTag tag = stack.getOrDefault(PCDataComponents.PET_DATA.get(), CustomData.EMPTY).copyTag();
		if (tag.contains(PCUtil.PET_ID)) {
			String petID = tag.getString(PCUtil.PET_ID);
			EntityType<?> pet = BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(petID));
			tooltip.add(Component.translatable(pet.getDescriptionId()).withStyle(ChatFormatting.GRAY));
			if (tag.contains(PCUtil.PET_VARIANT)) {
				String texture = "";

				if (pet.is(PCEntityTypeTags.CATS)) {
					ResourceLocation catVariant = ResourceLocation.parse(tag.getString(PCUtil.PET_VARIANT));
					texture = "cat_variant." + catVariant.getNamespace() + "." + catVariant.getPath();
				}

				if (pet.is(PCEntityTypeTags.PARROTS)) {
					texture = "parrot_variant.minecraft." + Parrot.Variant.byId(tag.getInt(PCUtil.PET_VARIANT)).getSerializedName();
				}

				if (pet.is(PCEntityTypeTags.WOLVES)) {
					ResourceLocation wolfVariant = ResourceLocation.parse(tag.getString(PCUtil.PET_VARIANT));
					texture = "wolf_variant." + wolfVariant.getNamespace() + "." + wolfVariant.getPath();
				}


				tooltip.add(Component.translatable(texture).withStyle(ChatFormatting.GRAY));
			}

			if (tag.getBoolean(PCUtil.IS_CHILD)) {
				tooltip.add(Component.translatable("tooltip." + PetCemetery.MOD_ID + ".baby").withStyle(ChatFormatting.GRAY));
			}
		}

		super.appendHoverText(stack, context, tooltip, flagIn);
	}

	public int getColor(ItemStack stack) {
		CompoundTag tag = stack.getOrDefault(PCDataComponents.PET_DATA.get(), CustomData.EMPTY).copyTag();
		DyeColor color = tag.contains(PCUtil.COLLAR_COLOR) ? DyeColor.byId(tag.getInt(PCUtil.COLLAR_COLOR)) : DyeColor.RED;
		return color.getTextureDiffuseColor();
	}
}
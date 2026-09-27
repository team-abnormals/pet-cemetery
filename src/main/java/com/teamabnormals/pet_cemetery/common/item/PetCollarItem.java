package com.teamabnormals.pet_cemetery.common.item;

import com.teamabnormals.pet_cemetery.core.PetCemetery;
import com.teamabnormals.pet_cemetery.core.other.PCUtil;
import com.teamabnormals.pet_cemetery.core.other.tags.PCEntityTypeTags;
import com.teamabnormals.pet_cemetery.core.registry.PCDataComponents;
import com.teamabnormals.pet_cemetery.core.registry.PCEntityTypes;
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

			if (pet.is(PCEntityTypeTags.PARROTS) && tag.contains("Variant")) {
				String texture = "parrot_variant.minecraft." + Parrot.Variant.byId(tag.getInt("Variant")).getSerializedName();
				tooltip.add(Component.translatable(texture).withStyle(ChatFormatting.GRAY));
			} else {
				PCEntityTypes.VariantTooltip variantTooltip = pet.builtInRegistryHolder().getData(PCEntityTypes.PET_VARIANT_TOOLTIPS);
				if (variantTooltip != null) {
					String variantKey = tag.contains("variant") ? "variant" : tag.contains("Variant") ? "Variant" : null;
					if (variantKey != null) {
						ResourceLocation variant = ResourceLocation.parse(tag.getString(variantKey));
						String texture = variantTooltip.translationPrefix() + "." + variant.getNamespace() + "." + variant.getPath();
						tooltip.add(Component.translatable(texture).withStyle(ChatFormatting.GRAY));
					}
				}
			}
			if (tag.getInt("Age") < 0) {
				tooltip.add(Component.translatable("tooltip." + PetCemetery.MOD_ID + ".baby").withStyle(ChatFormatting.GRAY));
			}
		}

		super.appendHoverText(stack, context, tooltip, flagIn);
	}

	public int getColor(ItemStack stack) {
		CompoundTag tag = stack.getOrDefault(PCDataComponents.PET_DATA.get(), CustomData.EMPTY).copyTag();
		DyeColor color = tag.contains("CollarColor") ? DyeColor.byId(tag.getInt("CollarColor")) : DyeColor.RED;
		return color.getTextureDiffuseColor();
	}
}
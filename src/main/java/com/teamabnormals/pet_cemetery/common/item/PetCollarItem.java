package com.teamabnormals.pet_cemetery.common.item;

import com.teamabnormals.pet_cemetery.core.PetCemetery;
import com.teamabnormals.pet_cemetery.core.other.PCUtil;
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
import org.apache.commons.lang3.text.WordUtils;

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

			Component petType = Component.translatable(pet.getDescriptionId()).withStyle(ChatFormatting.GRAY);
			if (tag.contains(PCUtil.PET_VARIANT)) {
				String texture = "";

				if (pet == EntityType.CAT || pet == PCEntityTypes.ZOMBIE_CAT.get()) {
					ResourceLocation catVariant = ResourceLocation.parse(tag.getString(PCUtil.PET_VARIANT));
					texture = catVariant.getPath();
				}

				if (pet == EntityType.PARROT || pet == PCEntityTypes.ZOMBIE_PARROT.get()) {
					texture = Parrot.Variant.byId(tag.getInt(PCUtil.PET_VARIANT)).getSerializedName();
				}

				if (pet == EntityType.WOLF || pet == PCEntityTypes.ZOMBIE_WOLF.get()) {
					ResourceLocation wolfVariant = ResourceLocation.parse(tag.getString(PCUtil.PET_VARIANT));
					texture = wolfVariant.getPath();
				}

				texture = texture.replace("_", " ").concat(" ");
				petType = Component.literal(WordUtils.capitalize(texture)).withStyle(ChatFormatting.GRAY).append(petType);
			}

			if (tag.getBoolean(PCUtil.IS_CHILD))
				petType = Component.translatable("tooltip." + PetCemetery.MOD_ID + ".baby").withStyle(ChatFormatting.GRAY).append(" ").append(petType);

			tooltip.add(petType);
		}

		super.appendHoverText(stack, context, tooltip, flagIn);
	}

	public int getColor(ItemStack stack) {
		CompoundTag tag = stack.getOrDefault(PCDataComponents.PET_DATA.get(), CustomData.EMPTY).copyTag();
		DyeColor color = tag.contains(PCUtil.COLLAR_COLOR) ? DyeColor.byId(tag.getInt(PCUtil.COLLAR_COLOR)) : DyeColor.RED;
		return color.getTextureDiffuseColor();
	}
}
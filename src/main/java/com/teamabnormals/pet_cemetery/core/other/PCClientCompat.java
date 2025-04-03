package com.teamabnormals.pet_cemetery.core.other;

import com.teamabnormals.pet_cemetery.common.item.PetCollarItem;
import com.teamabnormals.pet_cemetery.core.registry.PCItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;

public class PCClientCompat {
	public static void register() {
		PCItems.setupTabEditors();
		registerItemColors();
	}

	public static void registerItemColors() {
		ItemColors itemColors = Minecraft.getInstance().getItemColors();
		itemColors.register((stack, color) -> color > 0 ? -1 : ((PetCollarItem) stack.getItem()).getColor(stack), PCItems.PET_COLLAR.get());
	}
}
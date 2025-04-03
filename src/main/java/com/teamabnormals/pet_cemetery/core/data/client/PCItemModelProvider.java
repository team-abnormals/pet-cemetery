package com.teamabnormals.pet_cemetery.core.data.client;

import com.teamabnormals.pet_cemetery.core.PetCemetery;
import com.teamabnormals.pet_cemetery.core.registry.PCItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class PCItemModelProvider extends ItemModelProvider {

	public PCItemModelProvider(PackOutput output, ExistingFileHelper helper) {
		super(output, PetCemetery.MOD_ID, helper);
	}

	@Override
	protected void registerModels() {
		this.generatedWithOverlay(PCItems.PET_COLLAR.get());

		this.spawnEgg(PCItems.ZOMBIE_WOLF_SPAWN_EGG.get());
		this.spawnEgg(PCItems.ZOMBIE_CAT_SPAWN_EGG.get());
		this.spawnEgg(PCItems.ZOMBIE_PARROT_SPAWN_EGG.get());
		this.spawnEgg(PCItems.SKELETON_WOLF_SPAWN_EGG.get());
		this.spawnEgg(PCItems.SKELETON_CAT_SPAWN_EGG.get());
		this.spawnEgg(PCItems.SKELETON_PARROT_SPAWN_EGG.get());
	}

	private void generatedWithOverlay(ItemLike item) {
		ResourceLocation itemName = BuiltInRegistries.ITEM.getKey(item.asItem());
		this.withExistingParent(itemName.getPath(), "item/generated")
				.texture("layer0", ResourceLocation.fromNamespaceAndPath(this.modid, "item/" + itemName.getPath()))
				.texture("layer1", ResourceLocation.fromNamespaceAndPath(this.modid, "item/" + itemName.getPath() + "_overlay"));
	}

	private void spawnEgg(ItemLike item) {
		this.withExistingParent(BuiltInRegistries.ITEM.getKey(item.asItem()).getPath(), "item/template_spawn_egg");
	}
}
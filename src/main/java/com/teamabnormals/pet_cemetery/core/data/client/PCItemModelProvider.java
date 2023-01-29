package com.teamabnormals.pet_cemetery.core.data.client;

import com.teamabnormals.pet_cemetery.core.PetCemetery;
import com.teamabnormals.pet_cemetery.core.registry.PCItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class PCItemModelProvider extends ItemModelProvider {

	public PCItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, PetCemetery.MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		this.generatedWithOverlay(PCItems.FORGOTTEN_COLLAR.get());

		this.spawnEgg(PCItems.ZOMBIE_WOLF_SPAWN_EGG.get());
		this.spawnEgg(PCItems.ZOMBIE_CAT_SPAWN_EGG.get());
		this.spawnEgg(PCItems.ZOMBIE_PARROT_SPAWN_EGG.get());
		this.spawnEgg(PCItems.SKELETON_WOLF_SPAWN_EGG.get());
		this.spawnEgg(PCItems.SKELETON_CAT_SPAWN_EGG.get());
		this.spawnEgg(PCItems.SKELETON_PARROT_SPAWN_EGG.get());
	}

	private void generatedWithOverlay(ItemLike item) {
		ResourceLocation itemName = ForgeRegistries.ITEMS.getKey(item.asItem());
		this.withExistingParent(itemName.getPath(), "item/generated")
				.texture("layer0", new ResourceLocation(this.modid, "item/" + itemName.getPath()))
				.texture("layer1", new ResourceLocation(this.modid, "item/" + itemName.getPath() + "_overlay"));
	}

	private void spawnEgg(ItemLike item) {
		this.withExistingParent(ForgeRegistries.ITEMS.getKey(item.asItem()).getPath(), "item/template_spawn_egg");
	}
}
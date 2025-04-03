package com.teamabnormals.pet_cemetery.core.data.client;

import com.teamabnormals.blueprint.core.data.client.BlueprintItemModelProvider;
import com.teamabnormals.pet_cemetery.core.PetCemetery;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import static com.teamabnormals.pet_cemetery.core.registry.PCItems.*;

public class PCItemModelProvider extends BlueprintItemModelProvider {

	public PCItemModelProvider(PackOutput output, ExistingFileHelper helper) {
		super(output, PetCemetery.MOD_ID, helper);
	}

	@Override
	protected void registerModels() {
		this.generatedWithOverlay(PET_COLLAR.get());

		this.spawnEggItem(
				ZOMBIE_WOLF_SPAWN_EGG, ZOMBIE_CAT_SPAWN_EGG, ZOMBIE_PARROT_SPAWN_EGG,
				SKELETON_WOLF_SPAWN_EGG, SKELETON_CAT_SPAWN_EGG, SKELETON_PARROT_SPAWN_EGG
		);
	}

	private void generatedWithOverlay(ItemLike item) {
		ResourceLocation itemName = BuiltInRegistries.ITEM.getKey(item.asItem());
		this.withExistingParent(itemName.getPath(), "item/generated")
				.texture("layer0", ResourceLocation.fromNamespaceAndPath(this.modid, "item/" + itemName.getPath()))
				.texture("layer1", ResourceLocation.fromNamespaceAndPath(this.modid, "item/" + itemName.getPath() + "_overlay"));
	}
}
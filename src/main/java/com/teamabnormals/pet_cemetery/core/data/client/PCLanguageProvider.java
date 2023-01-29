package com.teamabnormals.pet_cemetery.core.data.client;

import com.teamabnormals.pet_cemetery.core.PetCemetery;
import com.teamabnormals.pet_cemetery.core.registry.PCEntityTypes;
import com.teamabnormals.pet_cemetery.core.registry.PCItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.text.WordUtils;

public class PCLanguageProvider extends LanguageProvider {

	public PCLanguageProvider(DataGenerator gen) {
		super(gen, PetCemetery.MOD_ID, "en_us");
	}

	@Override
	public void addTranslations() {
		this.add(PCItems.PET_COLLAR.get());
		this.add(PCItems.ZOMBIE_WOLF_SPAWN_EGG.get(), PCItems.ZOMBIE_CAT_SPAWN_EGG.get(), PCItems.ZOMBIE_PARROT_SPAWN_EGG.get(), PCItems.SKELETON_WOLF_SPAWN_EGG.get(), PCItems.SKELETON_CAT_SPAWN_EGG.get(), PCItems.SKELETON_PARROT_SPAWN_EGG.get());
		this.add(PCEntityTypes.ZOMBIE_WOLF.get(), PCEntityTypes.ZOMBIE_CAT.get(), PCEntityTypes.ZOMBIE_PARROT.get(), PCEntityTypes.SKELETON_WOLF.get(), PCEntityTypes.SKELETON_CAT.get(), PCEntityTypes.SKELETON_PARROT.get());
		this.add("tooltip." + PetCemetery.MOD_ID + ".baby", "Baby");
		this.addAdvancement("nether.respawn_pet", "Pet Cemetery", "Respawn a pet using a Pet Collar at a charged Respawn Anchor");
	}

	private void add(Item... entries) {
		for (Item entry : entries) {
			if (ForgeRegistries.ITEMS.getKey(entry) != null)
				this.add(entry, format(ForgeRegistries.ITEMS.getKey(entry)));
		}
	}

	private void add(EntityType<?>... entries) {
		for (EntityType<?> entry : entries) {
			if (ForgeRegistries.ENTITY_TYPES.getKey(entry) != null)
				this.add(entry, format(ForgeRegistries.ENTITY_TYPES.getKey(entry)));
		}
	}

	private void addAdvancement(String key, String title, String description) {
		String intro = "advancements." + PetCemetery.MOD_ID + "." + key + ".";
		this.add(intro + "title", title);
		this.add(intro + "description", description);
	}

	private String format(ResourceLocation registryName) {
		return WordUtils.capitalizeFully(registryName.getPath().replace("_", " "));
	}
}
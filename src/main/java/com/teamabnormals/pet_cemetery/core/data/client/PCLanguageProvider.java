package com.teamabnormals.pet_cemetery.core.data.client;

import com.teamabnormals.blueprint.core.data.client.BlueprintLanguageProvider;
import com.teamabnormals.pet_cemetery.core.PetCemetery;
import com.teamabnormals.pet_cemetery.core.registry.PCEntityTypes;
import com.teamabnormals.pet_cemetery.core.registry.PCItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.Parrot.Variant;
import net.minecraft.world.entity.animal.WolfVariant;
import net.minecraft.world.entity.animal.WolfVariants;

public class PCLanguageProvider extends BlueprintLanguageProvider {

	public PCLanguageProvider(PackOutput output) {
		super(output, PetCemetery.MOD_ID);
	}

	@Override
	public void addTranslations() {
		this.add(PCItems.PET_COLLAR.get());
		this.add(PCItems.ZOMBIE_WOLF_SPAWN_EGG.get(), PCItems.ZOMBIE_CAT_SPAWN_EGG.get(), PCItems.ZOMBIE_PARROT_SPAWN_EGG.get(), PCItems.SKELETON_WOLF_SPAWN_EGG.get(), PCItems.SKELETON_CAT_SPAWN_EGG.get(), PCItems.SKELETON_PARROT_SPAWN_EGG.get());
		this.add(PCEntityTypes.ZOMBIE_WOLF.get(), PCEntityTypes.ZOMBIE_CAT.get(), PCEntityTypes.ZOMBIE_PARROT.get(), PCEntityTypes.SKELETON_WOLF.get(), PCEntityTypes.SKELETON_CAT.get(), PCEntityTypes.SKELETON_PARROT.get());
		this.add("tooltip." + PetCemetery.MOD_ID + ".baby", "Baby");
		this.addAdvancement("nether.respawned_pet", "Frankenweenie", "Respawn a pet using a Pet Collar at a Respawn Anchor");
		this.addAdvancement("nether.cured_zombie_pet", "Zombie Veterinarian", "Weaken and then cure a Zombie pet");
		this.addAdvancement("nether.respawned_zombie_pet", "Grim Reaper", "Respawn a Zombie pet as a Skeleton");

		// Some names don't match the in-code names because they are based on the ones on Minecraft wiki.
		this.addCatVariant(CatVariant.TABBY, "Tabby");
		this.addCatVariant(CatVariant.BLACK, "Tuxedo");
		this.addCatVariant(CatVariant.RED, "Red");
		this.addCatVariant(CatVariant.SIAMESE, "Siamese");
		this.addCatVariant(CatVariant.BRITISH_SHORTHAIR, "British Shorthair");
		this.addCatVariant(CatVariant.CALICO, "Calico");
		this.addCatVariant(CatVariant.PERSIAN, "Persian");
		this.addCatVariant(CatVariant.RAGDOLL, "Ragdoll");
		this.addCatVariant(CatVariant.WHITE, "White");
		this.addCatVariant(CatVariant.JELLIE, "Jellie");
		this.addCatVariant(CatVariant.ALL_BLACK, "Black");

		this.addParrotVariant(Variant.RED_BLUE, "Red");
		this.addParrotVariant(Variant.BLUE, "Blue");
		this.addParrotVariant(Variant.GREEN, "Green");
		this.addParrotVariant(Variant.YELLOW_BLUE, "Cyan");
		this.addParrotVariant(Variant.GRAY, "Gray");

		this.addWolfVariant(WolfVariants.PALE, "Pale");
		this.addWolfVariant(WolfVariants.ASHEN, "Ashen");
		this.addWolfVariant(WolfVariants.BLACK, "Black");
		this.addWolfVariant(WolfVariants.CHESTNUT, "Chestnut");
		this.addWolfVariant(WolfVariants.RUSTY, "Rusty");
		this.addWolfVariant(WolfVariants.SNOWY, "Snowy");
		this.addWolfVariant(WolfVariants.SPOTTED, "Spotted");
		this.addWolfVariant(WolfVariants.STRIPED, "Striped");
		this.addWolfVariant(WolfVariants.WOODS, "Woods");
	}

	private void add(EntityType<?>... entries) {
		for (EntityType<?> entry : entries) {
			this.add(entry, format(BuiltInRegistries.ENTITY_TYPE.getKey(entry)));
		}
	}

	private void addCatVariant(ResourceKey<CatVariant> catVariant, String name) {
		ResourceLocation texture = catVariant.location();
		this.add("cat_variant." + texture.getNamespace() + "." + texture.getPath(), name);
	}

	private void addParrotVariant(Parrot.Variant parrotVariant, String name) {
		this.add("parrot_variant.minecraft." + parrotVariant.getSerializedName(), name);
	}

	private void addWolfVariant(ResourceKey<WolfVariant> wolfVariant, String name) {
		ResourceLocation texture = wolfVariant.location();
		this.add("wolf_variant." + texture.getNamespace() + "." + texture.getPath(), name);
	}

	private void addAdvancement(String key, String title, String description) {
		String intro = "advancements." + PetCemetery.MOD_ID + "." + key + ".";
		this.add(intro + "title", title);
		this.add(intro + "description", description);
	}
}
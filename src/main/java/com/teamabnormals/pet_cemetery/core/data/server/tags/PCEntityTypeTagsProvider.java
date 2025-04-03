package com.teamabnormals.pet_cemetery.core.data.server.tags;

import com.teamabnormals.pet_cemetery.core.PetCemetery;
import com.teamabnormals.pet_cemetery.core.other.tags.PCEntityTypeTags;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

import static com.teamabnormals.pet_cemetery.core.registry.PCEntityTypes.*;

public class PCEntityTypeTagsProvider extends EntityTypeTagsProvider {

	public PCEntityTypeTagsProvider(PackOutput output, CompletableFuture<Provider> provider, ExistingFileHelper helper) {
		super(output, provider, PetCemetery.MOD_ID, helper);
	}

	@Override
	public void addTags(Provider provider) {
		this.tag(PCEntityTypeTags.ZOMBIE_PETS).add(ZOMBIE_WOLF.get(), ZOMBIE_CAT.get(), ZOMBIE_PARROT.get());
		this.tag(PCEntityTypeTags.SKELETON_PETS).add(SKELETON_WOLF.get(), SKELETON_CAT.get(), SKELETON_PARROT.get());
		this.tag(PCEntityTypeTags.CAN_DROP_COLLAR_UNTAMED);

		this.tag(PCEntityTypeTags.WOLVES).add(EntityType.WOLF, ZOMBIE_WOLF.get(), SKELETON_WOLF.get());
		this.tag(PCEntityTypeTags.CATS).add(EntityType.CAT, ZOMBIE_CAT.get(), SKELETON_CAT.get());
		this.tag(PCEntityTypeTags.PARROTS).add(EntityType.PARROT, ZOMBIE_PARROT.get(), SKELETON_PARROT.get());

		this.tag(EntityTypeTags.UNDEAD).addTag(PCEntityTypeTags.ZOMBIE_PETS).addTag(PCEntityTypeTags.SKELETON_PETS);
	}
}
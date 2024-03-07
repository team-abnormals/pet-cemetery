package com.teamabnormals.pet_cemetery.core.data.server.tags;

import com.teamabnormals.pet_cemetery.core.PetCemetery;
import com.teamabnormals.pet_cemetery.core.other.tags.PCEntityTypeTags;
import com.teamabnormals.pet_cemetery.core.registry.PCEntityTypes;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class PCEntityTypeTagsProvider extends EntityTypeTagsProvider {

	public PCEntityTypeTagsProvider(PackOutput output, CompletableFuture<Provider> provider, ExistingFileHelper helper) {
		super(output, provider, PetCemetery.MOD_ID, helper);
	}

	@Override
	public void addTags(Provider provider) {
		this.tag(PCEntityTypeTags.ZOMBIE_PETS).add(PCEntityTypes.ZOMBIE_WOLF.get(), PCEntityTypes.ZOMBIE_CAT.get(), PCEntityTypes.ZOMBIE_PARROT.get());
		this.tag(PCEntityTypeTags.SKELETON_PETS).add(PCEntityTypes.SKELETON_WOLF.get(), PCEntityTypes.SKELETON_CAT.get(), PCEntityTypes.SKELETON_PARROT.get());
		this.tag(PCEntityTypeTags.DROPS_PET_COLLAR).add(EntityType.WOLF, EntityType.CAT, EntityType.PARROT).addTag(PCEntityTypeTags.ZOMBIE_PETS);
	}
}
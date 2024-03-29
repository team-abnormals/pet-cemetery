package com.teamabnormals.pet_cemetery.core.data.server.tags;

import com.teamabnormals.pet_cemetery.core.PetCemetery;
import com.teamabnormals.pet_cemetery.core.other.tags.PCEntityTypeTags;
import com.teamabnormals.pet_cemetery.core.registry.PCEntityTypes;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;

public class PCEntityTypeTagsProvider extends EntityTypeTagsProvider {

	public PCEntityTypeTagsProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, PetCemetery.MOD_ID, existingFileHelper);
	}

	@Override
	public void addTags() {
		this.tag(PCEntityTypeTags.ZOMBIE_PETS).add(PCEntityTypes.ZOMBIE_WOLF.get(), PCEntityTypes.ZOMBIE_CAT.get(), PCEntityTypes.ZOMBIE_PARROT.get());
		this.tag(PCEntityTypeTags.SKELETON_PETS).add(PCEntityTypes.SKELETON_WOLF.get(), PCEntityTypes.SKELETON_CAT.get(), PCEntityTypes.SKELETON_PARROT.get());
		this.tag(PCEntityTypeTags.DROPS_PET_COLLAR).add(EntityType.WOLF, EntityType.CAT, EntityType.PARROT).addTag(PCEntityTypeTags.ZOMBIE_PETS);
	}
}
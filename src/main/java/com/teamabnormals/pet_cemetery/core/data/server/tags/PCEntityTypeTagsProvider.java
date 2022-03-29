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
		this.tag(PCEntityTypeTags.DROPS_FORGOTTEN_COLLAR).add(EntityType.WOLF, EntityType.CAT, EntityType.PARROT, PCEntityTypes.ZOMBIE_WOLF.get(), PCEntityTypes.ZOMBIE_CAT.get(), PCEntityTypes.ZOMBIE_PARROT.get());
	}
}
package com.teamabnormals.pet_cemetery.core.other.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import com.teamabnormals.pet_cemetery.core.PetCemetery;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.EntityType;

public class PCEntityTypeTags {
	public static final Tag.Named<EntityType<?>> COLLAR_DROP_MOBS = entityTypeTag("collar_drop_mobs");

	private static Tag.Named<EntityType<?>> entityTypeTag(String name) {
		return TagUtil.entityTypeTag(PetCemetery.MOD_ID, name);
	}
}
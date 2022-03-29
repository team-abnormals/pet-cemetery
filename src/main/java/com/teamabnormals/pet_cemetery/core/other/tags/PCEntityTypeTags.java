package com.teamabnormals.pet_cemetery.core.other.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import com.teamabnormals.pet_cemetery.core.PetCemetery;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class PCEntityTypeTags {
	public static final TagKey<EntityType<?>> DROPS_FORGOTTEN_COLLAR = entityTypeTag("drops_forgotten_collar");

	private static TagKey<EntityType<?>> entityTypeTag(String name) {
		return TagUtil.entityTypeTag(PetCemetery.MOD_ID, name);
	}
}
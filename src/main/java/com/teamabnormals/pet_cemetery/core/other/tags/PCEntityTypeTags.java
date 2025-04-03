package com.teamabnormals.pet_cemetery.core.other.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import com.teamabnormals.pet_cemetery.core.PetCemetery;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class PCEntityTypeTags {
	public static final TagKey<EntityType<?>> ZOMBIE_PETS = entityTypeTag("zombie_pets");
	public static final TagKey<EntityType<?>> SKELETON_PETS = entityTypeTag("skeleton_pets");
	public static final TagKey<EntityType<?>> CAN_DROP_COLLAR_UNTAMED = entityTypeTag("can_drop_collar_untamed");

	public static final TagKey<EntityType<?>> WOLVES = TagUtil.entityTypeTag("c", "wolves");
	public static final TagKey<EntityType<?>> CATS = TagUtil.entityTypeTag("c", "cats");
	public static final TagKey<EntityType<?>> PARROTS = TagUtil.entityTypeTag("c", "parrots");

	private static TagKey<EntityType<?>> entityTypeTag(String name) {
		return TagUtil.entityTypeTag(PetCemetery.MOD_ID, name);
	}
}
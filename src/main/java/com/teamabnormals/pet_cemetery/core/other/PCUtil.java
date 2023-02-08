package com.teamabnormals.pet_cemetery.core.other;

import com.google.common.collect.Maps;
import com.teamabnormals.pet_cemetery.core.registry.PCEntityTypes;
import net.minecraft.Util;
import net.minecraft.world.entity.EntityType;

import java.util.Map;

public class PCUtil {
	public static final String PET_ID = "PetID";
	public static final String PET_VARIANT = "PetVariant";

	public static final String COLLAR_COLOR = "CollarColor";
	public static final String IS_CHILD = "IsChild";
	public static final String OWNER_ID = "OwnerID";

	public static final double HEALTH_DIFF = 2.0D;
	public static final double DAMAGE_DIFF = 1.0D;
	public static final float SPEED_DIFF = 0.05F;

	public static final Map<EntityType<?>, EntityType<?>> UNDEAD_MAP = Util.make(Maps.newHashMap(), (map) -> {
		map.put(EntityType.WOLF, PCEntityTypes.ZOMBIE_WOLF.get());
		map.put(EntityType.CAT, PCEntityTypes.ZOMBIE_CAT.get());
		map.put(EntityType.PARROT, PCEntityTypes.ZOMBIE_PARROT.get());
		map.put(PCEntityTypes.ZOMBIE_WOLF.get(), PCEntityTypes.SKELETON_WOLF.get());
		map.put(PCEntityTypes.ZOMBIE_CAT.get(), PCEntityTypes.SKELETON_CAT.get());
		map.put(PCEntityTypes.ZOMBIE_PARROT.get(), PCEntityTypes.SKELETON_PARROT.get());
	});
}

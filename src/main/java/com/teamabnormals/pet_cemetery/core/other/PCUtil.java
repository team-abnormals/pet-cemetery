package com.teamabnormals.pet_cemetery.core.other;

import com.google.common.collect.Maps;
import com.teamabnormals.pet_cemetery.core.registry.PCEntityTypes;
import net.minecraft.Util;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;

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

	public static int getConversionProgress(LivingEntity entity) {
		int conversionProgress = 1;
		if (entity.getRandom().nextFloat() < 0.01F) {
			int bonusBlocks = 0;
			MutableBlockPos pos = new MutableBlockPos();

			for (int x = (int) entity.getX() - 4; x < (int) entity.getX() + 4 && bonusBlocks < 14; ++x) {
				for (int y = (int) entity.getY() - 4; y < (int) entity.getY() + 4 && bonusBlocks < 14; ++y) {
					for (int z = (int) entity.getZ() - 4; z < (int) entity.getZ() + 4 && bonusBlocks < 14; ++z) {
						BlockState state = entity.level.getBlockState(pos.set(x, y, z));
						if (state.is(BlockTags.WOOL_CARPETS) || state.getBlock() instanceof BedBlock) {
							if (entity.getRandom().nextFloat() < 0.3F) {
								++conversionProgress;
							}
							++bonusBlocks;
						}
					}
				}
			}
		}
		return conversionProgress;
	}
}

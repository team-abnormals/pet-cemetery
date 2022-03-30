package com.teamabnormals.pet_cemetery.core.other;

import com.google.common.collect.Lists;
import com.teamabnormals.pet_cemetery.common.block.CompanionCoilBlock;
import com.teamabnormals.pet_cemetery.common.block.state.properties.CoilType;
import com.teamabnormals.pet_cemetery.core.PetCemetery;
import com.teamabnormals.pet_cemetery.core.other.tags.PCEntityTypeTags;
import com.teamabnormals.pet_cemetery.core.registry.PCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(modid = PetCemetery.MOD_ID)
public class PCEvents {

	@SubscribeEvent
	public static void onLivingSpawned(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();

		if (entity.getType().is(PCEntityTypeTags.DROPS_FORGOTTEN_COLLAR) && entity instanceof TamableAnimal pet) {
			List<Goal> goalsToRemove = Lists.newArrayList();
			pet.goalSelector.availableGoals.forEach((goal) -> {
				if (goal.getGoal() instanceof FloatGoal)
					goalsToRemove.add(goal.getGoal());
			});

			goalsToRemove.forEach(pet.goalSelector::removeGoal);
		}
	}

	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent event) {
		LivingEntity entity = event.getEntityLiving();
		Level world = entity.getCommandSenderWorld();

		if (entity instanceof Enemy && !world.isClientSide()) {
			AABB aabb = entity.getBoundingBox().inflate(5.0F, 2.0F, 5.0F);
			Stream<BlockPos> blocks = BlockPos.betweenClosedStream(new BlockPos(aabb.minX, aabb.minY, aabb.minZ), new BlockPos(aabb.maxX, aabb.maxY, aabb.maxZ));

			blocks.forEach(pos -> {
				BlockState state = world.getBlockState(pos);
				if (state.is(PCBlocks.COMPANION_COIL.get()) && state.getValue(CompanionCoilBlock.COIL_TYPE) == CoilType.BOTTOM) {
					((CompanionCoilBlock) PCBlocks.COMPANION_COIL.get()).powerBlock(state, world, pos);
				}
			});
		}
	}

	@SubscribeEvent
	public static void onEntityPlaceBlock(BlockEvent.EntityPlaceEvent event) {
		if (event.getEntity() != null) {
			Level level = event.getEntity().getLevel();
			BlockPos pos = event.getPos();
			BlockState state = event.getPlacedBlock();

			if (state.is(Blocks.LIGHTNING_ROD) && state.getValue(LightningRodBlock.FACING) == Direction.UP) {
				BlockPos belowPos = pos.below();
				BlockState belowState = level.getBlockState(belowPos);
				if (belowState.is(Blocks.LIGHTNING_ROD) && belowState.getValue(LightningRodBlock.FACING) == Direction.UP) {
					BlockPos doubleBelowPos = belowPos.below();
					BlockState doubleBelowState = level.getBlockState(doubleBelowPos);
					if (doubleBelowState.is(Blocks.CUT_COPPER)) {
						level.setBlockAndUpdate(pos, PCBlocks.COMPANION_COIL.get().defaultBlockState().setValue(CompanionCoilBlock.COIL_TYPE, CoilType.TOP).setValue(BlockStateProperties.WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)));
						level.setBlockAndUpdate(belowPos, PCBlocks.COMPANION_COIL.get().defaultBlockState().setValue(CompanionCoilBlock.COIL_TYPE, CoilType.MIDDLE).setValue(BlockStateProperties.WATERLOGGED, belowState.getValue(BlockStateProperties.WATERLOGGED)));
						level.setBlockAndUpdate(doubleBelowPos, PCBlocks.COMPANION_COIL.get().defaultBlockState().setValue(CompanionCoilBlock.COIL_TYPE, CoilType.BOTTOM));
					}
				}
			}
		}
	}
}

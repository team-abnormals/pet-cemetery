package com.teamabnormals.pet_cemetery.core.other;

import com.google.common.collect.Lists;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import com.teamabnormals.pet_cemetery.core.PetCemetery;
import com.teamabnormals.pet_cemetery.core.other.tags.PCEntityTypeTags;
import com.teamabnormals.pet_cemetery.core.registry.PCItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.UUID;

@EventBusSubscriber(modid = PetCemetery.MOD_ID)
public class PCEvents {

	@SubscribeEvent
	public static void onLivingSpawned(EntityJoinLevelEvent event) {
		Entity entity = event.getEntity();
		if (entity.getType().is(PCEntityTypeTags.DROPS_PET_COLLAR) && entity instanceof TamableAnimal pet) {
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
		LivingEntity entity = event.getEntity();
		EntityType<?> type = entity.getType();

		if (type.is(PCEntityTypeTags.DROPS_PET_COLLAR)) {
			ItemStack collar = new ItemStack(PCItems.PET_COLLAR.get());
			CompoundTag tag = collar.getOrCreateTag();

			tag.putString(PCUtil.PET_ID, ForgeRegistries.ENTITY_TYPES.getKey(type).toString());
			tag.putBoolean(PCUtil.IS_CHILD, entity.isBaby());
			if (entity.hasCustomName()) {
				collar.setHoverName(entity.getCustomName());
			}

			if (entity instanceof TamableAnimal pet && pet.isTame()) {
				tag.putString(PCUtil.OWNER_ID, pet.getOwnerUUID().toString());
				if (entity instanceof Wolf wolf) {
					tag.putInt(PCUtil.COLLAR_COLOR, wolf.getCollarColor().getId());
				} else if (entity instanceof Cat cat) {
					tag.putInt(PCUtil.PET_VARIANT, Registry.CAT_VARIANT.getId(cat.getCatVariant()));
					tag.putInt(PCUtil.COLLAR_COLOR, cat.getCollarColor().getId());
				} else if (entity instanceof Parrot parrot) {
					tag.putInt(PCUtil.PET_VARIANT, parrot.getVariant());
				}

				entity.spawnAtLocation(collar);
			}
		}
	}

	@SubscribeEvent
	public static void onRightClickBlock(RightClickBlock event) {
		Level level = event.getLevel();
		BlockPos pos = event.getPos();
		BlockState state = level.getBlockState(pos);
		ItemStack stack = event.getItemStack();
		BlockPos offsetPos = pos.above();

		if (stack.is(PCItems.PET_COLLAR.get()) && state.is(Blocks.RESPAWN_ANCHOR) && level.dimensionType().respawnAnchorWorks() && state.getValue(RespawnAnchorBlock.CHARGE) > RespawnAnchorBlock.MIN_CHARGES && level.getBlockState(offsetPos).getCollisionShape(level, offsetPos).isEmpty()) {
			Player player = event.getEntity();
			RandomSource random = player.getRandom();
			CompoundTag tag = stack.getOrCreateTag();

			if (tag.contains(PCUtil.PET_ID)) {
				EntityType<?> entityType = PCUtil.UNDEAD_MAP.get(ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(tag.getString(PCUtil.PET_ID))));

				if (entityType != null) {
					Animal entity = (Animal) entityType.create(level);
					UUID owner = tag.contains(PCUtil.OWNER_ID) ? UUID.fromString(tag.getString(PCUtil.OWNER_ID)) : player.getUUID();
					DyeColor collarColor = DyeColor.byId(tag.getInt(PCUtil.COLLAR_COLOR));
					int variant = tag.getInt(PCUtil.PET_VARIANT);

					entity.setBaby(tag.getBoolean(PCUtil.IS_CHILD));
					entity.setPos(offsetPos.getX() + 0.5F, offsetPos.getY(), offsetPos.getZ() + 0.5F);
					if (stack.hasCustomHoverName())
						entity.setCustomName(stack.getHoverName());

					TamableAnimal respawnedEntity = null;
					if (entity instanceof TamableAnimal pet) {
						pet.setTame(true);
						pet.setOwnerUUID(owner);
						if (pet instanceof Wolf wolf) {
							wolf.setCollarColor(collarColor);
							respawnedEntity = wolf;
						} else if (pet instanceof Cat cat) {
							cat.setCatVariant(Registry.CAT_VARIANT.byId(variant));
							cat.setCollarColor(collarColor);
							respawnedEntity = cat;
						} else if (pet instanceof Parrot parrot) {
							parrot.setVariant(variant);
							respawnedEntity = parrot;
						}
					}

					if (respawnedEntity != null) {
						if (player instanceof ServerPlayer serverPlayer) {
							PCCriteriaTriggers.RESPAWN_PET.trigger(serverPlayer, entity, respawnedEntity);
						}

						level.setBlockAndUpdate(pos, state.setValue(RespawnAnchorBlock.CHARGE, state.getValue(RespawnAnchorBlock.CHARGE) - 1));

						for (int i = 0; i < 10; ++i) {
							double d0 = random.nextGaussian() * 0.025D;
							double d1 = random.nextGaussian() * 0.025D;
							double d2 = random.nextGaussian() * 0.025D;
							NetworkUtil.spawnParticle(ParticleTypes.LARGE_SMOKE.writeToString(), respawnedEntity.getRandomX(0.75D), respawnedEntity.getRandomY(), respawnedEntity.getRandomZ(0.75D), d0, d1, d2);
						}

						level.playSound(player, pos, SoundEvents.RESPAWN_ANCHOR_DEPLETE, SoundSource.BLOCKS, 1.0F, 1.0F);
						level.addFreshEntity(respawnedEntity);
						if (!player.getAbilities().instabuild)
							stack.shrink(1);

						event.setCanceled(true);
						event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
					}
				}
			}
		}
	}
}

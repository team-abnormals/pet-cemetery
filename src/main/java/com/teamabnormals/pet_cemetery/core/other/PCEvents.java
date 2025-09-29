package com.teamabnormals.pet_cemetery.core.other;

import com.google.common.collect.Lists;
import com.teamabnormals.pet_cemetery.core.PetCemetery;
import com.teamabnormals.pet_cemetery.core.other.tags.PCEntityTypeTags;
import com.teamabnormals.pet_cemetery.core.registry.PCDataComponents;
import com.teamabnormals.pet_cemetery.core.registry.PCEntityTypes;
import com.teamabnormals.pet_cemetery.core.registry.PCEntityTypes.PetRespawn;
import com.teamabnormals.pet_cemetery.core.registry.PCItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@EventBusSubscriber(modid = PetCemetery.MOD_ID)
public class PCEvents {

	@SubscribeEvent
	public static void onLivingSpawned(EntityJoinLevelEvent event) {
		Entity entity = event.getEntity();
		EntityType<?> entityType = entity.getType();
		if ((entityType.is(PCEntityTypeTags.ZOMBIE_PETS) || entityType.is(PCEntityTypeTags.SKELETON_PETS)) && entity instanceof TamableAnimal pet) {
			List<Goal> goalsToRemove = Lists.newArrayList();
			pet.goalSelector.getAvailableGoals().forEach((goal) -> {
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

		if (type.builtInRegistryHolder().getData(PCEntityTypes.RESPAWNABLE_PETS) != null) {
			ItemStack collar = new ItemStack(PCItems.PET_COLLAR.get());
			CompoundTag tag = new CompoundTag();
			tag.putString(PCUtil.PET_ID, BuiltInRegistries.ENTITY_TYPE.getKey(type).toString());
			tag.putBoolean(PCUtil.IS_CHILD, entity.isBaby());
			if (entity.hasCustomName()) {
				collar.set(DataComponents.CUSTOM_NAME, entity.getCustomName());
			}

			if (entity instanceof TamableAnimal pet && pet.isTame()) {
				tag.putString(PCUtil.OWNER_ID, pet.getOwnerUUID().toString());
				if (entity instanceof Wolf wolf) {
					String variant = wolf.level().registryAccess().registry(Registries.WOLF_VARIANT).get().getKey(wolf.getVariant().value()).toString();
					tag.putString(PCUtil.PET_VARIANT, variant);
					tag.putInt(PCUtil.COLLAR_COLOR, wolf.getCollarColor().getId());
				} else if (entity instanceof Cat cat) {
					String variant = cat.level().registryAccess().registry(Registries.CAT_VARIANT).get().getKey(cat.getVariant().value()).toString();
					tag.putString(PCUtil.PET_VARIANT, variant);
					tag.putInt(PCUtil.COLLAR_COLOR, cat.getCollarColor().getId());
				} else if (entity instanceof Parrot parrot) {
					tag.putInt(PCUtil.PET_VARIANT, parrot.getVariant().getId());
				}
				collar.set(PCDataComponents.PET_DATA.get(), CustomData.of(tag));
				entity.spawnAtLocation(collar);
			} else if (entity.getType().is(PCEntityTypeTags.CAN_DROP_COLLAR_UNTAMED)) {
				collar.set(PCDataComponents.PET_DATA.get(), CustomData.of(tag));
				entity.spawnAtLocation(collar);
			}
		}
	}

	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		Level level = event.getLevel();
		BlockPos pos = event.getPos();
		BlockState state = level.getBlockState(pos);
		ItemStack stack = event.getItemStack();
		BlockPos offsetPos = pos.above();

		if (stack.is(PCItems.PET_COLLAR.get()) && state.is(Blocks.RESPAWN_ANCHOR) && level.dimensionType().respawnAnchorWorks() && state.getValue(RespawnAnchorBlock.CHARGE) > RespawnAnchorBlock.MIN_CHARGES && level.getBlockState(offsetPos).getCollisionShape(level, offsetPos).isEmpty()) {
			Player player = event.getEntity();
			RandomSource random = player.getRandom();
			CompoundTag tag = stack.getOrDefault(PCDataComponents.PET_DATA.get(), CustomData.EMPTY).copyTag();

			if (tag.contains(PCUtil.PET_ID)) {
				Optional<Reference<EntityType<?>>> entityHolder = BuiltInRegistries.ENTITY_TYPE.getHolder(ResourceLocation.parse(tag.getString(PCUtil.PET_ID)));
				if (entityHolder.isPresent()) {
					PetRespawn petRespawn = entityHolder.get().getData(PCEntityTypes.RESPAWNABLE_PETS);
					if (petRespawn != null && petRespawn.respawnedAs().value().create(level) instanceof LivingEntity entity) {
						UUID owner = tag.contains(PCUtil.OWNER_ID) ? UUID.fromString(tag.getString(PCUtil.OWNER_ID)) : player.getUUID();
						DyeColor collarColor = DyeColor.byId(tag.getInt(PCUtil.COLLAR_COLOR));

						if (entity instanceof AgeableMob ageable) {
							ageable.setBaby(tag.getBoolean(PCUtil.IS_CHILD));
						}
						entity.setPos(offsetPos.getX() + 0.5F, offsetPos.getY(), offsetPos.getZ() + 0.5F);
						if (stack.has(DataComponents.CUSTOM_NAME)) {
							entity.setCustomName(stack.getHoverName());
						}

						if (entity instanceof TamableAnimal pet) {
							pet.setTame(true, true);
							pet.setOwnerUUID(owner);
							switch (pet) {
								case Cat cat -> {
									Registry<CatVariant> registry = level.registryAccess().registryOrThrow(Registries.CAT_VARIANT);
									Optional<Reference<CatVariant>> variant = registry.getHolder(ResourceLocation.parse(tag.getString(PCUtil.PET_VARIANT)));
									if (variant.isPresent()) {
										cat.setVariant(variant.get());
										cat.setCollarColor(collarColor);
									}
								}
								case Wolf wolf -> {
									Registry<WolfVariant> registry = level.registryAccess().registryOrThrow(Registries.WOLF_VARIANT);
									Optional<Reference<WolfVariant>> variant = registry.getHolder(ResourceLocation.parse(tag.getString(PCUtil.PET_VARIANT)));
									if (variant.isPresent()) {
										wolf.setVariant(variant.get());
										wolf.setCollarColor(collarColor);
									}
								}
								case Parrot parrot -> {
									parrot.setVariant(Parrot.Variant.byId(tag.getInt(PCUtil.PET_VARIANT)));
								}
								default -> {
								}
							}
						}

						if (player instanceof ServerPlayer serverPlayer) {
							PCCriteriaTriggers.RESPAWNED_PET.get().trigger(serverPlayer, entity, entity);
						}

						level.setBlockAndUpdate(pos, state.setValue(RespawnAnchorBlock.CHARGE, state.getValue(RespawnAnchorBlock.CHARGE) - 1));

						for (int i = 0; i < 10; ++i) {
							double d0 = random.nextGaussian() * 0.025D;
							double d1 = random.nextGaussian() * 0.025D;
							double d2 = random.nextGaussian() * 0.025D;
							level.addParticle(ParticleTypes.LARGE_SMOKE, entity.getRandomX(0.75D), entity.getRandomY(), entity.getRandomZ(0.75D), d0, d1, d2);
						}

						level.playSound(player, pos, SoundEvents.RESPAWN_ANCHOR_DEPLETE.value(), SoundSource.BLOCKS, 1.0F, 1.0F);
						level.addFreshEntity(entity);
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

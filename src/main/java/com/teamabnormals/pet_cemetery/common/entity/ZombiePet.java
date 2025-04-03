package com.teamabnormals.pet_cemetery.common.entity;

import com.teamabnormals.pet_cemetery.core.other.PCCriteriaTriggers;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.EventHooks;

import javax.annotation.Nullable;
import java.util.UUID;

public interface ZombiePet {

	int getConversionTime();

	void setConversionTime(int conversionTime);

	UUID getConversionStarter();

	void setConversionStarter(UUID conversionStarter);

	EntityDataAccessor<Boolean> getConversionData();

	LivingEntity finalizeConversionSpawn(ServerLevel level);

	EntityType<? extends LivingEntity> getConversionType();

	default void tickConversionProgress() {
		LivingEntity zombie = (LivingEntity) this;
		if (!zombie.level().isClientSide && zombie.isAlive() && this.isConverting()) {
			int i = getConversionProgress(zombie);
			this.setConversionTime(this.getConversionTime() - i);
			if (this.getConversionTime() <= 0 && EventHooks.canLivingConvert(zombie, this.getConversionType(), this::setConversionTime)) {
				this.cureZombie(zombie, (ServerLevel) zombie.level());
			}
		}
	}

	default boolean isConverting() {
		return ((LivingEntity) this).getEntityData().get(this.getConversionData());
	}

	default void defineConvertingSynchedData(SynchedEntityData.Builder builder) {
		builder.define(this.getConversionData(), false);
	}

	default void addConvertingSavaData(CompoundTag compound) {
		compound.putInt("ConversionTime", this.isConverting() ? this.getConversionTime() : -1);
		if (this.getConversionStarter() != null) {
			compound.putUUID("ConversionPlayer", this.getConversionStarter());
		}
	}

	default void readConvertingSavaData(CompoundTag compound) {
		if (compound.contains("ConversionTime", 99) && compound.getInt("ConversionTime") > -1) {
			this.startConverting(compound.hasUUID("ConversionPlayer") ? compound.getUUID("ConversionPlayer") : null, compound.getInt("ConversionTime"));
		}
	}

	default void startConverting(@Nullable UUID conversionStarter, int conversionTime) {
		LivingEntity zombie = (LivingEntity) this;
		this.setConversionStarter(conversionStarter);
		this.setConversionTime(conversionTime);
		zombie.getEntityData().set(this.getConversionData(), true);
		zombie.removeEffect(MobEffects.WEAKNESS);
		zombie.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, conversionTime, Math.min(zombie.level().getDifficulty().getId() - 1, 0)));
		zombie.level().broadcastEntityEvent(zombie, (byte) 16);
	}

	default void cureZombie(LivingEntity zombie, ServerLevel level) {
		LivingEntity cured = this.finalizeConversionSpawn(level);
		if (cured != null) {
			if (this instanceof Mob mob) {
				for (EquipmentSlot equipmentslot : mob.dropPreservedEquipment(stack -> !EnchantmentHelper.has(stack, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE))) {
					cured.setItemSlot(equipmentslot, mob.getItemBySlot(equipmentslot));
				}
			}

			if (this.getConversionStarter() != null) {
				Player player = zombie.level().getPlayerByUUID(this.getConversionStarter());
				if (player instanceof ServerPlayer serverPlayer) {
					PCCriteriaTriggers.CURED_ZOMBIE_PET.get().trigger(serverPlayer, zombie, cured);
				}
			}

			cured.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
			if (!zombie.isSilent()) {
				level.levelEvent(null, 1027, zombie.blockPosition(), 0);
			}

			EventHooks.onLivingConvert(zombie, cured);
		}
	}

	default InteractionResult attemptCureZombie(Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		LivingEntity zombie = (LivingEntity) this;
		if (stack.getItem() == Items.GOLDEN_APPLE) {
			if (zombie.hasEffect(MobEffects.WEAKNESS)) {
				if (!player.getAbilities().instabuild) {
					stack.shrink(1);
				}

				if (!zombie.level().isClientSide) {
					this.startConverting(player.getUUID(), 60); //zombie.getRandom().nextInt(2401) + 3600);
				}

				return InteractionResult.SUCCESS;
			} else {
				return InteractionResult.CONSUME;
			}
		}

		return InteractionResult.PASS;
	}

	default boolean playCureSound(byte id) {
		LivingEntity zombie = (LivingEntity) this;
		if (id == 16) {
			if (!zombie.isSilent()) {
				zombie.level().playLocalSound(zombie.getX(), zombie.getEyeY(), zombie.getZ(), SoundEvents.ZOMBIE_VILLAGER_CURE, zombie.getSoundSource(), 1.0F + zombie.getRandom().nextFloat(), zombie.getRandom().nextFloat() * 0.7F + 0.3F, false);
			}
			return true;
		}
		return false;
	}

	static int getConversionProgress(LivingEntity zombie) {
		int conversionProgress = 1;
		if (zombie.getRandom().nextFloat() < 0.01F) {
			int bonusBlocks = 0;
			MutableBlockPos pos = new MutableBlockPos();

			for (int x = (int) zombie.getX() - 4; x < (int) zombie.getX() + 4 && bonusBlocks < 14; ++x) {
				for (int y = (int) zombie.getY() - 4; y < (int) zombie.getY() + 4 && bonusBlocks < 14; ++y) {
					for (int z = (int) zombie.getZ() - 4; z < (int) zombie.getZ() + 4 && bonusBlocks < 14; ++z) {
						BlockState state = zombie.level().getBlockState(pos.set(x, y, z));
						if (state.is(BlockTags.WOOL_CARPETS) || state.getBlock() instanceof BedBlock) {
							if (zombie.getRandom().nextFloat() < 0.3F) {
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

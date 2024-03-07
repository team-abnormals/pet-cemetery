package com.teamabnormals.pet_cemetery.common.entity;

import com.teamabnormals.pet_cemetery.core.other.PCCriteriaTriggers;
import com.teamabnormals.pet_cemetery.core.other.PCUtil;
import com.teamabnormals.pet_cemetery.core.registry.PCEntityTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.UUID;

public class ZombieParrot extends Parrot {
	private static final EntityDataAccessor<Boolean> CONVERTING = SynchedEntityData.defineId(ZombieParrot.class, EntityDataSerializers.BOOLEAN);
	private int conversionTime;
	private UUID conversionStarter;

	public ZombieParrot(EntityType<? extends ZombieParrot> type, Level worldIn) {
		super(type, worldIn);
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(CONVERTING, false);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Parrot.createAttributes()
				.add(Attributes.MAX_HEALTH, 6.0D + PCUtil.HEALTH_DIFF)
				.add(Attributes.FLYING_SPEED, 0.4F - PCUtil.SPEED_DIFF)
				.add(Attributes.MOVEMENT_SPEED, 0.2F - PCUtil.SPEED_DIFF);
	}

	@Override
	public ZombieParrot getBreedOffspring(ServerLevel world, AgeableMob entity) {
		ZombieParrot parrot = PCEntityTypes.ZOMBIE_PARROT.get().create(world);
		if (this.random.nextBoolean()) {
			parrot.setVariant(this.getVariant());
		} else {
			parrot.setVariant(parrot.getVariant());
		}

		if (this.isTame()) {
			parrot.setOwnerUUID(this.getOwnerUUID());
			parrot.setTame(true);
		}

		return parrot;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("ConversionTime", this.isConverting() ? this.conversionTime : -1);
		if (this.conversionStarter != null) {
			compound.putUUID("ConversionPlayer", this.conversionStarter);
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("ConversionTime", 99) && compound.getInt("ConversionTime") > -1) {
			this.startConverting(compound.hasUUID("ConversionPlayer") ? compound.getUUID("ConversionPlayer") : null, compound.getInt("ConversionTime"));
		}
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEAD;
	}

	@Override
	public void tick() {
		if (!this.level().isClientSide && this.isAlive() && this.isConverting()) {
			int i = PCUtil.getConversionProgress(this);
			this.conversionTime -= i;
			if (this.conversionTime <= 0 && ForgeEventFactory.canLivingConvert(this, EntityType.PARROT, (timer) -> this.conversionTime = timer)) {
				this.cureZombie((ServerLevel) this.level());
			}
		}

		super.tick();
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (itemstack.getItem() == Items.GOLDEN_APPLE) {
			if (this.hasEffect(MobEffects.WEAKNESS)) {
				if (!player.getAbilities().instabuild) {
					itemstack.shrink(1);
				}
				if (!this.level().isClientSide) {
					this.startConverting(player.getUUID(), this.random.nextInt(2401) + 3600);
				}
				return InteractionResult.SUCCESS;
			} else {
				return InteractionResult.CONSUME;
			}
		} else {
			return super.mobInteract(player, hand);
		}
	}

	public boolean isConverting() {
		return this.getEntityData().get(CONVERTING);
	}

	private void startConverting(@Nullable UUID conversionStarterIn, int conversionTimeIn) {
		this.conversionStarter = conversionStarterIn;
		this.conversionTime = conversionTimeIn;
		this.getEntityData().set(CONVERTING, true);
		this.removeEffect(MobEffects.WEAKNESS);
		this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, conversionTimeIn, Math.min(this.level().getDifficulty().getId() - 1, 0)));
		this.level().broadcastEntityEvent(this, (byte) 16);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void handleEntityEvent(byte id) {
		if (id == 16) {
			if (!this.isSilent()) {
				this.level().playLocalSound(this.getX(), this.getEyeY(), this.getZ(), SoundEvents.ZOMBIE_VILLAGER_CURE, this.getSoundSource(), 1.0F + this.random.nextFloat(), this.random.nextFloat() * 0.7F + 0.3F, false);
			}

		} else {
			super.handleEntityEvent(id);
		}
	}

	private void cureZombie(ServerLevel level) {
		Parrot parrot = this.copyEntityData();
		parrot.finalizeSpawn(level, level.getCurrentDifficultyAt(parrot.blockPosition()), MobSpawnType.CONVERSION, null, null);
		parrot.setVariant(this.getVariant());

		if (this.conversionStarter != null) {
			Player player = this.level().getPlayerByUUID(this.conversionStarter);
			if (player instanceof ServerPlayer serverPlayer) {
				PCCriteriaTriggers.CURED_ZOMBIE_PET.trigger(serverPlayer, this, parrot);
			}
		}

		parrot.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
		if (!this.isSilent()) {
			level.levelEvent(null, 1027, this.blockPosition(), 0);
		}

		ForgeEventFactory.onLivingConvert(this, parrot);
	}

	public Parrot copyEntityData() {
		Parrot parrot = this.convertTo(EntityType.PARROT, false);
		parrot.setTame(this.isTame());
		parrot.setOrderedToSit(this.isOrderedToSit());
		if (this.getOwner() != null)
			parrot.setOwnerUUID(this.getOwner().getUUID());
		return parrot;
	}
}
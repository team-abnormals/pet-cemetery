package com.teamabnormals.pet_cemetery.common.entity;

import com.teamabnormals.pet_cemetery.core.other.PCUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.UUID;

public class ZombieParrot extends Parrot implements ZombiePet {
	private static final EntityDataAccessor<Boolean> CONVERTING = SynchedEntityData.defineId(ZombieParrot.class, EntityDataSerializers.BOOLEAN);
	private int conversionTime;
	private UUID conversionStarter;

	public ZombieParrot(EntityType<? extends ZombieParrot> type, Level worldIn) {
		super(type, worldIn);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Parrot.createAttributes()
				.add(Attributes.MAX_HEALTH, 6.0D + PCUtil.HEALTH_DIFF)
				.add(Attributes.FLYING_SPEED, 0.4F - PCUtil.SPEED_DIFF)
				.add(Attributes.MOVEMENT_SPEED, 0.2F - PCUtil.SPEED_DIFF);
	}

	@Override
	public EntityType<? extends LivingEntity> getConversionType() {
		return EntityType.PARROT;
	}

	@Override
	public Parrot finalizeConversionSpawn(ServerLevel level) {
		Parrot parrot = this.convertTo(EntityType.PARROT, false);
		if (parrot != null) {
			parrot.setTame(this.isTame(), false);
			parrot.setOrderedToSit(this.isOrderedToSit());
			if (this.getOwner() != null)
				parrot.setOwnerUUID(this.getOwner().getUUID());


			parrot.finalizeSpawn(level, level.getCurrentDifficultyAt(parrot.blockPosition()), MobSpawnType.CONVERSION, null);
			parrot.setVariant(this.getVariant());
		}

		return parrot;
	}

	@Override
	public int getConversionTime() {
		return this.conversionTime;
	}

	@Override
	public void setConversionTime(int conversionTime) {
		this.conversionTime = conversionTime;
	}

	@Override
	public UUID getConversionStarter() {
		return this.conversionStarter;
	}

	@Override
	public void setConversionStarter(UUID conversionStarter) {
		this.conversionStarter = conversionStarter;
	}

	@Override
	public EntityDataAccessor<Boolean> getConversionData() {
		return CONVERTING;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		this.defineConvertingSynchedData(builder);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		this.addConvertingSavaData(tag);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.readConvertingSavaData(tag);
	}

	@Override
	public void tick() {
		this.tickConversionProgress();
		super.tick();
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		InteractionResult result = this.attemptCureZombie(player, hand);
		return result.indicateItemUse() ? result : super.mobInteract(player, hand);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void handleEntityEvent(byte id) {
		if (!this.playCureSound(id)) {
			super.handleEntityEvent(id);
		}
	}
}
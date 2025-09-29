package com.teamabnormals.pet_cemetery.common.entity;

import com.teamabnormals.pet_cemetery.core.other.PCUtil;
import com.teamabnormals.pet_cemetery.core.registry.PCEntityTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.UUID;

public class ZombieWolf extends Wolf implements ZombiePet {
	private static final EntityDataAccessor<Boolean> CONVERTING = SynchedEntityData.defineId(ZombieWolf.class, EntityDataSerializers.BOOLEAN);
	private int conversionTime;
	private UUID conversionStarter;

	public ZombieWolf(EntityType<? extends ZombieWolf> type, Level worldIn) {
		super(type, worldIn);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Wolf.createAttributes()
				.add(Attributes.MOVEMENT_SPEED, 0.3F - PCUtil.SPEED_DIFF)
				.add(Attributes.MAX_HEALTH, 8.0D + PCUtil.HEALTH_DIFF)
				.add(Attributes.ATTACK_DAMAGE, 2.0D - PCUtil.DAMAGE_DIFF);
	}

	@Override
	protected void applyTamingSideEffects() {
		if (this.isTame()) {
			this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(40.0D + PCUtil.HEALTH_DIFF * 5.0D);
			this.setHealth((float) (40.0F + (PCUtil.HEALTH_DIFF * 5.0D)));
		} else {
			this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(8.0D + PCUtil.HEALTH_DIFF);
		}
	}

	@Override
	public EntityType<? extends LivingEntity> getConversionType() {
		return EntityType.WOLF;
	}

	@Override
	public ZombieWolf getBreedOffspring(ServerLevel level, AgeableMob ageableMob) {
		ZombieWolf wolf = PCEntityTypes.ZOMBIE_WOLF.get().create(level);
		if (wolf != null && ageableMob instanceof Wolf parent) {
			wolf.setVariant(this.random.nextBoolean() ? this.getVariant() : parent.getVariant());
			if (this.isTame()) {
				wolf.setOwnerUUID(this.getOwnerUUID());
				wolf.setTame(true, true);
				wolf.setCollarColor(this.random.nextBoolean() ? this.getCollarColor() : parent.getCollarColor());
			}
		}

		return wolf;
	}

	@Override
	public Wolf finalizeConversionSpawn(ServerLevel level) {
		Wolf wolf = this.convertTo(EntityType.WOLF, false);
		if (wolf != null) {
			wolf.setCollarColor(this.getCollarColor());
			wolf.setTame(this.isTame(), true);
			wolf.setOrderedToSit(this.isOrderedToSit());
			if (this.getOwner() != null)
				wolf.setOwnerUUID(this.getOwner().getUUID());

			wolf.finalizeSpawn(level, level.getCurrentDifficultyAt(wolf.blockPosition()), MobSpawnType.CONVERSION, null);
			wolf.setVariant(this.getVariant());
		}

		return wolf;
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
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
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.UUID;

public class ZombieCat extends Cat implements ZombiePet {
	private static final EntityDataAccessor<Boolean> CONVERTING = SynchedEntityData.defineId(ZombieCat.class, EntityDataSerializers.BOOLEAN);
	private int conversionTime;
	private UUID conversionStarter;

	public ZombieCat(EntityType<? extends ZombieCat> type, Level worldIn) {
		super(type, worldIn);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Cat.createAttributes()
				.add(Attributes.MAX_HEALTH, 10.0D + PCUtil.HEALTH_DIFF)
				.add(Attributes.MOVEMENT_SPEED, 0.3F - PCUtil.SPEED_DIFF)
				.add(Attributes.ATTACK_DAMAGE, 3.0D - PCUtil.DAMAGE_DIFF);
	}

	@Override
	public EntityType<? extends LivingEntity> getConversionType() {
		return EntityType.CAT;
	}

	@Override
	public ZombieCat getBreedOffspring(ServerLevel level, AgeableMob ageableMob) {
		ZombieCat cat = PCEntityTypes.ZOMBIE_CAT.get().create(level);
		if (cat != null && ageableMob instanceof Cat parent) {
			cat.setVariant(this.random.nextBoolean() ? this.getVariant() : parent.getVariant());
			if (this.isTame()) {
				cat.setOwnerUUID(this.getOwnerUUID());
				cat.setTame(true, true);
				cat.setCollarColor(this.random.nextBoolean() ? this.getCollarColor() : parent.getCollarColor());
			}
		}

		return cat;
	}

	@Override
	public Cat finalizeConversionSpawn(ServerLevel level) {
		Cat cat = this.convertTo(EntityType.CAT, false);
		if (cat != null) {
			cat.setCollarColor(this.getCollarColor());
			cat.setTame(this.isTame(), true);
			cat.setOrderedToSit(this.isOrderedToSit());
			if (this.getOwner() != null)
				cat.setOwnerUUID(this.getOwner().getUUID());

			cat.finalizeSpawn(level, level.getCurrentDifficultyAt(cat.blockPosition()), MobSpawnType.CONVERSION, null);
			cat.setVariant(this.getVariant());
		}

		return cat;
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
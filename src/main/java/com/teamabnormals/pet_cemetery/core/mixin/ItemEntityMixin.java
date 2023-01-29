package com.teamabnormals.pet_cemetery.core.mixin;

import com.teamabnormals.pet_cemetery.common.item.ForgottenCollarItem;
import com.teamabnormals.pet_cemetery.core.registry.PCItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {

	@Shadow
	public abstract ItemStack getItem();

	public ItemEntityMixin(EntityType<?> type, Level level) {
		super(type, level);
	}

	@Override
	public void thunderHit(ServerLevel level, LightningBolt lightningBolt) {
		ItemEntity item = (ItemEntity) (Object) this;
		if (this.getItem().is(PCItems.FORGOTTEN_COLLAR.get())) {
			ForgottenCollarItem.onLightningStrike(item, level);
		}
	}
}

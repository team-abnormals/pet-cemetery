package com.teamabnormals.pet_cemetery.core.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamabnormals.pet_cemetery.core.other.tags.PCEntityTypeTags;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SitWhenOrderedToGoal.class)
public class SitWhenOrderedToGoalMixin {

	@WrapOperation(method = "canUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/TamableAnimal;isInWaterOrBubble()Z"))
	private boolean canUse(TamableAnimal animal, Operation<Boolean> original) {
		if (animal.getType().is(PCEntityTypeTags.ZOMBIE_PETS) || animal.getType().is(PCEntityTypeTags.SKELETON_PETS)) {
			return animal.level().getBlockState(animal.blockPosition()).is(Blocks.BUBBLE_COLUMN);
		}
		return original.call(animal);
	}
}
package com.teamabnormals.pet_cemetery.core.mixin;

import com.teamabnormals.pet_cemetery.core.other.tags.PCEntityTypeTags;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SitWhenOrderedToGoal.class)
public class SitWhenOrderedToGoalMixin {

	@Redirect(method = "canUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/TamableAnimal;isInWaterOrBubble()Z"))
	private boolean canUse(TamableAnimal animal) {
		return (animal.getType().is(PCEntityTypeTags.ZOMBIE_PETS) || animal.getType().is(PCEntityTypeTags.SKELETON_PETS)) ? animal.getLevel().getBlockState(animal.blockPosition()).is(Blocks.BUBBLE_COLUMN) : animal.isInWaterOrBubble();
	}
}

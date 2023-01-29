package com.teamabnormals.pet_cemetery.core.mixin;

import com.teamabnormals.pet_cemetery.core.other.tags.PCEntityTypeTags;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.monster.Phantom;
import org.apache.commons.compress.utils.Lists;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(Phantom.PhantomSweepAttackGoal.class)
public class PhantomSweepAttackGoalMixin {

	@Redirect(method = "canContinueToUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Cat;hiss()V"))
	private void hiss(Cat cat) {
		if (!(cat.getType().is(PCEntityTypeTags.DROPS_PET_COLLAR)))
			cat.hiss();
	}

	@Redirect(method = "canContinueToUse", at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z"))
	private boolean isEmpty(List<Cat> cats) {
		List<Cat> toRemove = Lists.newArrayList();
		for (Cat cat : cats) {
			if (cat.getType().is(PCEntityTypeTags.DROPS_PET_COLLAR)) {
				toRemove.add(cat);
			}
		}
		cats.removeAll(toRemove);
		return !cats.isEmpty();
	}
}

package com.teamabnormals.pet_cemetery.core.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.function.Predicate;

@Mixin(Phantom.PhantomSweepAttackGoal.class)
public class PhantomSweepAttackGoalMixin {

	@WrapOperation(method = "canContinueToUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"))
	private <T extends Entity> List<T> getEntitiesOfClass(Level level, Class<T> aClass, AABB aabb, Predicate<? super T> predicate, Operation<List<T>> original) {
		return level.getEntitiesOfClass(aClass, aabb, entity -> predicate.test(entity) && entity instanceof Cat);
	}
}
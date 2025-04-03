package com.teamabnormals.pet_cemetery.common.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamabnormals.pet_cemetery.common.advancement.ConvertedMobTrigger.TriggerInstance;
import com.teamabnormals.pet_cemetery.core.other.PCCriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.CriterionValidator;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.Optional;

public class ConvertedMobTrigger extends SimpleCriterionTrigger<TriggerInstance> {

	@Override
	public Codec<TriggerInstance> codec() {
		return TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayer player, LivingEntity originalEntity, LivingEntity convertedEntity) {
		this.trigger(player, instance -> instance.matches(EntityPredicate.createContext(player, originalEntity), EntityPredicate.createContext(player, convertedEntity)));
	}

	public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> originalEntity, Optional<ContextAwarePredicate> convertedEntity) implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
								EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
								EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("original_entity").forGetter(TriggerInstance::originalEntity),
								EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("converted_entity").forGetter(TriggerInstance::convertedEntity)
						)
						.apply(instance, TriggerInstance::new)
		);

		public static Criterion<TriggerInstance> curedZombiePet() {
			return PCCriteriaTriggers.CURED_ZOMBIE_PET.get().createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
		}

		public static Criterion<TriggerInstance> respawnedPet(Optional<ContextAwarePredicate> predicate) {
			return PCCriteriaTriggers.RESPAWNED_PET.get().createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), predicate));
		}

		public static Criterion<TriggerInstance> respawnedPet() {
			return respawnedPet(Optional.empty());
		}

		public boolean matches(LootContext mob, LootContext convertedMob) {
			return (this.originalEntity.isEmpty() || this.originalEntity.get().matches(mob)) && (this.convertedEntity.isEmpty() || this.convertedEntity.get().matches(convertedMob));
		}

		@Override
		public void validate(CriterionValidator validator) {
			SimpleCriterionTrigger.SimpleInstance.super.validate(validator);
			validator.validateEntity(this.originalEntity, ".original_entity");
			validator.validateEntity(this.convertedEntity, ".converted_entity");
		}
	}
}
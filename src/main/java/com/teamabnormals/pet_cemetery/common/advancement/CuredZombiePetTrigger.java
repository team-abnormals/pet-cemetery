package com.teamabnormals.pet_cemetery.common.advancement;

import com.google.gson.JsonObject;
import com.teamabnormals.pet_cemetery.common.advancement.CuredZombiePetTrigger.TriggerInstance;
import com.teamabnormals.pet_cemetery.core.PetCemetery;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.storage.loot.LootContext;

public class CuredZombiePetTrigger extends SimpleCriterionTrigger<TriggerInstance> {
	private static final ResourceLocation ID = new ResourceLocation(PetCemetery.MOD_ID, "cured_zombie_pet");

	public ResourceLocation getId() {
		return ID;
	}

	public CuredZombiePetTrigger.TriggerInstance createInstance(JsonObject jsonObject, ContextAwarePredicate player, DeserializationContext context) {
		ContextAwarePredicate zombie = EntityPredicate.fromJson(jsonObject, "zombie", context);
		ContextAwarePredicate pet = EntityPredicate.fromJson(jsonObject, "pet", context);
		return new CuredZombiePetTrigger.TriggerInstance(player, zombie, pet);
	}

	public void trigger(ServerPlayer player, Animal zombie, Animal pet) {
		LootContext zombieContext = EntityPredicate.createContext(player, zombie);
		LootContext petContext = EntityPredicate.createContext(player, pet);
		this.trigger(player, (instance) -> instance.matches(zombieContext, petContext));
	}

	public static class TriggerInstance extends AbstractCriterionTriggerInstance {
		private final ContextAwarePredicate zombie;
		private final ContextAwarePredicate pet;

		public TriggerInstance(ContextAwarePredicate player, ContextAwarePredicate zombie, ContextAwarePredicate pet) {
			super(CuredZombiePetTrigger.ID, player);
			this.zombie = zombie;
			this.pet = pet;
		}

		public static CuredZombiePetTrigger.TriggerInstance curedZombiePet() {
			return new CuredZombiePetTrigger.TriggerInstance(ContextAwarePredicate.ANY, ContextAwarePredicate.ANY, ContextAwarePredicate.ANY);
		}

		public boolean matches(LootContext zombieContext, LootContext petContext) {
			if (!this.zombie.matches(zombieContext)) {
				return false;
			} else {
				return this.pet.matches(petContext);
			}
		}

		public JsonObject serializeToJson(SerializationContext context) {
			JsonObject jsonObject = super.serializeToJson(context);
			jsonObject.add("zombie", this.zombie.toJson(context));
			jsonObject.add("pet", this.pet.toJson(context));
			return jsonObject;
		}
	}
}
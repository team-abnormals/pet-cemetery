package com.teamabnormals.pet_cemetery.common.advancement;

import com.google.gson.JsonObject;
import com.teamabnormals.pet_cemetery.common.advancement.RespawnPetTrigger.TriggerInstance;
import com.teamabnormals.pet_cemetery.core.PetCemetery;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.storage.loot.LootContext;

public class RespawnPetTrigger extends SimpleCriterionTrigger<TriggerInstance> {
	private static final ResourceLocation ID = new ResourceLocation(PetCemetery.MOD_ID, "respawn_pet");

	public ResourceLocation getId() {
		return ID;
	}

	public RespawnPetTrigger.TriggerInstance createInstance(JsonObject jsonObject, EntityPredicate.Composite player, DeserializationContext context) {
		EntityPredicate.Composite pet = EntityPredicate.Composite.fromJson(jsonObject, "pet", context);
		EntityPredicate.Composite respawnedPet = EntityPredicate.Composite.fromJson(jsonObject, "respawned_pet", context);
		return new RespawnPetTrigger.TriggerInstance(player, pet, respawnedPet);
	}

	public void trigger(ServerPlayer player, Animal pet, Animal respawnedPet) {
		LootContext petContext = EntityPredicate.createContext(player, pet);
		LootContext respawnedPetContext = EntityPredicate.createContext(player, respawnedPet);
		this.trigger(player, (instance) -> instance.matches(petContext, respawnedPetContext));
	}

	public static class TriggerInstance extends AbstractCriterionTriggerInstance {
		private final EntityPredicate.Composite pet;
		private final EntityPredicate.Composite respawnedPet;

		public TriggerInstance(EntityPredicate.Composite player, EntityPredicate.Composite pet, EntityPredicate.Composite respawnedPet) {
			super(RespawnPetTrigger.ID, player);
			this.pet = pet;
			this.respawnedPet = respawnedPet;
		}

		public static RespawnPetTrigger.TriggerInstance respawnPet() {
			return new RespawnPetTrigger.TriggerInstance(EntityPredicate.Composite.ANY, EntityPredicate.Composite.ANY, EntityPredicate.Composite.ANY);
		}

		public static RespawnPetTrigger.TriggerInstance respawnPet(EntityPredicate entity) {
			return new RespawnPetTrigger.TriggerInstance(EntityPredicate.Composite.ANY, EntityPredicate.Composite.ANY, EntityPredicate.Composite.wrap(entity));
		}

		public boolean matches(LootContext petContext, LootContext respawnedPetContext) {
			if (!this.pet.matches(petContext)) {
				return false;
			} else {
				return this.respawnedPet.matches(respawnedPetContext);
			}
		}

		public JsonObject serializeToJson(SerializationContext context) {
			JsonObject jsonObject = super.serializeToJson(context);
			jsonObject.add("pet", this.pet.toJson(context));
			jsonObject.add("respawned_pet", this.respawnedPet.toJson(context));
			return jsonObject;
		}
	}
}
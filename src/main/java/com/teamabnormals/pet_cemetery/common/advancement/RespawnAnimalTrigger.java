package com.teamabnormals.pet_cemetery.common.advancement;

import com.google.gson.JsonObject;
import com.teamabnormals.pet_cemetery.common.advancement.RespawnAnimalTrigger.TriggerInstance;
import com.teamabnormals.pet_cemetery.core.PetCemetery;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.storage.loot.LootContext;

public class RespawnAnimalTrigger extends SimpleCriterionTrigger<TriggerInstance> {
	private static final ResourceLocation ID = new ResourceLocation(PetCemetery.MOD_ID, "respawn_animal");

	public ResourceLocation getId() {
		return ID;
	}

	public RespawnAnimalTrigger.TriggerInstance createInstance(JsonObject jsonObject, EntityPredicate.Composite entity, DeserializationContext context) {
		EntityPredicate.Composite animal = EntityPredicate.Composite.fromJson(jsonObject, "entity", context);
		return new RespawnAnimalTrigger.TriggerInstance(entity, animal);
	}

	public void trigger(ServerPlayer player, Animal animal) {
		LootContext context = EntityPredicate.createContext(player, animal);
		this.trigger(player, (instance) -> instance.matches(context));
	}

	public static class TriggerInstance extends AbstractCriterionTriggerInstance {
		private final EntityPredicate.Composite entity;

		public TriggerInstance(EntityPredicate.Composite entity, EntityPredicate.Composite animal) {
			super(RespawnAnimalTrigger.ID, entity);
			this.entity = animal;
		}

		public static RespawnAnimalTrigger.TriggerInstance respawnedAnimal() {
			return new RespawnAnimalTrigger.TriggerInstance(EntityPredicate.Composite.ANY, EntityPredicate.Composite.ANY);
		}

		public static RespawnAnimalTrigger.TriggerInstance respawnedAnimal(EntityPredicate animal) {
			return new RespawnAnimalTrigger.TriggerInstance(EntityPredicate.Composite.ANY, EntityPredicate.Composite.wrap(animal));
		}

		public boolean matches(LootContext context) {
			return this.entity.matches(context);
		}

		public JsonObject serializeToJson(SerializationContext context) {
			JsonObject jsonObject = super.serializeToJson(context);
			jsonObject.add("entity", this.entity.toJson(context));
			return jsonObject;
		}
	}
}
package com.teamabnormals.pet_cemetery.core.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamabnormals.blueprint.core.util.registry.EntitySubRegistryHelper;
import com.teamabnormals.pet_cemetery.common.entity.*;
import com.teamabnormals.pet_cemetery.core.PetCemetery;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber(modid = PetCemetery.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class PCEntityTypes {
	public static final EntitySubRegistryHelper ENTITY_TYPES = PetCemetery.REGISTRY_HELPER.getEntitySubHelper();

	public static final DeferredHolder<EntityType<?>, EntityType<ZombieWolf>> ZOMBIE_WOLF = ENTITY_TYPES.createEntity("zombie_wolf", ZombieWolf::new, MobCategory.CREATURE, 0.6F, 0.85F);
	public static final DeferredHolder<EntityType<?>, EntityType<ZombieCat>> ZOMBIE_CAT = ENTITY_TYPES.createEntity("zombie_cat", ZombieCat::new, MobCategory.CREATURE, 0.6F, 0.7F);
	public static final DeferredHolder<EntityType<?>, EntityType<ZombieParrot>> ZOMBIE_PARROT = ENTITY_TYPES.createEntity("zombie_parrot", ZombieParrot::new, MobCategory.CREATURE, 0.5F, 0.9F);

	public static final DeferredHolder<EntityType<?>, EntityType<SkeletonWolf>> SKELETON_WOLF = ENTITY_TYPES.createEntity("skeleton_wolf", SkeletonWolf::new, MobCategory.CREATURE, 0.6F, 0.85F);
	public static final DeferredHolder<EntityType<?>, EntityType<SkeletonCat>> SKELETON_CAT = ENTITY_TYPES.createEntity("skeleton_cat", SkeletonCat::new, MobCategory.CREATURE, 0.6F, 0.7F);
	public static final DeferredHolder<EntityType<?>, EntityType<SkeletonParrot>> SKELETON_PARROT = ENTITY_TYPES.createEntity("skeleton_parrot", SkeletonParrot::new, MobCategory.CREATURE, 0.5F, 0.9F);

	public static final DataMapType<EntityType<?>, PetRespawn> RESPAWNABLE_PETS = DataMapType.builder(PetCemetery.location("respawnable_pets"), Registries.ENTITY_TYPE, PetRespawn.CODEC).synced(PetRespawn.RESPAWN_CODEC, false).build();

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(ZOMBIE_WOLF.get(), ZombieWolf.createAttributes().build());
		event.put(ZOMBIE_CAT.get(), ZombieCat.createAttributes().build());
		event.put(ZOMBIE_PARROT.get(), ZombieParrot.createAttributes().build());

		event.put(SKELETON_WOLF.get(), SkeletonWolf.createAttributes().build());
		event.put(SKELETON_CAT.get(), SkeletonCat.createAttributes().build());
		event.put(SKELETON_PARROT.get(), SkeletonParrot.createAttributes().build());
	}

	@SubscribeEvent
	public static void registerDataMaps(RegisterDataMapTypesEvent event) {
		event.register(RESPAWNABLE_PETS);
	}

	public record PetRespawn(Holder<EntityType<?>> respawnedAs) {
		public static final Codec<PetRespawn> RESPAWN_CODEC = RegistryFixedCodec.create(Registries.ENTITY_TYPE).xmap(PetRespawn::new, PetRespawn::respawnedAs);

		public static final Codec<PetRespawn> CODEC = Codec.withAlternative(
				RecordCodecBuilder.create(in -> in.group(
						RegistryFixedCodec.create(Registries.ENTITY_TYPE).fieldOf("respawned_as").forGetter(PetRespawn::respawnedAs)).apply(in, PetRespawn::new)),
				RESPAWN_CODEC);
	}
}
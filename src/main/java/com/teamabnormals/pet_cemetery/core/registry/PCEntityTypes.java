package com.teamabnormals.pet_cemetery.core.registry;

import com.teamabnormals.blueprint.core.util.registry.EntitySubRegistryHelper;
import com.teamabnormals.pet_cemetery.common.entity.skeleton.*;
import com.teamabnormals.pet_cemetery.common.entity.zombie.*;
import com.teamabnormals.pet_cemetery.core.PetCemetery;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.*;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = PetCemetery.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PCEntityTypes {
	public static final EntitySubRegistryHelper HELPER = PetCemetery.REGISTRY_HELPER.getEntitySubHelper();


	public static final RegistryObject<EntityType<ZombieWolf>> ZOMBIE_WOLF = HELPER.createLivingEntity("zombie_wolf", ZombieWolf::new, MobCategory.CREATURE, 0.6F, 0.85F);
	public static final RegistryObject<EntityType<ZombieCat>> ZOMBIE_CAT = HELPER.createLivingEntity("zombie_cat", ZombieCat::new, MobCategory.CREATURE, 0.6F, 0.7F);
	public static final RegistryObject<EntityType<ZombieParrot>> ZOMBIE_PARROT = HELPER.createLivingEntity("zombie_parrot", ZombieParrot::new, MobCategory.CREATURE, 0.5F, 0.9F);

	public static final RegistryObject<EntityType<SkeletonWolf>> SKELETON_WOLF = HELPER.createLivingEntity("skeleton_wolf", SkeletonWolf::new, MobCategory.CREATURE, 0.6F, 0.85F);
	public static final RegistryObject<EntityType<SkeletonCat>> SKELETON_CAT = HELPER.createLivingEntity("skeleton_cat", SkeletonCat::new, MobCategory.CREATURE, 0.6F, 0.7F);
	public static final RegistryObject<EntityType<SkeletonParrot>> SKELETON_PARROT = HELPER.createLivingEntity("skeleton_parrot", SkeletonParrot::new, MobCategory.CREATURE, 0.5F, 0.9F);

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(ZOMBIE_WOLF.get(), Wolf.createAttributes().build());
		event.put(ZOMBIE_CAT.get(), Cat.createAttributes().build());
		event.put(ZOMBIE_PARROT.get(), Parrot.createAttributes().build());

		event.put(SKELETON_WOLF.get(), Wolf.createAttributes().build());
		event.put(SKELETON_CAT.get(), Cat.createAttributes().build());
		event.put(SKELETON_PARROT.get(), Parrot.createAttributes().build());
	}
}
package com.teamabnormals.pet_cemetery.core.data.server;

import com.teamabnormals.pet_cemetery.core.registry.PCEntityTypes;
import com.teamabnormals.pet_cemetery.core.registry.PCEntityTypes.PetRespawn;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.concurrent.CompletableFuture;

public class PCDataMapProvider extends DataMapProvider {

	public PCDataMapProvider(PackOutput output, CompletableFuture<Provider> provider) {
		super(output, provider);
	}

	@Override
	protected void gather(Provider provider) {
		this.builder(PCEntityTypes.RESPAWNABLE_PETS)
				.add(EntityType.WOLF.builtInRegistryHolder(), new PetRespawn(PCEntityTypes.ZOMBIE_WOLF), false)
				.add(EntityType.CAT.builtInRegistryHolder(), new PetRespawn(PCEntityTypes.ZOMBIE_CAT), false)
				.add(EntityType.PARROT.builtInRegistryHolder(), new PetRespawn(PCEntityTypes.ZOMBIE_PARROT), false)
				.add(PCEntityTypes.ZOMBIE_WOLF, new PetRespawn(PCEntityTypes.SKELETON_WOLF), false)
				.add(PCEntityTypes.ZOMBIE_CAT, new PetRespawn(PCEntityTypes.SKELETON_CAT), false)
				.add(PCEntityTypes.ZOMBIE_PARROT, new PetRespawn(PCEntityTypes.SKELETON_PARROT), false);
	}
}

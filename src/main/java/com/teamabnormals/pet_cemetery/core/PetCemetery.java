package com.teamabnormals.pet_cemetery.core;

import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import com.teamabnormals.pet_cemetery.core.data.client.PCItemModelProvider;
import com.teamabnormals.pet_cemetery.core.data.client.PCLanguageProvider;
import com.teamabnormals.pet_cemetery.core.data.server.PCAdvancementProvider;
import com.teamabnormals.pet_cemetery.core.data.server.tags.PCEntityTypeTagsProvider;
import com.teamabnormals.pet_cemetery.core.other.PCClientCompat;
import com.teamabnormals.pet_cemetery.core.registry.PCDataComponents;
import com.teamabnormals.pet_cemetery.core.registry.PCEntityTypes;
import com.teamabnormals.pet_cemetery.core.registry.PCItems;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@Mod(PetCemetery.MOD_ID)
public class PetCemetery {
	public static final String MOD_ID = "pet_cemetery";
	public static final RegistryHelper REGISTRY_HELPER = new RegistryHelper(MOD_ID);

	public PetCemetery(IEventBus bus, ModContainer container) {
		PCItems.ITEMS.register(bus);
		PCEntityTypes.ENTITY_TYPES.register(bus);
		PCDataComponents.COMPONENTS.register(bus);

		bus.addListener(this::clientSetup);
		bus.addListener(this::dataSetup);
	}

	private void clientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(PCClientCompat::register);
	}

	private void dataSetup(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		CompletableFuture<Provider> provider = event.getLookupProvider();
		ExistingFileHelper helper = event.getExistingFileHelper();

		generator.addProvider(event.includeServer(), new PCEntityTypeTagsProvider(output, provider, helper));
		generator.addProvider(event.includeServer(), PCAdvancementProvider.create(output, provider, helper));
		generator.addProvider(event.includeClient(), new PCItemModelProvider(output, helper));
		generator.addProvider(event.includeClient(), new PCLanguageProvider(output));
	}

	public static ResourceLocation location(String name) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
	}
}
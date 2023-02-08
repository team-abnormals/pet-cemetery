package com.teamabnormals.pet_cemetery.core;

import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import com.teamabnormals.pet_cemetery.client.renderer.entity.UndeadCatRenderer;
import com.teamabnormals.pet_cemetery.client.renderer.entity.UndeadParrotRenderer;
import com.teamabnormals.pet_cemetery.client.renderer.entity.UndeadWolfRenderer;
import com.teamabnormals.pet_cemetery.client.renderer.entity.layers.UndeadParrotOnShoulderLayer;
import com.teamabnormals.pet_cemetery.common.item.PetCollarItem;
import com.teamabnormals.pet_cemetery.core.data.client.PCItemModelProvider;
import com.teamabnormals.pet_cemetery.core.data.client.PCLanguageProvider;
import com.teamabnormals.pet_cemetery.core.data.server.PCAdvancementProvider;
import com.teamabnormals.pet_cemetery.core.data.server.tags.PCEntityTypeTagsProvider;
import com.teamabnormals.pet_cemetery.core.registry.PCEntityTypes;
import com.teamabnormals.pet_cemetery.core.registry.PCItems;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(PetCemetery.MOD_ID)
public class PetCemetery {
	public static final String MOD_ID = "pet_cemetery";
	public static final RegistryHelper REGISTRY_HELPER = new RegistryHelper(MOD_ID);

	public PetCemetery() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		MinecraftForge.EVENT_BUS.register(this);

		REGISTRY_HELPER.register(bus);

		bus.addListener(this::clientSetup);
		bus.addListener(this::dataSetup);
		bus.addListener(this::registerRenderers);

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			bus.addListener(this::registerLayers);
			bus.addListener(this::registerItemColors);
		});
	}

	private void clientSetup(FMLClientSetupEvent event) {
	}

	private void dataSetup(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper helper = event.getExistingFileHelper();

		generator.addProvider(event.includeServer(), new PCEntityTypeTagsProvider(generator, helper));
		generator.addProvider(event.includeServer(), new PCAdvancementProvider(generator, helper));
		generator.addProvider(event.includeClient(), new PCItemModelProvider(generator, helper));
		generator.addProvider(event.includeClient(), new PCLanguageProvider(generator));
	}

	private void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(PCEntityTypes.ZOMBIE_WOLF.get(), UndeadWolfRenderer::new);
		event.registerEntityRenderer(PCEntityTypes.ZOMBIE_CAT.get(), UndeadCatRenderer::new);
		event.registerEntityRenderer(PCEntityTypes.ZOMBIE_PARROT.get(), UndeadParrotRenderer::new);
		event.registerEntityRenderer(PCEntityTypes.SKELETON_WOLF.get(), UndeadWolfRenderer::new);
		event.registerEntityRenderer(PCEntityTypes.SKELETON_CAT.get(), UndeadCatRenderer::new);
		event.registerEntityRenderer(PCEntityTypes.SKELETON_PARROT.get(), UndeadParrotRenderer::new);
	}

	@OnlyIn(Dist.CLIENT)
	private void registerItemColors(RegisterColorHandlersEvent.Item event) {
		event.register((stack, color) -> color > 0 ? -1 : ((PetCollarItem) stack.getItem()).getColor(stack), PCItems.PET_COLLAR.get());
	}

	@OnlyIn(Dist.CLIENT)
	private void registerLayers(EntityRenderersEvent.AddLayers event) {
		event.getSkins().forEach(skin -> {
			PlayerRenderer renderer = event.getSkin(skin);
			renderer.addLayer(new UndeadParrotOnShoulderLayer<>(renderer, event.getEntityModels()));
		});
	}
}
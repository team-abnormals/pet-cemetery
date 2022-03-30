package com.teamabnormals.pet_cemetery.core;

import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import com.teamabnormals.pet_cemetery.client.renderer.entity.SkeletonCatRenderer;
import com.teamabnormals.pet_cemetery.client.renderer.entity.SkeletonParrotRenderer;
import com.teamabnormals.pet_cemetery.client.renderer.entity.SkeletonWolfRenderer;
import com.teamabnormals.pet_cemetery.client.renderer.entity.ZombieCatRenderer;
import com.teamabnormals.pet_cemetery.client.renderer.entity.ZombieParrotRenderer;
import com.teamabnormals.pet_cemetery.client.renderer.entity.ZombieWolfRenderer;
import com.teamabnormals.pet_cemetery.client.renderer.entity.layers.UndeadParrotLayer;
import com.teamabnormals.pet_cemetery.common.item.ForgottenCollarItem;
import com.teamabnormals.pet_cemetery.core.data.client.PCItemModelProvider;
import com.teamabnormals.pet_cemetery.core.data.client.PCLanguageProvider;
import com.teamabnormals.pet_cemetery.core.data.server.tags.PCEntityTypeTagsProvider;
import com.teamabnormals.pet_cemetery.core.registry.PCBlocks;
import com.teamabnormals.pet_cemetery.core.registry.PCEntityTypes;
import com.teamabnormals.pet_cemetery.core.registry.PCItems;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

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
		event.enqueueWork(() -> {
			ItemBlockRenderTypes.setRenderLayer(PCBlocks.COMPANION_COIL.get(), RenderType.cutout());
		});
	}

	private void dataSetup(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(new PCEntityTypeTagsProvider(generator, helper));
		}

		if (event.includeClient()) {
			generator.addProvider(new PCItemModelProvider(generator, helper));
			generator.addProvider(new PCLanguageProvider(generator));
		}
	}

	private void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(PCEntityTypes.ZOMBIE_WOLF.get(), ZombieWolfRenderer::new);
		event.registerEntityRenderer(PCEntityTypes.ZOMBIE_CAT.get(), ZombieCatRenderer::new);
		event.registerEntityRenderer(PCEntityTypes.ZOMBIE_PARROT.get(), ZombieParrotRenderer::new);
		event.registerEntityRenderer(PCEntityTypes.SKELETON_WOLF.get(), SkeletonWolfRenderer::new);
		event.registerEntityRenderer(PCEntityTypes.SKELETON_CAT.get(), SkeletonCatRenderer::new);
		event.registerEntityRenderer(PCEntityTypes.SKELETON_PARROT.get(), SkeletonParrotRenderer::new);
	}

	@OnlyIn(Dist.CLIENT)
	private void registerItemColors(ColorHandlerEvent.Item event) {
		event.getItemColors().register((stack, color) -> color > 0 ? -1 : ((ForgottenCollarItem) stack.getItem()).getColor(stack), PCItems.FORGOTTEN_COLLAR.get());
	}

	@OnlyIn(Dist.CLIENT)
	private void registerLayers(EntityRenderersEvent.AddLayers event) {
		event.getSkins().forEach(skin -> {
			PlayerRenderer renderer = event.getSkin(skin);
			renderer.addLayer(new UndeadParrotLayer<>(renderer, event.getEntityModels()));
		});
	}
}
package com.teamabnormals.pet_cemetery.core.other;

import com.teamabnormals.pet_cemetery.client.renderer.entity.UndeadCatRenderer;
import com.teamabnormals.pet_cemetery.client.renderer.entity.UndeadParrotRenderer;
import com.teamabnormals.pet_cemetery.client.renderer.entity.UndeadWolfRenderer;
import com.teamabnormals.pet_cemetery.client.renderer.entity.layers.UndeadParrotOnShoulderLayer;
import com.teamabnormals.pet_cemetery.core.PetCemetery;
import com.teamabnormals.pet_cemetery.core.registry.PCEntityTypes;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = PetCemetery.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PCModelLayers {
	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(PCEntityTypes.ZOMBIE_WOLF.get(), UndeadWolfRenderer::new);
		event.registerEntityRenderer(PCEntityTypes.ZOMBIE_CAT.get(), UndeadCatRenderer::new);
		event.registerEntityRenderer(PCEntityTypes.ZOMBIE_PARROT.get(), UndeadParrotRenderer::new);
		event.registerEntityRenderer(PCEntityTypes.SKELETON_WOLF.get(), UndeadWolfRenderer::new);
		event.registerEntityRenderer(PCEntityTypes.SKELETON_CAT.get(), UndeadCatRenderer::new);
		event.registerEntityRenderer(PCEntityTypes.SKELETON_PARROT.get(), UndeadParrotRenderer::new);
	}
	@SubscribeEvent
	public static void registerLayers(EntityRenderersEvent.AddLayers event) {
		event.getSkins().forEach(skin -> {
			PlayerRenderer renderer = event.getSkin(skin);
			renderer.addLayer(new UndeadParrotOnShoulderLayer<>(renderer, event.getEntityModels()));
		});
	}
}
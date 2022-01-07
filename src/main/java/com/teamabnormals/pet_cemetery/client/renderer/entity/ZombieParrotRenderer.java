package com.teamabnormals.pet_cemetery.client.renderer.entity;

import com.teamabnormals.pet_cemetery.core.PetCemetery;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ParrotRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Parrot;

public class ZombieParrotRenderer extends ParrotRenderer {
	public static final ResourceLocation TEXTURE = new ResourceLocation(PetCemetery.MOD_ID, "textures/entity/parrot/zombie_parrot.png");

	public ZombieParrotRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(Parrot entity) {
		return TEXTURE;
	}
}
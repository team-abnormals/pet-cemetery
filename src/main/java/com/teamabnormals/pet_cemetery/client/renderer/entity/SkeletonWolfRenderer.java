package com.teamabnormals.pet_cemetery.client.renderer.entity;

import com.teamabnormals.pet_cemetery.core.PetCemetery;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;

public class SkeletonWolfRenderer extends WolfRenderer {
	private static final ResourceLocation SKELETON_WOLF_TEXTURES = new ResourceLocation(PetCemetery.MOD_ID, "textures/entity/wolf/skeleton_wolf.png");

	public SkeletonWolfRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(Wolf entity) {
		return SKELETON_WOLF_TEXTURES;
	}
}
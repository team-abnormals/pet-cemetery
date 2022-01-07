package com.teamabnormals.pet_cemetery.client.renderer.entity;

import com.teamabnormals.pet_cemetery.core.PetCemetery;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cat;

public class SkeletonCatRenderer extends CatRenderer {
	private static final ResourceLocation SKELETON_CAT_TEXTURES = new ResourceLocation(PetCemetery.MOD_ID, "textures/entity/cat/skeleton_cat.png");

	public SkeletonCatRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(Cat entity) {
		return SKELETON_CAT_TEXTURES;
	}
}
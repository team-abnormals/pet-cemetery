package com.teamabnormals.pet_cemetery.client.renderer.entity;

import com.teamabnormals.pet_cemetery.core.PetCemetery;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cat;

public class ZombieCatRenderer extends CatRenderer {
	private static final ResourceLocation ZOMBIE_CAT_TEXTURES = new ResourceLocation(PetCemetery.MOD_ID, "textures/entity/cat/zombie_cat.png");

	public ZombieCatRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(Cat entity) {
		return ZOMBIE_CAT_TEXTURES;
	}
}
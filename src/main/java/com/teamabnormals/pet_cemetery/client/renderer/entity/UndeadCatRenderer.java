package com.teamabnormals.pet_cemetery.client.renderer.entity;

import com.teamabnormals.pet_cemetery.common.entity.ZombieCat;
import com.teamabnormals.pet_cemetery.core.PetCemetery;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cat;

public class UndeadCatRenderer extends CatRenderer {
	private static final ResourceLocation ZOMBIE_CAT_TEXTURE = new ResourceLocation(PetCemetery.MOD_ID, "textures/entity/cat/zombie_cat.png");
	private static final ResourceLocation SKELETON_CAT_TEXTURE = new ResourceLocation(PetCemetery.MOD_ID, "textures/entity/cat/skeleton_cat.png");

	public UndeadCatRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(Cat entity) {
		return entity instanceof ZombieCat ? ZOMBIE_CAT_TEXTURE : SKELETON_CAT_TEXTURE;
	}
}
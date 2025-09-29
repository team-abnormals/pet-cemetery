package com.teamabnormals.pet_cemetery.client.renderer.entity;

import com.teamabnormals.pet_cemetery.common.entity.ZombieWolf;
import com.teamabnormals.pet_cemetery.core.PetCemetery;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;

public class UndeadWolfRenderer extends WolfRenderer {
	private static final ResourceLocation ZOMBIE_WOLF_TEXTURE = PetCemetery.location("textures/entity/wolf/zombie_wolf.png");
	private static final ResourceLocation SKELETON_WOLF_TEXTURE = PetCemetery.location("textures/entity/wolf/skeleton_wolf.png");

	public UndeadWolfRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(Wolf entity) {
		return entity instanceof ZombieWolf ? ZOMBIE_WOLF_TEXTURE : SKELETON_WOLF_TEXTURE;
	}

	@Override
	protected boolean isShaking(Wolf wolf) {
		return super.isShaking(wolf) || wolf instanceof ZombieWolf zombieWolf && zombieWolf.isConverting();
	}
}
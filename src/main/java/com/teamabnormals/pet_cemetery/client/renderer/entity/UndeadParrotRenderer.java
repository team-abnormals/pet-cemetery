package com.teamabnormals.pet_cemetery.client.renderer.entity;

import com.teamabnormals.pet_cemetery.common.entity.ZombieParrot;
import com.teamabnormals.pet_cemetery.core.PetCemetery;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ParrotRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Parrot;

public class UndeadParrotRenderer extends ParrotRenderer {
	public static final ResourceLocation ZOMBIE_PARROT_TEXTURE = PetCemetery.location("textures/entity/parrot/zombie_parrot.png");
	public static final ResourceLocation SKELETON_PARROT_TEXTURE = PetCemetery.location("textures/entity/parrot/skeleton_parrot.png");

	public UndeadParrotRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(Parrot entity) {
		return entity instanceof ZombieParrot ? ZOMBIE_PARROT_TEXTURE : SKELETON_PARROT_TEXTURE;
	}

	@Override
	protected boolean isShaking(Parrot parrot) {
		return super.isShaking(parrot) || parrot instanceof ZombieParrot zombieParrot && zombieParrot.isConverting();
	}
}
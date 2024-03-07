package com.teamabnormals.pet_cemetery.core.data.server;

import com.teamabnormals.pet_cemetery.common.advancement.CuredZombiePetTrigger;
import com.teamabnormals.pet_cemetery.common.advancement.RespawnPetTrigger;
import com.teamabnormals.pet_cemetery.core.PetCemetery;
import com.teamabnormals.pet_cemetery.core.other.PCUtil;
import com.teamabnormals.pet_cemetery.core.other.tags.PCEntityTypeTags;
import com.teamabnormals.pet_cemetery.core.registry.PCItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.EntityTypePredicate;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.common.data.ForgeAdvancementProvider.AdvancementGenerator;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class PCAdvancementProvider implements AdvancementGenerator {

	public static ForgeAdvancementProvider create(PackOutput output, CompletableFuture<Provider> provider, ExistingFileHelper helper) {
		return new ForgeAdvancementProvider(output, provider, helper, List.of(new PCAdvancementProvider()));
	}

	@Override
	public void generate(Provider provider, Consumer<Advancement> consumer, ExistingFileHelper helper) {
		ItemStack stack = new ItemStack(PCItems.PET_COLLAR.get());

		createAdvancement("respawn_pet", "nether", new ResourceLocation("nether/charge_respawn_anchor"), stack, FrameType.TASK, true, true, false)
				.addCriterion("respawn_pet", RespawnPetTrigger.TriggerInstance.respawnPet()).save(consumer, PetCemetery.MOD_ID + ":nether/respawn_pet");

		stack.getOrCreateTag().putInt(PCUtil.COLLAR_COLOR, DyeColor.GREEN.getId());
		createAdvancement("cured_zombie_pet", "nether", new ResourceLocation(PetCemetery.MOD_ID, "nether/respawn_pet"), stack, FrameType.GOAL, true, true, false)
				.addCriterion("cured_zombie_pet", CuredZombiePetTrigger.TriggerInstance.curedZombiePet()).save(consumer, PetCemetery.MOD_ID + ":nether/cured_zombie_pet");

		stack.getOrCreateTag().putInt(PCUtil.COLLAR_COLOR, DyeColor.WHITE.getId());
		createAdvancement("respawn_zombie_pet", "nether", new ResourceLocation(PetCemetery.MOD_ID, "nether/respawn_pet"), stack, FrameType.TASK, true, true, false)
				.addCriterion("respawn_zombie_pet", RespawnPetTrigger.TriggerInstance.respawnPet(EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(PCEntityTypeTags.SKELETON_PETS)).build())).save(consumer, PetCemetery.MOD_ID + ":nether/respawn_zombie_pet");
	}

	private static Advancement.Builder createAdvancement(String name, String category, ResourceLocation parent, ItemStack icon, FrameType frame, boolean showToast, boolean announceToChat, boolean hidden) {
		return Advancement.Builder.advancement().parent(Advancement.Builder.advancement().build(parent)).display(icon,
				Component.translatable("advancements." + PetCemetery.MOD_ID + "." + category + "." + name + ".title"),
				Component.translatable("advancements." + PetCemetery.MOD_ID + "." + category + "." + name + ".description"),
				null, frame, showToast, announceToChat, hidden);
	}
}
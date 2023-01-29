package com.teamabnormals.pet_cemetery.core.data.server;

import com.teamabnormals.pet_cemetery.common.advancement.RespawnAnimalTrigger;
import com.teamabnormals.pet_cemetery.core.PetCemetery;
import com.teamabnormals.pet_cemetery.core.registry.PCItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;

public class PCAdvancementProvider extends AdvancementProvider {

	public PCAdvancementProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, existingFileHelper);
	}

	@Override
	protected void registerAdvancements(Consumer<Advancement> consumer, ExistingFileHelper existingFileHelper) {
		createAdvancement("respawn_pet", "nether", new ResourceLocation("nether/charge_respawn_anchor"), PCItems.FORGOTTEN_COLLAR.get(), FrameType.TASK, true, true, false)
				.addCriterion("respawn_pet", RespawnAnimalTrigger.TriggerInstance.respawnedAnimal()).save(consumer, PetCemetery.MOD_ID + ":nether/respawn_pet");
	}

	private static Advancement.Builder createAdvancement(String name, String category, ResourceLocation parent, ItemLike icon, FrameType frame, boolean showToast, boolean announceToChat, boolean hidden) {
		return Advancement.Builder.advancement().parent(Advancement.Builder.advancement().build(parent)).display(icon,
				Component.translatable("advancements." + PetCemetery.MOD_ID + "." + category + "." + name + ".title"),
				Component.translatable("advancements." + PetCemetery.MOD_ID + "." + category + "." + name + ".description"),
				null, frame, showToast, announceToChat, hidden);
	}
}
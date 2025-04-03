package com.teamabnormals.pet_cemetery.core.data.server;

import com.teamabnormals.pet_cemetery.common.advancement.CuredZombiePetTrigger;
import com.teamabnormals.pet_cemetery.common.advancement.RespawnPetTrigger;
import com.teamabnormals.pet_cemetery.core.PetCemetery;
import com.teamabnormals.pet_cemetery.core.other.PCUtil;
import com.teamabnormals.pet_cemetery.core.registry.PCDataComponents;
import com.teamabnormals.pet_cemetery.core.registry.PCItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.AdvancementProvider.AdvancementGenerator;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class PCAdvancementProvider implements AdvancementGenerator {

	public static AdvancementProvider create(PackOutput output, CompletableFuture<Provider> provider, ExistingFileHelper helper) {
		return new AdvancementProvider(output, provider, helper, List.of(new PCAdvancementProvider()));
	}

	@Override
	public void generate(Provider registries, Consumer<AdvancementHolder> consumer, ExistingFileHelper existingFileHelper) {
		ItemStack stack = new ItemStack(PCItems.PET_COLLAR.get());
		createAdvancement("respawn_pet", "nether", ResourceLocation.withDefaultNamespace("nether/charge_respawn_anchor"), stack, AdvancementType.TASK, true, true, false)
				.addCriterion().save(consumer, PetCemetery.MOD_ID + ":nether/respawn_pet");

		stack.getOrDefault(PCDataComponents.PET_DATA.get(), CustomData.EMPTY).copyTag().putInt(PCUtil.COLLAR_COLOR, DyeColor.GREEN.getId());
		createAdvancement("cured_zombie_pet", "nether", PetCemetery.location("nether/respawn_pet"), stack, AdvancementType.TASK, true, true, false)
				.addCriterion().save(consumer, PetCemetery.MOD_ID + ":nether/cured_zombie_pet");;

		stack.getOrDefault(PCDataComponents.PET_DATA.get(), CustomData.EMPTY).copyTag().putInt(PCUtil.COLLAR_COLOR, DyeColor.GREEN.getId());
		createAdvancement("respawn_zombie_pet", "nether", PetCemetery.location("nether/respawn_pet"), stack, AdvancementType.TASK, true, true, false)
				.addCriterion().save(consumer, PetCemetery.MOD_ID + ":nether/respawn_zombie_pet");;
	}


	private static Advancement.Builder createAdvancement(String name, String category, ResourceLocation parent, ItemStack icon, AdvancementType frame, boolean showToast, boolean announceToChat, boolean hidden) {
		return Advancement.Builder.advancement().parent(Advancement.Builder.advancement().build(parent)).display(icon,
				Component.translatable("advancements." + PetCemetery.MOD_ID + "." + category + "." + name + ".title"),
				Component.translatable("advancements." + PetCemetery.MOD_ID + "." + category + "." + name + ".description"),
				null, frame, showToast, announceToChat, hidden);
	}
}
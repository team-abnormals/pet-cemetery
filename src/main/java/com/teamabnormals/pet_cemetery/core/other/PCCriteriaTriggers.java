package com.teamabnormals.pet_cemetery.core.other;

import com.teamabnormals.pet_cemetery.common.advancement.ConvertedMobTrigger;
import com.teamabnormals.pet_cemetery.core.PetCemetery;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PCCriteriaTriggers {
	public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS = DeferredRegister.create(Registries.TRIGGER_TYPE, PetCemetery.MOD_ID);

	public static final DeferredHolder<CriterionTrigger<?>, ConvertedMobTrigger> RESPAWNED_PET = TRIGGERS.register("respawned_pet", ConvertedMobTrigger::new);
	public static final DeferredHolder<CriterionTrigger<?>, ConvertedMobTrigger> CURED_ZOMBIE_PET = TRIGGERS.register("cured_zombie_pet", ConvertedMobTrigger::new);
}

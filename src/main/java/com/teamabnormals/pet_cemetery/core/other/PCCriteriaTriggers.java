package com.teamabnormals.pet_cemetery.core.other;

import com.teamabnormals.pet_cemetery.common.advancement.RespawnAnimalTrigger;
import com.teamabnormals.pet_cemetery.core.PetCemetery;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = PetCemetery.MOD_ID)
public class PCCriteriaTriggers {
	public static final RespawnAnimalTrigger RESPAWN_ANIMAL = CriteriaTriggers.register(new RespawnAnimalTrigger());
}

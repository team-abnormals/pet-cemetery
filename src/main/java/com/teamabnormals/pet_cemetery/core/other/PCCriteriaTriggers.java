package com.teamabnormals.pet_cemetery.core.other;

import com.teamabnormals.pet_cemetery.common.advancement.CuredZombiePetTrigger;
import com.teamabnormals.pet_cemetery.common.advancement.RespawnPetTrigger;
import com.teamabnormals.pet_cemetery.core.PetCemetery;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = PetCemetery.MOD_ID)
public class PCCriteriaTriggers {
	public static final RespawnPetTrigger RESPAWN_PET = CriteriaTriggers.register(new RespawnPetTrigger());
	public static final CuredZombiePetTrigger CURED_ZOMBIE_PET = CriteriaTriggers.register(new CuredZombiePetTrigger());
}

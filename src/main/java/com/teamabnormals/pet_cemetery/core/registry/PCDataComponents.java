package com.teamabnormals.pet_cemetery.core.registry;

import com.teamabnormals.pet_cemetery.core.PetCemetery;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PCDataComponents {
	public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, PetCemetery.MOD_ID);

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<CustomData>> PET_DATA = COMPONENTS.register("pet_data", () -> DataComponentType.<CustomData>builder().persistent(CustomData.CODEC).networkSynchronized(CustomData.STREAM_CODEC).build());
}
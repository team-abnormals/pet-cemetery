package com.teamabnormals.pet_cemetery.core.registry;

import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import com.teamabnormals.pet_cemetery.common.block.CompanionCoilBlock;
import com.teamabnormals.pet_cemetery.core.PetCemetery;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = PetCemetery.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class PCBlocks {
	public static final BlockSubRegistryHelper HELPER = PetCemetery.REGISTRY_HELPER.getBlockSubHelper();

	public static final RegistryObject<Block> COMPANION_COIL = HELPER.createBlockNoItem("companion_coil", () -> new CompanionCoilBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)));
}
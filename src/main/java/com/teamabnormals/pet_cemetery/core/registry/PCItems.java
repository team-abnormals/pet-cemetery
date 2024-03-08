package com.teamabnormals.pet_cemetery.core.registry;

import com.teamabnormals.blueprint.core.util.item.CreativeModeTabContentsPopulator;
import com.teamabnormals.blueprint.core.util.registry.ItemSubRegistryHelper;
import com.teamabnormals.pet_cemetery.common.item.PetCollarItem;
import com.teamabnormals.pet_cemetery.core.PetCemetery;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.RegistryObject;

import static com.teamabnormals.blueprint.core.util.item.ItemStackUtil.is;
import static net.minecraft.world.item.CreativeModeTabs.*;
import static net.minecraft.world.item.crafting.Ingredient.of;

@EventBusSubscriber(modid = PetCemetery.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class PCItems {
	public static final ItemSubRegistryHelper HELPER = PetCemetery.REGISTRY_HELPER.getItemSubHelper();

	public static final RegistryObject<Item> PET_COLLAR = HELPER.createItem("pet_collar", () -> new PetCollarItem(new Item.Properties().stacksTo(1).fireResistant()));

	public static final RegistryObject<ForgeSpawnEggItem> ZOMBIE_WOLF_SPAWN_EGG = HELPER.createSpawnEggItem("zombie_wolf", PCEntityTypes.ZOMBIE_WOLF::get, 0x6A9D5A, 0x364430);
	public static final RegistryObject<ForgeSpawnEggItem> ZOMBIE_CAT_SPAWN_EGG = HELPER.createSpawnEggItem("zombie_cat", PCEntityTypes.ZOMBIE_CAT::get, 0x4A7D52, 0x79AD69);
	public static final RegistryObject<ForgeSpawnEggItem> ZOMBIE_PARROT_SPAWN_EGG = HELPER.createSpawnEggItem("zombie_parrot", PCEntityTypes.ZOMBIE_PARROT::get, 0x315D39, 0x5A8D52);

	public static final RegistryObject<ForgeSpawnEggItem> SKELETON_WOLF_SPAWN_EGG = HELPER.createSpawnEggItem("skeleton_wolf", PCEntityTypes.SKELETON_WOLF::get, 0x979797, 0x494949);
	public static final RegistryObject<ForgeSpawnEggItem> SKELETON_CAT_SPAWN_EGG = HELPER.createSpawnEggItem("skeleton_cat", PCEntityTypes.SKELETON_CAT::get, 0xD3D3D3, 0x979797);
	public static final RegistryObject<ForgeSpawnEggItem> SKELETON_PARROT_SPAWN_EGG = HELPER.createSpawnEggItem("skeleton_parrot", PCEntityTypes.SKELETON_PARROT::get, 0x979797, 0xADABAD);


	public static void setupTabEditors() {
		CreativeModeTabContentsPopulator.mod(PetCemetery.MOD_ID)
				.tab(TOOLS_AND_UTILITIES)
				.addItemsBefore(of(Items.SADDLE), PET_COLLAR)
				.tab(SPAWN_EGGS)
				.addItemsAlphabetically(is(SpawnEggItem.class), ZOMBIE_WOLF_SPAWN_EGG, ZOMBIE_CAT_SPAWN_EGG, ZOMBIE_PARROT_SPAWN_EGG, SKELETON_WOLF_SPAWN_EGG, SKELETON_CAT_SPAWN_EGG, SKELETON_PARROT_SPAWN_EGG);
	}
}
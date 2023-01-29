package com.teamabnormals.pet_cemetery.core.other;

import com.google.common.collect.Lists;
import com.teamabnormals.pet_cemetery.core.PetCemetery;
import com.teamabnormals.pet_cemetery.core.other.tags.PCEntityTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = PetCemetery.MOD_ID)
public class PCEvents {

	@SubscribeEvent
	public static void onLivingSpawned(EntityJoinLevelEvent event) {
		Entity entity = event.getEntity();

		if (entity.getType().is(PCEntityTypeTags.DROPS_FORGOTTEN_COLLAR) && entity instanceof TamableAnimal pet) {
			List<Goal> goalsToRemove = Lists.newArrayList();
			pet.goalSelector.availableGoals.forEach((goal) -> {
				if (goal.getGoal() instanceof FloatGoal)
					goalsToRemove.add(goal.getGoal());
			});

			goalsToRemove.forEach(pet.goalSelector::removeGoal);
		}
	}
}

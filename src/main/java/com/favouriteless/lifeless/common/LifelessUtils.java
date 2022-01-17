package com.favouriteless.lifeless.common;

import net.minecraft.ChatFormatting;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;

public class LifelessUtils {

	public static void updateNamesDelayed(ServerPlayer player) {
		var executor = LogicalSidedProvider.WORKQUEUE.get(LogicalSide.SERVER);
		executor.tell(new TickTask(0, () -> {
			player.refreshTabListName();
			player.refreshDisplayName();
		}));
	}

	public static ChatFormatting getFormatting(int lives) {
		if(lives > 3) return ChatFormatting.DARK_GREEN;
		else if(lives > 2) return ChatFormatting.GREEN;
		else if(lives > 1) return ChatFormatting.YELLOW;
		else if(lives > 0) return ChatFormatting.RED;
		else return ChatFormatting.DARK_RED;
	}

}

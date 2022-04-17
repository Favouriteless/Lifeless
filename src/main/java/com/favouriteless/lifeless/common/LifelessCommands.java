package com.favouriteless.lifeless.common;

import com.favouriteless.lifeless.Lifeless;
import com.favouriteless.lifeless.LifelessConfig;
import com.favouriteless.lifeless.api.capabilities.IPlayerLifeCapability;
import com.favouriteless.lifeless.api.capabilities.PlayerLifeCapabilityManager;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.Collection;

@EventBusSubscriber(modid=Lifeless.MOD_ID)
public class LifelessCommands {

	@SubscribeEvent
	public static void onRegisterCommands(RegisterCommandsEvent event) {
		event.getDispatcher().register(
				Commands.literal("getlives").executes(command -> getLives(command.getSource(), null))
						.then(Commands.argument("player", EntityArgument.player())
								.executes(command -> getLives(command.getSource(), EntityArgument.getPlayer(command, "player"))))
		);
		event.getDispatcher().register(
				Commands.literal("givelife")
						.then(Commands.argument("player", EntityArgument.player())
								.executes(command -> giveLife(command.getSource(), EntityArgument.getPlayer(command, "player"))))
		);
		event.getDispatcher().register(
				Commands.literal("setlives").requires(player -> player.hasPermission(2))
						.then(Commands.argument("players", EntityArgument.players())
						.then(Commands.argument("lives", IntegerArgumentType.integer())
								.executes(command -> setLives(command.getSource(), EntityArgument.getPlayers(command, "players"), IntegerArgumentType.getInteger(command, "lives")))))
		);
		event.getDispatcher().register(
				Commands.literal("awardlife").requires(player -> player.hasPermission(2))
						.then(Commands.argument("players", EntityArgument.players())
						.executes(command -> awardLife(command.getSource(), EntityArgument.getPlayers(command, "players"))))
		);
	}

	private static int getLives(CommandSourceStack pSource, ServerPlayer target) {
		if(pSource.getLevel().getLevelData().isHardcore()) {
			int lives = 0;
			Entity entity;
			if(target == null)
				entity = pSource.getEntity();
			else
				entity = target;

			if(entity instanceof ServerPlayer player) {
				LazyOptional<IPlayerLifeCapability> lazy = player.getCapability(PlayerLifeCapabilityManager.INSTANCE);
				IPlayerLifeCapability cap = lazy.orElse(null);
				lives = cap.getValue();
				pSource.sendSuccess(new TranslatableComponent(String.format("%s has %s lives", player.getDisplayName().getString(), lives)).withStyle(LifelessUtils.getFormatting(lives)), true);
			}
			else {
				pSource.sendFailure(new TextComponent("Not a valid player.").withStyle(ChatFormatting.RED));
			}
			return lives;
		}
		else {
			pSource.sendFailure(new TextComponent("Lifeless only functions in hardcore worlds. Sorry!"));
		}
		return 0;
	}

	private static int setLives(CommandSourceStack pSource, Collection<ServerPlayer> targets, int lives) {
		if(pSource.getLevel().getLevelData().isHardcore()) {
			if(targets.isEmpty()) {
				if(pSource.getEntity() != null)
					setLives((ServerPlayer)pSource.getEntity(), lives);
				else
					return 0;
			}
			else {
				for(ServerPlayer player : targets) {
					setLives(player, lives);
				}
			}
		}
		else {
			pSource.sendFailure(new TextComponent("Lifeless only functions in hardcore worlds. Sorry!"));
		}
		return targets.isEmpty() ? 1 : targets.size();
	}

	private static void setLives(ServerPlayer player, int lives) {
		LazyOptional<IPlayerLifeCapability> lazy = player.getCapability(PlayerLifeCapabilityManager.INSTANCE);
		IPlayerLifeCapability cap = lazy.orElse(null);
		cap.setValue(lives);
		LifelessUtils.updateNamesDelayed(player);
	}

	private static int giveLife(CommandSourceStack pSource, ServerPlayer target) {
		if(pSource.getLevel().getLevelData().isHardcore()) {
			if(LifelessConfig.ENABLE_TRADING.get()) {
				Entity entity = pSource.getEntity();
				if(entity instanceof ServerPlayer original) {
					if(target != null) {
						LazyOptional<IPlayerLifeCapability> originalLazy = original.getCapability(PlayerLifeCapabilityManager.INSTANCE);
						originalLazy.ifPresent(cap -> cap.setValue(cap.getValue() - 1));
						LazyOptional<IPlayerLifeCapability> targetLazy = target.getCapability(PlayerLifeCapabilityManager.INSTANCE);
						targetLazy.ifPresent(cap -> cap.setValue(cap.getValue() + 1));

						target.displayClientMessage(new TextComponent(String.format("%s has given you a life.", original.getDisplayName().getString())).withStyle(ChatFormatting.GREEN), false);
						pSource.sendSuccess(new TextComponent(String.format("You give a life to %s.", original.getDisplayName().getString())).withStyle(ChatFormatting.GREEN), true);
						LifelessUtils.updateNamesDelayed(original);
						LifelessUtils.updateNamesDelayed(target);
					}
					else {
						pSource.sendFailure(new TextComponent("Invalid target player.").withStyle(ChatFormatting.RED));
					}
				}
				else {
					pSource.sendFailure(new TextComponent("Invalid source player.").withStyle(ChatFormatting.RED));
				}
			}
			else {
				pSource.sendFailure(new TextComponent("Trading is currently disabled.").withStyle(ChatFormatting.RED));
			}
		}
		else {
			pSource.sendFailure(new TextComponent("Lifeless only functions in hardcore worlds. Sorry!"));
		}
		return 0;
	}

	private static int awardLife(CommandSourceStack pSource, Collection<ServerPlayer> targets) {
		if(pSource.getLevel().getLevelData().isHardcore()) {
			for(ServerPlayer target : targets) {
				if(target != null) {
					LazyOptional<IPlayerLifeCapability> targetLazy = target.getCapability(PlayerLifeCapabilityManager.INSTANCE);
					targetLazy.ifPresent(cap -> cap.setValue(cap.getValue() + 1));

					target.displayClientMessage(new TextComponent("You have been awared a life.").withStyle(ChatFormatting.GREEN), false);
					pSource.sendSuccess(new TextComponent(String.format("You award a life to %s.", target.getDisplayName().getString())).withStyle(ChatFormatting.GREEN), true);
					LifelessUtils.updateNamesDelayed(target);
				}
			}
		}
		else {
			pSource.sendFailure(new TextComponent("Lifeless only functions in hardcore worlds. Sorry!"));
		}
		return targets.size();
	}

}

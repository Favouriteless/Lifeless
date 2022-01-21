package com.favouriteless.lifeless.common;

import com.favouriteless.lifeless.Lifeless;
import com.favouriteless.lifeless.LifelessConfig;
import com.favouriteless.lifeless.api.capabilities.IPlayerLifeCapability;
import com.favouriteless.lifeless.api.capabilities.PlayerLifeCapabilityManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket.Action;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import java.util.Random;

@EventBusSubscriber(modid=Lifeless.MOD_ID, bus=Bus.FORGE)
public class CommonEvents {

	public static final Random RANDOM = new Random();

	@SubscribeEvent
	public static void onEntityJoin(EntityJoinWorldEvent event) {
		if(!event.getWorld().isClientSide && event.getWorld().getLevelData().isHardcore()) {
			Entity entity = event.getEntity();
			if(entity instanceof ServerPlayer player) {
				LazyOptional<IPlayerLifeCapability> cap = player.getCapability(PlayerLifeCapabilityManager.INSTANCE);
				cap.ifPresent(source -> {
					if(!source.getInitialized()) {
						int lives = LifelessConfig.BASE_LIVES.get();
						lives += RANDOM.nextInt(LifelessConfig.RANDOM_LIVES.get() + 1);
						source.setValue(lives);
						source.setInitialized(true);
					}
					else if(source.getValue() <= 0){
						player.setGameMode(GameType.SPECTATOR);
					}
					updateNamesDelayed(player);
				});
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event) {
		if(!event.getOriginal().level.isClientSide && event.getOriginal().level.getLevelData().isHardcore()) {
			ServerPlayer player = (ServerPlayer)event.getPlayer();
			event.getOriginal().reviveCaps();
			event.getOriginal().getCapability(PlayerLifeCapabilityManager.INSTANCE).ifPresent(oldData ->
					player.getCapability(PlayerLifeCapabilityManager.INSTANCE).ifPresent(data -> {
						int lives = oldData.getValue();
						if(event.isWasDeath() && lives-- > 0)
							player.displayClientMessage(new TextComponent(String.format("Congratulations, you died. Lives remaining: %s", lives)).withStyle(ChatFormatting.RED), false);
						data.setValue(lives);
						data.setInitialized(oldData.getInitialized());
					}));
			event.getOriginal().invalidateCaps();
		}
	}

	@SubscribeEvent
	public static void onPlayerRespawn(PlayerRespawnEvent event) {
		if(!event.getPlayer().level.isClientSide && event.getPlayer().level.getLevelData().isHardcore() && event.getPlayer() instanceof ServerPlayer player) {
			updateNamesDelayed(player);
		}
	}

	@SubscribeEvent
	public static void onPlayerName(PlayerEvent.NameFormat event) {
		if(!event.getPlayer().level.isClientSide && event.getPlayer().level.getLevelData().isHardcore()) {
			event.getPlayer().getCapability(PlayerLifeCapabilityManager.INSTANCE).ifPresent(cap ->
					event.setDisplayname(new TextComponent(event.getUsername().getString()).withStyle(LifelessUtils.getFormatting(cap.getValue()))));
		}
	}

	@SubscribeEvent
	public static void onPlayerTabName(PlayerEvent.TabListNameFormat event) {
		if(!event.getPlayer().level.isClientSide && event.getPlayer().level.getLevelData().isHardcore()) {
			event.getPlayer().getCapability(PlayerLifeCapabilityManager.INSTANCE).ifPresent(cap ->
					event.setDisplayName(new TextComponent(event.getPlayer().getName().getString()).withStyle(LifelessUtils.getFormatting(cap.getValue()))));
		}
	}

	public static void updateNamesDelayed(ServerPlayer player) {
		var executor = LogicalSidedProvider.WORKQUEUE.get(LogicalSide.SERVER);
		executor.tell(new TickTask(0, () -> {
			player.refreshTabListName();
			player.refreshDisplayName();
		}));
	}

}

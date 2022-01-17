package com.favouriteless.lifeless.api.capabilities;

import com.favouriteless.lifeless.Lifeless;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@EventBusSubscriber(modid=Lifeless.MOD_ID)
public class PlayerLifeCapabilityManager {

	public static Capability<IPlayerLifeCapability> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});

	public PlayerLifeCapabilityManager() {}

	@SubscribeEvent
	public static void attach(final AttachCapabilitiesEvent<Entity> event) {
		Entity entity = event.getObject();
		if(entity instanceof Player) {
			final PlayerLifeCapabilityProvider provider = new PlayerLifeCapabilityProvider();
			event.addCapability(PlayerLifeCapabilityProvider.IDENTIFIER, provider);
		}
	}

	private static class PlayerLifeCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

		public static final ResourceLocation IDENTIFIER = new ResourceLocation(Lifeless.MOD_ID, "player_lives");

		private final IPlayerLifeCapability backend = new PlayerLifeCapability();
		private final LazyOptional<IPlayerLifeCapability> optionalData = LazyOptional.of(() -> backend);

		@NotNull
		@Override
		public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
			return PlayerLifeCapabilityManager.INSTANCE.orEmpty(cap, this.optionalData);
		}

		void invalidate() {
			this.optionalData.invalidate();
		}

		@Override
		public CompoundTag serializeNBT() {
			return this.backend.serializeNBT();
		}

		@Override
		public void deserializeNBT(CompoundTag nbt) {
			this.backend.deserializeNBT(nbt);
		}
	}

}
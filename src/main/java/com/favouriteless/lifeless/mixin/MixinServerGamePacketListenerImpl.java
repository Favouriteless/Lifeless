package com.favouriteless.lifeless.mixin;

import com.favouriteless.lifeless.api.capabilities.IPlayerLifeCapability;
import com.favouriteless.lifeless.api.capabilities.PlayerLifeCapabilityManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerGamePacketListenerImpl.class)
public class MixinServerGamePacketListenerImpl {

	@Shadow public ServerPlayer player;

	@Redirect(method="handleClientCommand", at = @At(value="INVOKE", target="Lnet/minecraft/server/MinecraftServer;isHardcore()Z"))
	public boolean handleClientCommandHardcore(MinecraftServer instance) {
		if(instance.isHardcore()) {
			LazyOptional<IPlayerLifeCapability> lazy = player.getCapability(PlayerLifeCapabilityManager.INSTANCE);
			IPlayerLifeCapability cap = lazy.orElse(null);
			return cap.getValue() <= 0;
		}
		return false;
	}

}

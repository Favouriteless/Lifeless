package com.favouriteless.lifeless.mixin;

import com.favouriteless.lifeless.Lifeless;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class MixinServerGamePacketListenerImpl {

	@Inject(method="handleClientCommand", at = @At("HEAD"))
	public void handleClientCommandHead(ServerboundClientCommandPacket pPacket, CallbackInfo callback) {
		Lifeless.DISABLE_HARDCORE = true;
	}

	@Inject(method="handleClientCommand", at = @At("TAIL"))
	public void handleClientCommandReturn(ServerboundClientCommandPacket pPacket, CallbackInfo callback) {
		Lifeless.DISABLE_HARDCORE = false;
	}

}

package com.favouriteless.lifeless.mixin;

import com.favouriteless.lifeless.Lifeless;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

	@Inject(method = "isHardcore", at = @At("HEAD"))
	public void isHardcore(CallbackInfoReturnable<Boolean> cir) {
		if(Lifeless.DISABLE_HARDCORE) cir.setReturnValue(false);
	}

}

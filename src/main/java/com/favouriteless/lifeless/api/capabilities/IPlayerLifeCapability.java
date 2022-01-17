package com.favouriteless.lifeless.api.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IPlayerLifeCapability extends INBTSerializable<CompoundTag> {
	int getValue();
	void setValue(int lives);
	boolean getInitialized();
	void setInitialized(boolean value);
}

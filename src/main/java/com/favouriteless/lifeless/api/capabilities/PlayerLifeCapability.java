package com.favouriteless.lifeless.api.capabilities;

import com.favouriteless.lifeless.LifelessConfig;
import net.minecraft.nbt.CompoundTag;

public class PlayerLifeCapability implements IPlayerLifeCapability {

	private static final String LIVES_KEY = "lives";
	private static final String LOGIN_KEY = "initialized";
	private int livesRemaining;
	private boolean initialized;

	@Override
	public int getValue() {
		return livesRemaining;
	}

	@Override
	public void setValue(int lives) {
		livesRemaining = lives;
	}

	@Override
	public boolean getInitialized() {
		return initialized;
	}

	@Override
	public void setInitialized(boolean value) {
		initialized = value;
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();
		nbt.putInt(LIVES_KEY, livesRemaining);
		nbt.putBoolean(LOGIN_KEY, initialized);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		if(nbt.contains(LIVES_KEY))
			livesRemaining = nbt.getInt(LIVES_KEY);
		else
			livesRemaining = LifelessConfig.BASE_LIVES.get();

		if(nbt.contains(LOGIN_KEY))
			initialized = nbt.getBoolean(LOGIN_KEY);
		else
			initialized = true;
	}

}

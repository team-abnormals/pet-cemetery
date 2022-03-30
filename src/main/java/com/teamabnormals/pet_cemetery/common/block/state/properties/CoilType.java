package com.teamabnormals.pet_cemetery.common.block.state.properties;

import net.minecraft.util.StringRepresentable;

public enum CoilType implements StringRepresentable {
	TOP("top"),
	MIDDLE("middle"),
	BOTTOM("bottom");

	private final String name;

	CoilType(String name) {
		this.name = name;
	}

	public String getSerializedName() {
		return this.name;
	}
}
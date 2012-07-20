package org.spout.api.material.basic;

import org.spout.api.material.BlockMaterial;

public class Unbreakable extends BlockMaterial {
	public Unbreakable() {
		super("Unbreakable");
		setHardness(100.f);
	}
}

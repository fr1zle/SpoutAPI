/*
 * This file is part of SpoutAPI (http://www.spout.org/).
 *
 * SpoutAPI is licensed under the SpoutDev License Version 1.
 *
 * SpoutAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * SpoutAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.api.generator.biome;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.io.store.map.MemoryStoreMap;
import org.spout.api.io.store.map.SimpleStoreMap;
import org.spout.api.map.DefaultedMap;
import org.spout.api.math.Vector3;

/**
 * A simple store wrapper that holds biomes and the selector.
 */
public final class BiomeMap {
	private final SimpleStoreMap<Vector3, Biome> biomeOverrides;
	private final SimpleStoreMap<Integer, Biome> map;
	private World world = null;
	private final int MINIMUM_BIOME_SIZE = 4;
	private BiomeSelector selector;

	public BiomeMap() {
		map = new MemoryStoreMap<Integer, Biome>();
		biomeOverrides = new MemoryStoreMap<Vector3, Biome>();
	}
	
	public void setWorld(World world) {
		this.world = world;
	}
	
	public Biome getBiomeRaw(int index){
		return map.get(Math.abs(index) % map.getSize());
	}

	public void setSelector(BiomeSelector selector) {
		this.selector = selector;
		selector.parent = this;
	}

	public void addBiome(Biome biome) {
		map.set(map.getSize(), biome);
	}

	public void setBiome(Vector3 loc, Biome biome) {
		biomeOverrides.set(loc, biome);
	}

	/**
	 * TODO This needs to generate a noise function relying on x and z to generate a map that is [0-map.getSize()] so that we can select
	 * Biomes for the biome generator
	 */
	public Biome getBiome(int x, int z, long seed) {
		return getBiome(x, 0, z, seed);
	}

	public Biome getBiome(int x, int y, int z, long seed) {
		return getBiome(new Vector3(x, y, z), seed);
	}

	/**
	 * Returns the biome at the current location.  If the position has a override, that override is used.
	 * @param position
	 * @param seed
	 * @return
	 */
	public Biome getBiome(Vector3 position, long seed) {
		if(selector == null) throw new IllegalStateException("Biome Selector is null and cannot set a selector");
		Biome biome = biomeOverrides.get(position);
		if (biome != null) {
			return biome;
		}
		if (world != null) {
			Chunk chunk = world.getChunkFromBlock(position, false);
			if (chunk != null) {
				DefaultedMap<String, Serializable> map = chunk.getDataMap();
				
				if (map.containsKey("BiomeData")) {
					
				}
			}
		}

		return null;
	}
	
	public Collection<Biome> getBiomes() {
		Set<Biome> biomes = new HashSet<Biome> (map.getValues());
		biomes.addAll(biomeOverrides.getValues());
		return biomes;
	}
	
	public int indexOf(Biome biome) {
		if(map.reverseGet(biome) != null) {
			return map.reverseGet(biome);
		} else {
			return -1;
		}
	}
}

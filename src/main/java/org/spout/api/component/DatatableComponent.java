/*
 * This file is part of SpoutAPI.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * SpoutAPI is licensed under the Spout License Version 1.
 *
 * SpoutAPI is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * SpoutAPI is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.api.component;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.spout.api.datatable.ManagedHashMap;
import org.spout.api.datatable.SerializableMap;
import org.spout.api.map.DefaultedKey;

public class DatatableComponent extends Component implements SerializableMap {
	private final SerializableMap dataMap;
	private final AtomicBoolean dirty = new AtomicBoolean(true);

	public DatatableComponent() {
		this.dataMap = newMap();
	}

	public final SerializableMap getBaseMap() {
		dirty.set(true);
		return dataMap;
	}
	
	public SerializableMap newMap() {
		return new ManagedHashMap();
	}

	@Override
	public boolean isDetachable() {
		return false;
	}

	@Override
	public <T extends Serializable> T put(DefaultedKey<T> key, T value) {
		dirty.set(true);
		return dataMap.put(key, value);
	}

	@Override
	public <T extends Serializable> T putIfAbsent(DefaultedKey<T> key, T value) {
		dirty.set(true);
		return dataMap.putIfAbsent(key, value);
	}

	@Override
	public Serializable get(Object key) {
		dirty.set(true);
		return dataMap.get(key);
	}

	@Override
	public <T extends Serializable> T get(DefaultedKey<T> key) {
		dirty.set(true);
		return dataMap.get(key);
	}

	@Override
	public <T extends Serializable> T get(Object key, T defaultValue) {
		dirty.set(true);
		return dataMap.get(key, defaultValue);
	}

	@Override
	public boolean containsKey(Object key) {
		return dataMap.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return dataMap.containsValue(value);
	}

	@Override
	public int size() {
		return this.dataMap.size();
	}

	@Override
	public boolean isEmpty() {
		return this.dataMap.isEmpty();
	}

	@Override
	public Serializable remove(Object key) {
		dirty.set(true);
		return this.dataMap.remove(key);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Serializable> m) {
		dirty.set(true);
		this.dataMap.putAll(m);
	}

	@Override
	public void clear() {
		dirty.set(true);
		this.dataMap.clear();
	}

	@Override
	public Set<String> keySet() {
		return this.dataMap.keySet();
	}

	@Override
	public Collection<Serializable> values() {
		dirty.set(true);
		return this.dataMap.values();
	}

	@Override
	public Set<java.util.Map.Entry<String, Serializable>> entrySet() {
		dirty.set(true);
		return this.dataMap.entrySet();
	}

	@Override
	public Serializable put(String key, Serializable value) {
		dirty.set(true);
		return this.dataMap.put(key, value);
	}

	@Override
	public Serializable putIfAbsent(String key, Serializable value) {
		dirty.set(true);
		return this.dataMap.put(key, value);
	}

	@Override
	public byte[] serialize() {
		return dataMap.serialize();
	}

	@Override
	public void deserialize(byte[] data) throws IOException {
		dataMap.deserialize(data);
	}

	@Override
	public void deserialize(byte[] data, boolean wipe) throws IOException {
		dataMap.deserialize(data, wipe);
	}

	@Override
	public SerializableMap deepCopy() {
		return dataMap.deepCopy();
	}

	@Override
	public <T> T get(String key, Class<T> clazz) {
		dirty.set(true);
		return dataMap.<T>get(key, clazz);
	}
	
	public boolean isDirty() {
		return dirty.get();
	}
	
	public void onSend() {
		dirty.set(false);
	}
}

/*
 * This file is part of SpoutAPI.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spout.api.entity.component;

import org.spout.api.entity.Entity;
import org.spout.api.entity.EntityComponent;

public abstract class BasicEntityComponent implements EntityComponent {
	private Entity parent;

	/**
	 * Called when this controller is attached to an entity.
	 * @param e entity this controller will be attached to.
	 */
	@Override
	public void attachToEntity(Entity e) {
		this.parent = e;
	}

	/**
	 * Gets the parent Entity associated with this controller.
	 *
	 * @return parent Entity
	 */
	@Override
	public Entity getParent() {
		return this.parent;
	}

	/**
	 * Called when this component is attached to an entity.
	 */
	@Override
	public abstract void onAttached();

	/** 
	 * Called when this component is detached from an entity.
	 */
	@Override
	public void onDetached() {
		
	}
}

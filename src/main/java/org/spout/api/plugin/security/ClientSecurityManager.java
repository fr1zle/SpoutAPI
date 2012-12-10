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
package org.spout.api.plugin.security;

import java.net.MalformedURLException;
import java.security.CodeSource;
import java.security.Permission;
import java.util.List;
import java.util.logging.Level;

import org.spout.api.Spout;
import org.spout.api.plugin.Plugin;

public class ClientSecurityManager extends CommonSecurityManager {

	/**
	 * The depth of the caller stack.
	 * It is needed to find the calling class in the current call stack.
	 */
	private static final int CALL_STACK_DEPTH = 3;
	private static boolean isSunReflectionClassAvailable = false;

	static {
		try {
			Class<?> reflectionClass = Class.forName("sun.reflect.Reflection");
			if (reflectionClass != null) {
				isSunReflectionClassAvailable = true;
			}
		} catch (ClassNotFoundException e) {
			// We are running on a JVM which does not have the sun.reflect.Reflection class.
			// Use the fallback for getting the caller class.
			isSunReflectionClassAvailable = false;
		}
	}

	public ClientSecurityManager(double key) {
		super(key);
	}

	@Override
	public void checkPermission(Permission perm) {
//		super.checkPermission(perm);
		Class<?> callerClass = getCallerClass();
		CodeSource codeSource = callerClass.getProtectionDomain().getCodeSource();
		if (!isClassEngineClass(callerClass)) {
			Plugin callingPlugin = findPlugin(codeSource);
			if (callingPlugin == null) {
				throw new SecurityException(String.format("The Plugin %s was not probably enabled but tried to execute a privileged action!", codeSource.getLocation().toExternalForm()));
			}
			checkCallingPluginPermission(callingPlugin, perm);
		}
		// Do nothing, the engine is allowed to do 'anything'
	}

	private void checkCallingPluginPermission(Plugin callingPlugin, Permission perm) {
		// TODO Auto-generated method stub

	}

	private Plugin findPlugin(CodeSource codeSource) {
		List<Plugin> plugins = Spout.getEngine().getPluginManager().getPlugins();
		for (Plugin plugin : plugins) {
			try {
				if (codeSource.getLocation().equals(plugin.getFile().toURI().toURL())) {
					return plugin;
				}
			} catch (MalformedURLException e) {
				Spout.getLogger().log(Level.WARNING, e.getMessage(), e);
			}
		}
		return null;
	}

	private boolean isClassEngineClass(Class<?> callerClass) {
		return Spout.getEngine().getClass().getProtectionDomain().getCodeSource().equals(callerClass.getProtectionDomain().getCodeSource());
	}

	@SuppressWarnings("restriction")
	// We are using a restricted class here, since it is a way faster method of finding the caller class.
	private Class<?> getCallerClass() {
		if (isSunReflectionClassAvailable) {
			return sun.reflect.Reflection.getCallerClass(CALL_STACK_DEPTH);
		} else {
			// A slower fallback for getting the caller class. Still faster than using the stack trace.
			return getClassContext()[CALL_STACK_DEPTH];
		}
	}
}

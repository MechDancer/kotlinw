package org.mechdancer.kotlinw.internal

import java.io.File

class PathClassLoader(
	private val path: String,
	parent: ClassLoader? = null
) : ClassLoader(parent) {
	override fun findClass(name: String): Class<*> =
		File("$path/${name.replace('.', '/')}.class")
			.readBytes()
			.let { defineClass(name, it, 0, it.size) }
}

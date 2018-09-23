package org.mechdancer.kotlinw.core

import java.io.File

/**
 * 路径类加载器
 * 从指定路径加载类文件
 * @param path 路径
 * @param parent 父类加载器 默认为 `null`
 */
class PathClassLoader(
		private val path: String,
		parent: ClassLoader? = null
) : ClassLoader(parent) {
	override fun findClass(name: String): Class<*> =
			File("$path/${name.replace('.', '/')}.class")
					.readBytes()
					.let { defineClass(name, it, 0, it.size) }
}

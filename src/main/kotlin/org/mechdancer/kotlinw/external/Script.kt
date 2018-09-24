package org.mechdancer.kotlinw.external

import org.mechdancer.kotlinw.core.Analyzer
import org.mechdancer.kotlinw.core.Compiler
import org.mechdancer.kotlinw.core.EnvironmentUtil

/**
 * 将一段代码文本视作脚本并转换为函数
 */
inline fun <reified T, R> compileScript(
		action: String,
		`package`: String = "script",
		vararg reference: String
): T.() -> R =
		buildString {
			if (`package`.isNotBlank()) append("package $`package`\n")
			reference.forEach { append("import $it\n") }
			"""
				fun ${T::class.qualifiedName}.f() = run {
					$action
				}
			""".trimIndent().let(::append)
		}
				.let {
					val environment = EnvironmentUtil.createEnvironment()
					val ktFile = Analyzer.parse(it, environment)
					val result = Compiler.compile(ktFile, environment)

					Compiler
							.loadClasses(result)[0]
							.methods
							.first { method -> method.name == "f" }
				}.let {
					{
						@Suppress("UNCHECKED_CAST")
						it(null, this) as R
					}
				}

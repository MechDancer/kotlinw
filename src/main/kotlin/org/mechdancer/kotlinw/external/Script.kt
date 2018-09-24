package org.mechdancer.kotlinw.external

import org.mechdancer.kotlinw.core.Analyzer
import org.mechdancer.kotlinw.core.Compiler
import org.mechdancer.kotlinw.core.EnvironmentUtil

/**
 * 将一段代码文本视作脚本并转换为函数
 */
fun <R> Any?.script(
	action: String,
	`package`: String = "script",
	vararg reference: String
) =
	StringBuilder()
		.apply {
			if (`package`.isNotBlank()) append("package $`package`\n")
			reference.forEach { append("import $it\n") }
			"""
			fun Any?.f() = run {
				$action
			}
			""".trimIndent().let(::append)
		}
		.toString()
		.run {
			val environment = EnvironmentUtil.createEnvironment()
			val ktFile = Analyzer.parse(this@run, environment)
			val result = Compiler.compile(ktFile, environment)

			Compiler
				.loadClasses(result)[0]
				.methods
				.first { it.name == "f" }
				.let { function ->
					{
						@Suppress("UNCHECKED_CAST")
						function.invoke(null, this@script) as R
					}
				}
		}

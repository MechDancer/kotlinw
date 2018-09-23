package org.mechdancer.kotlinw.external

import org.mechdancer.kotlinw.core.Analyzer
import org.mechdancer.kotlinw.core.Compiler
import org.mechdancer.kotlinw.core.EnvironmentUtil

/**
 * 将一段代码文本视作脚本并转换为函数
 */
fun script(
	action: String,
	`package`: String = "script",
	reference: List<String> = listOf()
) =
	StringBuilder().apply {
		if (`package`.isNotBlank()) append("package $`package`\n")
		reference.forEach { append("import $it\n") }
		append("""
			fun f() = run {
				$action
			}
		""".trimIndent())
	}
		.toString()
		.let {
			val environment = EnvironmentUtil.createEnvironment()
			val ktFile = Analyzer.parse(it, environment)
			val result = Compiler.compile(ktFile, environment)
			Compiler
				.loadClasses(result)[0]
				.getMethod("f")
		}.let { { it(null) } }



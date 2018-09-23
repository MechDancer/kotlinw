package org.mechdancer.kotlinw.internal

import org.jetbrains.kotlin.cli.jvm.config.addJvmClasspathRoot
import org.jetbrains.kotlin.cli.jvm.repl.ReplInterpreter
import org.jetbrains.kotlin.cli.jvm.repl.configuration.ConsoleReplConfiguration
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration

class Evaluator {

	private val interpreter = ReplInterpreter(
			EnvironmentUtil.createDisposable(),
			CompilerConfiguration().apply {
				addJvmClasspathRoot(EnvironmentUtil.getStdlibPath())
				put(CommonConfigurationKeys.MODULE_NAME, "foo")
			},
			ConsoleReplConfiguration())

	fun eval(code: String) =
			interpreter.eval(code)
}
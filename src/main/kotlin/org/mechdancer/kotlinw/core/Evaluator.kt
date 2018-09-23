package org.mechdancer.kotlinw.core

import org.jetbrains.kotlin.cli.jvm.config.addJvmClasspathRoot
import org.jetbrains.kotlin.cli.jvm.repl.ReplInterpreter
import org.jetbrains.kotlin.cli.jvm.repl.configuration.ConsoleReplConfiguration
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration

/**
 * 解释器
 */
object Evaluator {
	private val interpreter = ReplInterpreter(
			EnvironmentUtil.createDisposable(),
			CompilerConfiguration().apply {
				addJvmClasspathRoot(EnvironmentUtil.getStdlibPath())
				put(CommonConfigurationKeys.MODULE_NAME, "foo")
			},
			ConsoleReplConfiguration())

	/**
	 * 交互式解释器
	 * @param code 输入代码
	 * @return 执行结果
	 */
	fun eval(code: String) = interpreter.eval(code)
}

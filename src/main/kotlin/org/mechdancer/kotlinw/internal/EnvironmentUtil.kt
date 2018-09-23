package org.mechdancer.kotlinw.internal

import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.cli.jvm.config.addJvmClasspathRoot
import org.jetbrains.kotlin.com.intellij.openapi.Disposable
import org.jetbrains.kotlin.com.intellij.psi.PsiFileFactory
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.utils.PathUtil

/** 环境变量 */
object EnvironmentUtil {
	/** kotlin 标准库 */
	fun getStdlibPath() = PathUtil.getResourcePathForClass(Function0::class.java)

	/** kotlin 工作路径 */
	fun getCurrentPath() = PathUtil.getResourcePathForClass(EnvironmentUtil::class.java)

	/** ???? */
	fun createDisposable() = Disposable { }

	/** ???? */
	fun getPsiDactory(environment: KotlinCoreEnvironment) =
		PsiFileFactory.getInstance(environment.project)

	/** 构造环境变量实例 */
	fun createEnvironment(name: String? = null): KotlinCoreEnvironment {
		val config = CompilerConfiguration().apply {
			addJvmClasspathRoot(getCurrentPath())
			addJvmClasspathRoot(getStdlibPath())
			put(CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY, MessageCollector.NONE)
			put(CommonConfigurationKeys.MODULE_NAME, name ?: "foo")
		}
		return KotlinCoreEnvironment.createForProduction(
			createDisposable(),
			config,
			EnvironmentConfigFiles.JVM_CONFIG_FILES
		)
	}
}

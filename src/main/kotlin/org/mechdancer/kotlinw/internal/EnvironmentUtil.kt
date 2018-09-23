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

/**
 * 编译环境工具类
 */
object EnvironmentUtil {
	/** kotlin 标准库路径 */
	fun getStdlibPath() = PathUtil.getResourcePathForClass(Function0::class.java)

	/** 当前项目路径 */
	fun getCurrentPath() = PathUtil.getResourcePathForClass(EnvironmentUtil::class.java)

	/**
	 * 创建空的 "可丢弃" 实例 intellij api 中需要
	 * 请见 [Disposable]
	 * */
	fun createDisposable() = Disposable { }

	/**
	 * 从编译环境获取工程的 Psi 文件工厂
	 * 请见 [org.jetbrains.kotlin.com.intellij.psi.PsiFile]
	 * */
	fun getPsiFactory(environment: KotlinCoreEnvironment): PsiFileFactory =
			PsiFileFactory.getInstance(environment.project)

	/**
	 * 构建编译环境实例
	 *
	 * @param name 用于生成代码的 kotlin 模块名。默认为 `null`
	 */
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

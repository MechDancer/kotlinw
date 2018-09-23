package org.mechdancer.kotlinw.core

import org.jetbrains.kotlin.backend.jvm.JvmIrCodegenFactory
import org.jetbrains.kotlin.cli.common.output.writeAllTo
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.codegen.*
import org.jetbrains.kotlin.codegen.state.GenerationState
import org.jetbrains.kotlin.config.JVMConfigurationKeys
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.resolve.AnalyzingUtils
import java.io.File

/**
 * 编译器工具类
 * 将 kt 文件编译为 class 文件
 */
object Compiler {
	/**
	 * 编译单个 kt 文件
	 * @param file kt psi 文件
	 * @param environment 编译环境
	 */
	fun compile(file: KtFile, environment: KotlinCoreEnvironment): GenerationState =
		compile(listOf(file), environment)

	/**
	 * 编译一组 kt 文件
	 * @param files 多个 kt psi 文件
	 * @param environment 编译环境
	 */
	fun compile(files: List<KtFile>, environment: KotlinCoreEnvironment): GenerationState {
		val result = Analyzer.analyze(files, environment)
		result.throwIfError()
		val state = GenerationState.Builder(
			environment.project, ClassBuilderFactories.BINARIES,
			result.moduleDescriptor, result.bindingContext,
			files, environment.configuration).codegenFactory(
			if (environment.configuration.getBoolean(JVMConfigurationKeys.IR))
				JvmIrCodegenFactory else DefaultCodegenFactory
		).build()
		if (result.shouldGenerateCode)
			KotlinCodegenFacade.compileCorrectFiles(state, CompilationErrorHandler.THROW_EXCEPTION)
		AnalyzingUtils.throwExceptionOnErrors(state.collectedExtraJvmDiagnostics)

		return state
	}

	/**
	 * 写入编译好的 class 至文件
	 * @param state 编译结果
	 * @param dir 要写入的文件夹
	 */
	fun writeToFile(state: GenerationState, dir: File) {
		state.factory.writeAllTo(dir)
	}

	/**
	 * 加载编译好的 class
	 * @param state 编译结果
	 * @return 编译完成的类
	 */
	fun loadClasses(state: GenerationState): List<Class<*>> {
		val loader = object : ClassLoader(javaClass.classLoader) {
			override fun findClass(name: String): Class<*> =
				state.factory.getClassFiles()
					.find {
						it.relativePath.replace('/', '.')
							.dropLast(6) == name
					}?.let {
						defineClass(name, it.asByteArray(), 0, it.asByteArray().size)
					} ?: throw ClassNotFoundException()
		}
		return state.factory.getClassFiles().map {
			loader.loadClass(it.relativePath.replace('/', '.').dropLast(6))
		}
	}
}

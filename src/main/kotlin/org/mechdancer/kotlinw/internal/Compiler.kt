package org.mechdancer.kotlinw.internal

import org.jetbrains.kotlin.backend.jvm.JvmIrCodegenFactory
import org.jetbrains.kotlin.cli.common.output.writeAllTo
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.codegen.*
import org.jetbrains.kotlin.codegen.state.GenerationState
import org.jetbrains.kotlin.config.JVMConfigurationKeys
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.resolve.AnalyzingUtils
import java.io.File

object Compiler {

	fun compile(file: KtFile, environment: KotlinCoreEnvironment): GenerationState =
			compile(listOf(file), environment)

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

	fun writeToFile(state: GenerationState, dir: File) {
		state.factory.writeAllTo(dir)
	}

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
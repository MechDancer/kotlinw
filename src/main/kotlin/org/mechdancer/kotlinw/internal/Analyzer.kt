package org.mechdancer.kotlinw.internal

import org.jetbrains.kotlin.analyzer.AnalysisResult
import org.jetbrains.kotlin.cli.jvm.compiler.CliBindingTrace
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.cli.jvm.compiler.TopDownAnalyzerFacadeForJVM
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.resolve.AnalyzingUtils

object Analyzer {

	fun parse(src: String, environment: KotlinCoreEnvironment): KtFile =
			EnvironmentUtil.getPsiDactory(environment)
					.createFileFromText("", KotlinLanguage.INSTANCE, src, false, false, true) as KtFile

	fun analyze(ktFiles: Collection<KtFile>, environment: KotlinCoreEnvironment): AnalysisResult {
		ktFiles.forEach { AnalyzingUtils.checkForSyntacticErrors(it) }
		val result = TopDownAnalyzerFacadeForJVM.analyzeFilesWithJavaIntegration(
				environment.project, ktFiles, CliBindingTrace(),
				environment.configuration, environment::createPackagePartProvider)
		AnalyzingUtils.throwExceptionOnErrors(result.bindingContext)
		return result
	}
}
package org.mechdancer.kotlinw.internal

import org.jetbrains.kotlin.analyzer.AnalysisResult
import org.jetbrains.kotlin.cli.jvm.compiler.CliBindingTrace
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.cli.jvm.compiler.TopDownAnalyzerFacadeForJVM
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.resolve.AnalyzingUtils

/**
 * 源码解析器工具类
 *
 * 负责将字符串源码解析为 [KtFile] psi 文件，
 * 并分析代码是否含有语法错误。
 */
object Analyzer {
	/**
	 * 解析为单个 kt 文件
	 *
	 * @param src 源码
	 * @param environment 编译环境 请见[EnvironmentUtil.createEnvironment]
	 * @return 解析完成的 psi 文件
	 *
	 */
	fun parse(src: String, environment: KotlinCoreEnvironment): KtFile =
			EnvironmentUtil
					.getPsiFactory(environment)
					.createFileFromText("", KotlinLanguage.INSTANCE, src, false, false, true)
					as KtFile

	/**
	 * 分析一组 kt 文件是否含有语法错误
	 *
	 * @param ktFiles 多个 kt psi 文件
	 * @param environment 编译环境
	 */
	fun analyze(ktFiles: Collection<KtFile>, environment: KotlinCoreEnvironment)
			: AnalysisResult {
		ktFiles.forEach { AnalyzingUtils.checkForSyntacticErrors(it) }
		val result = TopDownAnalyzerFacadeForJVM.analyzeFilesWithJavaIntegration(
				environment.project, ktFiles, CliBindingTrace(),
				environment.configuration, environment::createPackagePartProvider)
		AnalyzingUtils.throwExceptionOnErrors(result.bindingContext)
		return result
	}
}

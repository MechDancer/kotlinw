import org.junit.Assert
import org.junit.Test
import org.mechdancer.kotlinw.internal.Analyzer
import org.mechdancer.kotlinw.internal.Compiler
import org.mechdancer.kotlinw.internal.EnvironmentUtil

class TestCompile {
	@Test
	fun test() {
		val src = """
		@file:JvmName("Main")
		package banana

		fun b() = 2

		fun a() {
			println("a")
		}
	""".trimIndent()

		val environment = EnvironmentUtil.createEnvironment()
		val ktFile = Analyzer.parse(src, environment)
		val result = Compiler.compile(ktFile, environment)
		val classes = Compiler.loadClasses(result)

		classes[0].let {
			it.getMethod("a").invoke(null)
			Assert.assertEquals(2, it.getMethod("b").invoke(null))
		}
	}
}

import org.junit.Assert
import org.junit.Test
import org.mechdancer.kotlinw.external.compileScript

class TestScript {
	@Test
	fun test() {
		val script =
			"""
				println("hello world")
				this * 2
			""".trimIndent()
		val function = compileScript<Int, Int>(script)
		Assert.assertEquals(200, function(100))
	}
}

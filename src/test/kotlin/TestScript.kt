import org.junit.Assert
import org.junit.Test
import org.mechdancer.kotlinw.external.script

class TestScript {
	@Test
	fun test() {
		val r =
			"""
				println("hello world")
				this * 2
			"""
				.trimIndent()
				.run { 100.script<Int, Int>(this)() }
		Assert.assertEquals(200, r)
	}
}

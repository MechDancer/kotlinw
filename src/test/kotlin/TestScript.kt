import org.junit.Assert
import org.junit.Test
import org.mechdancer.kotlinw.external.script

class TestScript {
	@Test
	fun test() {
		val r =
			"""
				val num = this as Int
				println("hello world")
				num * 2
			"""
				.trimIndent()
				.run { 100.script<Int>(this)() }
		Assert.assertEquals(200, r)
	}
}
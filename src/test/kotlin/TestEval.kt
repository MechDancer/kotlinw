import org.junit.Assert
import org.junit.Test
import org.mechdancer.kotlinw.internal.Evaluator

class TestEval {
	@Test
	fun test() {
		val r = Evaluator.eval("1+1").toString()

		Assert.assertEquals("2 : kotlin.Int", r)
	}
}

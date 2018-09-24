package org.mechdancer.kotlinw.test

import org.junit.Assert
import org.junit.Test
import org.mechdancer.kotlinw.core.Evaluator

class TestEval {
	@Test
	fun test() {
		val r = Evaluator.eval("1+1")
		Assert.assertEquals("2 : kotlin.Int", r)
	}
}

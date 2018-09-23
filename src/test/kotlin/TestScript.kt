import org.mechdancer.kotlinw.external.script

fun main(args: Array<String>) {
	"""
		println("hello world")
		"ok"
	"""
		.trimIndent()
		.run { script(this)() }
		.let(::println)
}

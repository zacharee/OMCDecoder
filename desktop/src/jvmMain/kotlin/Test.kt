import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import tk.zwander.common.util.Coder

object Test {
    @JvmStatic
    fun main(args: Array<String>) {
        val coder = Coder()

        val scope = CoroutineScope(Job())
        scope.launch {
            val decoded = coder.decodeArray(Test::class.java.getResourceAsStream("cscfeature.xml").readAllBytes())
            println("Decoded")
            println(decoded.decodeToString())
        }
    }
}
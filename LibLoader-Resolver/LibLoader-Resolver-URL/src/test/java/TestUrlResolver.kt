import de.phyrone.libloader.resolver.url.UrlResolver
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.io.File
import kotlin.system.measureTimeMillis

class TestUrlResolver {


    @ParameterizedTest
    @ValueSource(strings = [
        "https://static.phyrone.de/download/LIB/MavenResolver-With-Dependencies-13.12.18.jar"
    ])
    fun testResolver(dependency: String) {
        val tempFile = File("testDir/")
        tempFile.deleteOnExit()
        val resolver = UrlResolver(tempFile)
        val time = measureTimeMillis {
            resolver.resolve(dependency).forEach {
                println("   - " + it.path)
            }
        }
println("Took $time ms")
    }
}
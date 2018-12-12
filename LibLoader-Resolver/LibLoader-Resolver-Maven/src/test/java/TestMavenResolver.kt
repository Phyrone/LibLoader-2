import de.phyrone.libloader.resolver.maven.DefaultMavenResolver
import de.phyrone.libloader.resolver.maven.MavenResolver
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.system.measureTimeMillis


class TestMavenResolver {


    @ParameterizedTest
    @ValueSource(strings = [
        "com.google.code.gson:gson:2.8.5",
        "com.google.guava:guava:27.0.1-jre",
        "org.apache.commons:commons-lang3:3.8.1",
        "com.uchuhimo:konf:0.12",
        "io.ktor:ktor-server-netty:1.0.1"
    ])
    fun testResolver(dependency: String) {
        val time = measureTimeMillis {
            DefaultMavenResolver.resolve(dependency).forEach {
                println("   - " + it.path)
            }
        }
        println("Resolve Took $time ms")
    }
}
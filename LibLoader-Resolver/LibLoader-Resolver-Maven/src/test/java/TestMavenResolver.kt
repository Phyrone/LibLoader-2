import de.phyrone.libloader.resolver.maven.DefaultMavenResolver
import de.phyrone.libloader.resolver.maven.noDependencyResolveOption
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
        "io.ktor:ktor-server-netty:1.0.1",
        "com.github.Phyrone.LibLoader-2:LibLoader-Resolver-Maven:90a9394275",
        "fr.minuskube.inv:smart-invs:1.2.5 -x",
        "com.github.Phyrone.Libs:JacksonWithDependecys:cfbbdf68fc"
    ])
    fun testResolver(dependency: String) {
        val time = measureTimeMillis {
            val result = DefaultMavenResolver.resolve(dependency)
            println("Files: ")
            result.forEach {
                println("   - " + it.path)
            }
        }
        println("Resolve Took $time ms")
    }
}
package vitruv.tools.vitruvadpter.testServer

import java.nio.file.Path
import java.nio.file.Paths


/**
 * Starts the server
 */

fun main() {
    val rootPath: Path = Paths.get("target", "root").toAbsolutePath()
    val serverInitializer = ServerInitializer()
    val server = serverInitializer.initialize(rootPath)
    server.start()
    println("Vitruvius server started on: " + serverInitializer.serverPort + " host: " + serverInitializer.host)
}

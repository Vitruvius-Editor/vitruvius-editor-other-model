package vitruv.tools.vitruvadpter.testServer

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Starts the server
 */

fun main(args: Array<String>) {
    val rootPath: Path = Paths.get("target", "root").toAbsolutePath()
    val hostname = if (args.isNotEmpty()) args[0] else "localhost"
    val port = if (args.size > 1) args[1].toIntOrNull() ?: 8000 else 8000

    val serverInitializer = ServerInitializer(hostname, port)
    val server = serverInitializer.initialize(rootPath)
    server.start()
    println("Vitruvius server started on: ${serverInitializer.serverPort} host: ${serverInitializer.host}")
}

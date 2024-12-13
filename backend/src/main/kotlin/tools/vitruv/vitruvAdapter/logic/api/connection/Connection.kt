package tools.vitruv.vitruvAdapter.logic.api.connection

import tools.vitruv.framework.views.ViewProvider
import tools.vitruv.framework.views.ViewTypeProvider
import java.nio.file.Path
import tools.vitruv.vitruvAdapter.exception.VitruviusConnectFailedException

/**
 * THIS CLASS IS ONLY AN IDEA AND NOT FINAL
 * This interface represents a connection to a Vitruvius server.
 * @author uhsab
 */
interface Connection {


    /**
     * Gets the view provider from the connection.
     * @return The view provider.
     */
    fun getViewProvider(): ViewProvider

    /**
     * Gets the view type provider from the connection.
     * @return The view type provider.
     */
    fun getViewTypeProvider(): ViewTypeProvider

    /**
     * Creates a connection to a Vitruvius server.
     * @param ip The ip of the server.
     * @param port The port of the server.
     * @param protocol The protocol of the server.
     * @param temporaryPath The path to an empty temporary directory where the {@link VitruvClient} can store temporary files.
     * @throws VitruviusConnectFailedException If the connection to the server failed.
     */
    fun createConnection(ip: String, port: Int, protocol: String, temporaryPath: Path)

    /**
     * Checks if the connection is established.
     * @return True if the connection is established, false otherwise.
     */
    fun isConnected(): Boolean

    fun getIp(): String

    fun getPort(): Int

    fun getProtocol(): String

    fun getTemporaryPath(): Path

}
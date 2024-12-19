package tools.vitruv.vitruvAdapter.vitruv.connection

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
     * Checks if the connection is established.
     * @return True if the connection is established, false otherwise.
     */
    fun isConnected(): Boolean

    /**
     * Gets the ip of the connection.
     * @return The ip of the connection.
     */
    fun getIp(): String

    /**
     * Gets the port of the connection.
     * @return The port of the connection.
     */
    fun getPort(): Int

    /**
     * Gets the protocol of the connection.
     * @return The protocol of the connection.
     */
    fun getProtocol(): String

    /**
     * Gets the temporary path of the connection.
     * @return The temporary path of the connection.
     */
    fun getTemporaryPath(): Path
}

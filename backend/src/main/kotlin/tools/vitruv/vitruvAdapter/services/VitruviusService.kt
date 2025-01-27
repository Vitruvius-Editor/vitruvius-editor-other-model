package tools.vitruv.vitruvAdapter.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tools.vitruv.framework.remote.client.VitruvClientFactory
import tools.vitruv.vitruvAdapter.dto.WindowSelectionRequest
import tools.vitruv.vitruvAdapter.exception.DisplayViewNotFoundException
import tools.vitruv.vitruvAdapter.vitruv.api.DisplayView
import tools.vitruv.vitruvAdapter.vitruv.api.VitruvAdapter
import tools.vitruv.vitruvAdapter.vitruv.api.Window
import tools.vitruv.vitruvAdapter.vitruv.impl.DisplayViewRepository
import java.util.*
import kotlin.io.path.Path
import kotlin.io.path.createTempDirectory

/**
 * This service handles all Vitruvius Interaction. It uses the [VitruvAdapter] to interact with a remote Vitruvius Server and
 * a [DisplayViewRepository] provided by the [DefaultDisplayViewRepositoryFactory] to store all implemented [DisplayView]s.
 *
 */
@Service
class VitruviusService {
    @Autowired
    lateinit var connectionService: ConnectionService

    @Autowired
    lateinit var vitruvAdapter: VitruvAdapter

    @Autowired
    lateinit var displayViewRepository: DisplayViewRepository

    /**
     * Returns all DisplayViews of a given connection.
     *
     * @param connectionId The id of the connection to operate on.
     * @return A list of available DisplayViews of the connection.
     */
    fun getDisplayViews(connectionId: UUID): Set<DisplayView> {
        setupConnection(connectionId)
        return vitruvAdapter.getDisplayViews()
    }

    /**
     * Returns all avaliable [Window]s of a [DisplayView].
     *
     * @param connectionId The id of the connection to operate on.
     * @param displayViewName The name of the [DisplayView].
     * @return A list of all avaliable [Window]s of the connection.
     */
    fun getDisplayViewWindows(
        connectionId: UUID,
        displayViewName: String,
    ): Set<String> {
        setupConnection(connectionId)
        val displayView = vitruvAdapter.getDisplayView(displayViewName)?: throw DisplayViewNotFoundException()
        return vitruvAdapter.getWindows(displayView)
    }

    /**
     * Returns the content of one or multiple [Window]s of a [DisplayView].
     *
     * @param connectionId The id of the connection to operate on.
     * @param displayViewName The name of the [DisplayView].
     * @param windowSelectionRequest Describes which [Window]s the returned content should include.
     * @return The content of the selected windows.
     */
    fun getDisplayViewContent(
        connectionId: UUID,
        displayViewName: String,
        windowSelectionRequest: WindowSelectionRequest,
    ): String {
        setupConnection(connectionId)
        val displayView = vitruvAdapter.getDisplayView(displayViewName)?: throw DisplayViewNotFoundException()
        return vitruvAdapter.createWindowContent(displayView, windowSelectionRequest.windows)
    }

    /**
     * Edits the content of one or multiple [Window]s of a [DisplayView] and returns the result if successful.
     *
     * @param connectionId The id of the connection to operate on.
     * @param displayViewName The name of the [DisplayView].
     * @param updatedContent The updated content that shall be synchronised with the Vitruvius server.
     * @return The updated content if the update was successful.
     */
    fun editDisplayViewContent(
        connectionId: UUID,
        displayViewName: String,
        updatedContent: String,
    ): String {
        setupConnection(connectionId)
        val displayView = vitruvAdapter.getDisplayView(displayViewName)?: throw DisplayViewNotFoundException()
        vitruvAdapter.editDisplayView(displayView, updatedContent)
        return updatedContent
    }

    /**
     * Helper function to setup a connection in the [VitruvAdapter] to the Vitruvius model server.
     *
     * @param connectionId The saved uuid of the connection.
     */
    private fun setupConnection(connectionId: UUID) {
        val connection = connectionService.getConnectionById(connectionId)
        // TODO: Add logic to allow other ports
        val client = VitruvClientFactory.create(connection.url, 8000, createTempDirectory("vitruvius-editor"))
        vitruvAdapter.setDisplayViewContainer(displayViewRepository)
        vitruvAdapter.connectClient(client)
    }
}

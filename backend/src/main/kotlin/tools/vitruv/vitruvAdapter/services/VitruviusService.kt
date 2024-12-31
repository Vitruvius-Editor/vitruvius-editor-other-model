package tools.vitruv.vitruvAdapter.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tools.vitruv.vitruvAdapter.vitruv.api.DisplayView
import tools.vitruv.vitruvAdapter.vitruv.api.Selector
import tools.vitruv.vitruvAdapter.vitruv.api.Window
import tools.vitruv.vitruvAdapter.vitruv.impl.DisplayViewRepository
import java.util.*

/**
 * This service handles all Vitruvius Interaction. It uses the RemoteVitruviusClient to make requests to a
 * RemoteVitruviusServer
 *
 */
@Service
class VitruviusService {
    @Autowired
    lateinit var displayViewRepository: DisplayViewRepository

    fun getDisplayViews(projectId: UUID): List<DisplayView> {
        TODO("Not yet implemented")
    }

    fun getDisplayViewWindows(
        projectId: UUID,
        displayViewName: String,
    ): List<Window> {
        TODO("Not yet implemented")
    }

    fun getDisplayViewContent(
        projectId: UUID,
        displayViewName: String,
        selector: Selector,
    ): String {
        TODO("Not yet implemented")
    }

    fun editDisplayViewContent(
        projectId: UUID,
        displayViewName: String,
        updatedContent: String,
    ) {
        TODO("Not yet implemented")
    }
}

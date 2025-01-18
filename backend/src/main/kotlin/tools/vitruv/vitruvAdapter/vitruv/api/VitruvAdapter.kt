package tools.vitruv.vitruvAdapter.vitruv.api

import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl
import org.springframework.stereotype.Service
import tools.vitruv.framework.views.View
import tools.vitruv.framework.views.changederivation.DefaultStateBasedChangeResolutionStrategy
import org.eclipse.emf.common.util.URI
import tools.vitruv.framework.remote.client.VitruvClient
import tools.vitruv.framework.remote.client.VitruvClientFactory
import tools.vitruv.framework.views.ViewSelector
import tools.vitruv.framework.views.ViewType
import tools.vitruv.vitruvAdapter.vitruv.impl.exception.DisplayViewException
import java.util.Collections
import java.util.*

@Service
class VitruvAdapter {

    private var vitruvClient: VitruvClient? = null

    private var displayViewContainer: DisplayViewContainer? = null

    fun setConnection(protocol: String, host: String, port: Int, tmp: Path) {
        vitruvClient = VitruvClientFactory.create(protocol, host, port, tmp)
    }

    fun setDisplayViewContainer(displayViewContainer: DisplayViewContainer) {
        this.displayViewContainer = displayViewContainer
    }


    private val filePath = "newView.xmi"

    /**
     * Returns all available DisplayViews.
     * @return The available DisplayViews.
     */
    fun getDisplayViews(): Set<DisplayView> = displayViewContainer.getDisplayViews()

    /**
     * gets all windows that are available for a given DisplayView.
     * @param displayView The DisplayView to get the windows for.
     * @return The windows that are available for the given DisplayView.
     */
    fun getWindows(displayView : DisplayView): Set<String> {
        val internalSelector = getViewType(displayView).createSelector(null)
        displayView.windowSelector.applySelection(internalSelector)
        return displayView.viewMapper.mapViewToWindows(internalSelector.createView().rootObjects.toList())
    }

    /**
     * Creates the content for the given windows.
     * @param windows The windows to create the content for.
     * @return The created View for the windows.
     */
    private fun getViewForWindows(displayView: DisplayView, windows: Set<String>): View {
        val internalSelector = getViewType(displayView).createSelector(null)
        displayView.contentSelector.applySelection(internalSelector)
        // Now only the things needed to create content for the windows should be in the selection
        return internalSelector.createView()
    }

    /**
     * Creates the content for the given windows.
     * @param windows The windows to create the content for.
     * @return The created content for each window.
     */
    fun createWindowContent(displayView: DisplayView, windows: Set<String>): DisplayContent =
        displayView.viewMapper.mapViewToJson(getViewForWindows(displayView, windows).rootObjects.toList())

    /**
     * This method reverts the json that Theia can interpret to display views to EObjects and tries to
     * apply the changes to the model by state changed derivation strategy.
     * @param displayView The DisplayView to edit.
     * @param json The json to edit the DisplayView with.
     */
    fun editDisplayView(displayView: DisplayView, json: String) {
        val newViewContent = displayView.viewMapper.mapJsonToView(json)
        val oldViewContent = getViewForWindows(displayView, getWindows(displayView))
        val newViewResource = convertEObjectsToResource(newViewContent, filePath)
        val oldViewResource = convertViewToResource(oldViewContent, filePath)
        val strategy = DefaultStateBasedChangeResolutionStrategy().getChangeSequenceBetween(newViewResource, oldViewResource)
        val view = oldViewContent.withChangeDerivingTrait(DefaultStateBasedChangeResolutionStrategy())
        view.commitChanges()
    }

    private fun getViewType(displayView: DisplayView): ViewType<out ViewSelector> {
        val viewType = vitruvClient.viewTypes.find { it.name == displayView.viewTypeName }
        if ( viewType == null ) {
            throw DisplayViewException("View type ${displayView.viewTypeName} not found on model server.")
        }
        return viewType
    }

    private fun convertEObjectsToResource(eObjects: List<EObject>, filePath: String,
                                          save: Boolean = true): Resource {
        val resourceSet: ResourceSet = ResourceSetImpl()

        // Register the XMI factory for the .xmi extension (if not already registered).
        resourceSet.resourceFactoryRegistry.extensionToFactoryMap["xmi"] = XMIResourceFactoryImpl()

        // 2. Create a Resource with a URI pointing to the specified file path.
        val uri = URI.createFileURI(filePath)
        val resource = resourceSet.createResource(uri)

        // 3. Add the EObjects to the resource’s contents.
        resource.contents.addAll(eObjects)

        // 4. (Optional) save the Resource to disk.
        if (save) {
            resource.save(Collections.emptyMap<Any, Any>())
        }

        return resource
    }
    private fun convertViewToResource(view: View, filePath: String, save: Boolean = true): Resource? {
        // 1) Ensure the view is not closed or outdated before proceeding.
        //    (Depending on your use-case, you might throw exceptions or handle otherwise.)
        if (view.isClosed) {
            throw IllegalStateException("Cannot create a Resource from a closed View.")
        }
        if (view.isOutdated) {
            println("Warning: The View is outdated. Proceeding might not reflect latest changes.")
        }

        // 2) Get the root objects from the view
        val rootObjects = view.rootObjects
        if (rootObjects.isEmpty()) {
            println("No root objects found in the View.")
            return null
        }

        // 3) Create a ResourceSet and register an XMI factory (if needed)
        val resourceSet: ResourceSet = ResourceSetImpl()
        resourceSet.resourceFactoryRegistry.extensionToFactoryMap["xmi"] = XMIResourceFactoryImpl()

        // 4) Create the Resource at the desired URI
        val uri = URI.createFileURI(filePath)
        val resource = resourceSet.createResource(uri)

        // 5) Add the View's root objects to the Resource
        resource.contents.addAll(rootObjects)

        // 6) Optionally save to disk
        if (save) {
            resource.save(Collections.emptyMap<Any, Any>())
            println("Resource saved at: $filePath")
        }

        return resource
    }
}
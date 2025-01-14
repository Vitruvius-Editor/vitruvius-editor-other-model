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
import java.util.Collections
import java.util.*

@Service
class VitruvAdapter {


    private val filePath = "newView.xmi"

    fun getWindows(displayView : DisplayView): Set<String> {
        val internalSelector = displayView.getViewType().createSelector(null)
        displayView.windowSelector.applySelection(internalSelector)
        // Now only the names of the windows should be in the selection
        val view = internalSelector.createView()
        val windows = mutableSetOf<String>()
        for (rootObject in view.rootObjects) {
            // Add all names of windows to windows set
        }
        return windows
    }


    /**
     * Gets all windows that are available for this view.
     * @see Window
     * @return The windows that are available for this view.
     */


    private fun getViewForWindows(displayView: DisplayView, windows: Set<String>): View {
        val internalSelector = displayView.getViewType().createSelector(null)
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
     * @param json the json String
     */
    fun editDisplayView(displayView: DisplayView, json: String) {
        val newViewContent = displayView.viewMapper.mapJsonToView(json)
        val oldViewContent = getViewForWindows(displayView, getWindows(displayView))
        val newViewResource = convertEObjectsToResource(newViewContent, filePath)
        val oldViewResource = convertViewToResource(oldViewContent, filePath)
        val strategy = DefaultStateBasedChangeResolutionStrategy().getChangeSequenceBetween(newViewResource, oldViewResource)
        // apply change to model

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
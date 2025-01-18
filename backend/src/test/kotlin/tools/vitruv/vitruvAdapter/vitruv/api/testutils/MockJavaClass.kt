package tools.vitruv.vitruvAdapter.vitruv.api.testutils

import org.eclipse.emf.common.notify.Adapter
import org.eclipse.emf.common.notify.Notification
import org.eclipse.emf.common.util.BasicEList
import org.eclipse.emf.common.util.EList
import org.eclipse.emf.common.util.TreeIterator
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EOperation
import org.eclipse.emf.ecore.resource.Resource

/**
 * Minimal mock implementation of an EObject representing a Java class.
 */
class MockJavaClass(val className: String) : EObject {

    // ---------------------------------------------------------------------------------------------
    //  Basic EObject Methods (Return stubs or “empty” data to compile and run in tests)
    // ---------------------------------------------------------------------------------------------

    override fun eClass(): EClass? = null
    override fun eResource(): Resource? = null
    override fun eContainer(): EObject? = null
    override fun eContainingFeature() = null
    override fun eContainmentFeature() = null
    override fun eIsProxy(): Boolean = false

    override fun eContents(): EList<EObject> = BasicEList()  // or mutableListOf()
    override fun eAllContents(): TreeIterator<EObject> {
        TODO("Not yet implemented")
    }

    override fun eCrossReferences(): EList<EObject> = BasicEList()

    // Typically you'd return or set real feature data. Here we stub them out:
    override fun eGet(feature: org.eclipse.emf.ecore.EStructuralFeature): Any? = null
    override fun eGet(feature: org.eclipse.emf.ecore.EStructuralFeature, resolve: Boolean): Any? = null
    override fun eSet(feature: org.eclipse.emf.ecore.EStructuralFeature, newValue: Any?) { /* no-op */ }
    override fun eIsSet(feature: org.eclipse.emf.ecore.EStructuralFeature): Boolean = false
    override fun eUnset(feature: org.eclipse.emf.ecore.EStructuralFeature) { /* no-op */ }
    override fun eInvoke(p0: EOperation?, p1: EList<*>?): Any {
        TODO("Not yet implemented")
    }

    // ---------------------------------------------------------------------------------------------
    //  Notification & Delivery
    // ---------------------------------------------------------------------------------------------

    override fun eAdapters(): EList<Adapter> = BasicEList()
    override fun eDeliver(): Boolean = false
    override fun eSetDeliver(deliver: Boolean) { /* no-op */ }
    override fun eNotify(notification: Notification?) { /* no-op */ }

    // ---------------------------------------------------------------------------------------------
    //  Operations (eInvoke)
    // ---------------------------------------------------------------------------------------------


    // If you have the older signature with EOperation? p0, EList<*>? p1:
    // override fun eInvoke(p0: EOperation?, p1: EList<*>?): Any? = null

    // ---------------------------------------------------------------------------------------------
    //  toString() for easier debugging
    // ---------------------------------------------------------------------------------------------

    override fun toString(): String = "MockJavaClass($className)"
}
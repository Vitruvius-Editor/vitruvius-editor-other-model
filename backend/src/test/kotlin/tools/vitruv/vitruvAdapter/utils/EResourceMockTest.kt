package tools.vitruv.vitruvAdapter.utils

import org.eclipse.uml2.uml.UMLFactory
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class EResourceMockTest {

    @Test
    fun testMockERessourceForEObjects() {
        val class1 = UMLFactory.eINSTANCE.createClass()
        class1.name = "Class1"
        val class2 = UMLFactory.eINSTANCE.createClass()
        class2.name = "Class2"
        val eObjects = listOf(class1, class2)
        val mockedEObjects = EResourceMock.mockERessourceForEObjects(eObjects)
        assertEquals(EResourceMock.getFakeUUID(class1), mockedEObjects[0].eResource().getURIFragment(class1))
        assertEquals(EResourceMock.getFakeUUID(class2), mockedEObjects[1].eResource().getURIFragment(class2))
    }

    @Test
    fun testMockERessourceForEObject() {
        val class1 = UMLFactory.eINSTANCE.createClass()
        class1.name = "Class1"
        val class1Mocked = EResourceMock.mockERessourceForEObject(class1)
        assertEquals(EResourceMock.getFakeUUID(class1), class1Mocked.eResource().getURIFragment(class1))
    }
}
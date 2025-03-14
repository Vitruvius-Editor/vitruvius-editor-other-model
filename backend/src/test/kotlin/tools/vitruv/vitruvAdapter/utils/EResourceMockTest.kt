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
        val mockedEObjects = EResourceMock.mockERessourceAndUuidForEObjects(eObjects)
        assertEquals(EResourceMock.getFakeUUID(mockedEObjects[0]), mockedEObjects[1].eResource().getURIFragment(mockedEObjects[0]))
        assertEquals(EResourceMock.getFakeUUID(mockedEObjects[1]), mockedEObjects[1].eResource().getURIFragment(mockedEObjects[1]))
    }

    @Test
    fun testMockERessourceForEObject() {
        val class1 = UMLFactory.eINSTANCE.createClass()
        class1.name = "Class1"
        val class1Mocked = EResourceMock.mockERessourceAndUuidForEObject(class1)
        assertEquals(EResourceMock.getFakeUUID(class1Mocked), class1Mocked.eResource().getURIFragment(class1Mocked))
    }

    @Test
    fun testMockERessourceAndUUIDForEObject() {
        val umlPackage = EResourceMock.mockERessourceForEObject(UMLFactory.eINSTANCE.createPackage())
        val class1 = UMLFactory.eINSTANCE.createClass()
        class1.name = "Class1"
        val class1Mocked = EResourceMock.mockERessourceAndUuidForEObject(class1, umlPackage)
        assertEquals(EResourceMock.getFakeUUID(class1Mocked), umlPackage.eResource().getURIFragment(class1Mocked))
    }
}

package tools.vitruv.vitruvAdapter.utils

import org.eclipse.uml2.uml.UMLFactory
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class EResourceMockTest {

    @Test
    fun testMockERessourceForEObjects() {
        val class1 = UMLFactory.eINSTANCE.createClass()
        class1.name = "Class1"
        println(class1)
        val class2 = UMLFactory.eINSTANCE.createClass()
        class2.name = "Class2"
        val eObjects = listOf(class1, class2)
        val mockedEObjects = EResourceMock.mockERessourceForEObjects(eObjects)
        assertEquals(EResourceMock.getFakeUUID(mockedEObjects[0]), mockedEObjects[0].eResource().getURIFragment(mockedEObjects[0]))
        assertEquals(EResourceMock.getFakeUUID(mockedEObjects[1]), mockedEObjects[1].eResource().getURIFragment(mockedEObjects[1]))

    }
}
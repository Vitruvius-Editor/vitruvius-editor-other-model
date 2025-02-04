package tools.vitruv.vitruvAdapter.core.impl.displayContentMapper

import tools.vitruv.vitruvAdapter.core.impl.uml.*
import kotlin.test.assertEquals

class UmlDisplayContentMapperTest {
 @org.junit.jupiter.api.Test
 fun testMap() {
  // Create an instance of the mapper for ClassTableEntry using the factory method
  val umlContentMapper = UmlDisplayContentMapper()


  val attribute = UmlAttribute(
   UmlVisibility.PUBLIC,
   "attribute1",
   "String"
  )

  val methodParameters = listOf(
    UmlParameter(
     "attribute1",
     "String"
    ),
    UmlParameter(
     "attribute2",
     "Object"
    )
  )

  val method = UmlMethod(
   UmlVisibility.PUBLIC,
   "method1",
   listOf(),
   "String"
  )

  val attribute2 = UmlAttribute(
   UmlVisibility.PUBLIC,
   "attribute2",
   "String"
  )
  val umlNodes = listOf(
   UmlNode(
    "1",
    "Class1",
    "Class",
    listOf(attribute),
    listOf(method)
   ),
    UmlNode(
     "2",
     "Class2",
     "Class",
     listOf(attribute2),
     listOf(UmlMethod(
      UmlVisibility.PUBLIC,
      "method2",
      methodParameters,
      "Object"
     ))
    )
  )

  val umlConnections = listOf(
   UmlConnection(
    "1",
    "1",
    "2",
    UmlConnectionType.EXTENDS,
    "",
    "",
    ""
   )
  )

  val umlDiagram = UmlDiagram(
    umlNodes,
   umlConnections
  )

    val displayContent = umlContentMapper.parseContent(umlDiagram)
  println(displayContent)


  val umlDiagramRemapped = umlContentMapper.parseString(displayContent)

  val displayContentMappedAgain = umlContentMapper.parseContent(umlDiagramRemapped)

  assertEquals(displayContent, displayContentMappedAgain)


 }
}
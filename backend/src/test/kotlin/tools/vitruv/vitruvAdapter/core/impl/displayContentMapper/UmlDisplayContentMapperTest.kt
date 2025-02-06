package tools.vitruv.vitruvAdapter.core.impl.displayContentMapper

import tools.vitruv.vitruvAdapter.core.impl.uml.*
import kotlin.test.assertEquals

class UmlDisplayContentMapperTest {
 @org.junit.jupiter.api.Test
 fun testMap() {
  // Create an instance of the mapper for ClassTableEntry using the factory method
  val umlContentMapper = UmlDisplayContentMapper()


  val attribute = UmlAttribute(
   "",
   UmlVisibility.PUBLIC,
   "attribute1",
   UmlType(
    "",
    "Int"
   )
  )

  val methodParameters = listOf(
    UmlParameter(
     "",
     "attribute1",
     UmlType(
      "",
      "Int"
     )
    ),
    UmlParameter(
     "",
     "attribute2",
        UmlType(
        "",
        "String"
        )
    )
  )

  val method = UmlMethod(
   "",
   UmlVisibility.PUBLIC,
   "method1",
   listOf(),
   UmlType(
    "",
    "Object"
   )
  )

  val attribute2 = UmlAttribute(
   "",
   UmlVisibility.PUBLIC,
   "attribute2",
   UmlType(
    "",
    "String"
   )
  )
  val umlNodes = listOf(
   UmlNode(
    "1",
    "Class1",
    "Class",
    listOf(attribute),
    listOf(method),
    listOf()
   ),
    UmlNode(
     "2",

     "Class2",
     "Class",
     listOf(attribute2),
     listOf(UmlMethod(
      "",
      UmlVisibility.PUBLIC,
      "method2",
      methodParameters,
      UmlType(
       "",
       "String"
      ),
     )),
     listOf()
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
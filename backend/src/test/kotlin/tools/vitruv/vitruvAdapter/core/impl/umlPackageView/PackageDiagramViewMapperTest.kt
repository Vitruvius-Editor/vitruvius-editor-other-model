package tools.vitruv.vitruvAdapter.core.impl.umlPackageView

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PackageDiagramViewMapperTest {

 val packageDiagramViewMapper = PackageDiagramViewMapper()


 @Test
 fun testMapPackageToDiagram() {
     val packageDiagramViewMapper = PackageDiagramViewMapper()
     val
     val diagram = packageDiagramViewMapper.map(package)
     assertEquals(1, diagram.nodes.size)
     assertEquals(0, diagram.connections.size)
     assertEquals(package, diagram.nodes[0].umlType)
 }
}
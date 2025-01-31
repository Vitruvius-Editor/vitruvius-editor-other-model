import { DefaultNodeModel } from '@projectstorm/react-diagrams-defaults';

// export class PackageNode extends DefaultNodeModel {
//   constructor(className: string, attributes: string[], methods: string[]) {
//       super({
//           name: (
//               <>
//                 Class Name <br />
//                 <hr width="100%" size="2" color="black" noshade></hr>
//                 +attribute1: type <br />
//                 -attribute2: type <br />
//                 <hr width="100%" size="2" color="black" noshade></hr>
//                 +method1: void <br />
//                 -method2: type <br />
//               </>
//             ),
//           color: 'rgb(145, 145, 145)'})
//   }
// }

export class PackageNode extends DefaultNodeModel {
    constructor(className: string, attributes: string[], methods: string[]) {
        super({
            name: ( 
              "a"
              ),
            color: 'rgb(145, 145, 145)'})
    }
}
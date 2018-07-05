package generator;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import microserviceMetamodel.AnotherMicroserviceMetamodelPackage;
import microserviceMetamodel.impl.MetaModelStructureImpl;
import microserviceMetamodel.xtend.EnvironmentGenerator;

public class GenerateEnvironment {
	public static void main(String[] args) throws IOException {
		EPackage[] epackage = new EPackage[] { AnotherMicroserviceMetamodelPackage.eINSTANCE };

		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
		
		
		Resource resource = resourceSet.getResource(URI.createFileURI(new File("model/ChaosPaper.xmi").getAbsolutePath()), true);
		resourceSet.getPackageRegistry().put(epackage[0].getNsURI(), AnotherMicroserviceMetamodelPackage.eINSTANCE);
		resource.load(null);
		MetaModelStructureImpl mmsi = (MetaModelStructureImpl) resource.getContents().get(0);
		new EnvironmentGenerator(new File("env-chaos"), mmsi);
	}
}

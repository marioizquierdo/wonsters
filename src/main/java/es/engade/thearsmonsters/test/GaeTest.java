package es.engade.thearsmonsters.test;

import java.io.File;

import org.junit.After;
import org.junit.Before;

import com.google.appengine.tools.development.ApiProxyLocalImpl;
import com.google.apphosting.api.ApiProxy;

/**
 * Superclass de los tests thearsmonsters.
 * Cualquier test que pretenda ejecutarse en GAE debe extender esta clase, que inicializa correctamente el entorno de pruebas.
 * La clase extendida se usa igual que cualquier otro test de JUnit4, pudiendo usar @BeforeClass, @Before, @After, @AfterClass, @Test, etc..
 * 
 * NOTA: JARS necesarios para poder ejecutar los test en el entorno GAE:
 * Para ejecutar las pruebas junit es necesario importar otros dos jars, que en teoría deberían estar incluidos en la librería de Google App Engine 
 * (que pasará aqui??) y sin embargo el eclipse dice que no están.
 * Por lo tanto hay que darle otra vez a "Add JARs" y añadir los siguientes jars que están dentro de la carpeta donde se instaló el eclipse, en plugins:
 *    eclipse/plugins/com.google.appengine.eclipse.sdkbundle_<version>/appengine-java-sdk-<version>/lib/impl/
 *        * appengine-api-stubs.jar
 *        * appengine-local-runtime.jar
 */
public class GaeTest {
	
	String pepe = "hola";

	@Before
    public final void setEnvironmentForCurrentThread() throws Exception {
        ApiProxy.setEnvironmentForCurrentThread(new TestEnvironment());
        ApiProxy.setDelegate(new ApiProxyLocalImpl(new File(".")){});
    }

	@After
    public final void unsetEnvironmentForCurrentThread() throws Exception {
        // not strictly necessary to null these out but there's no harm either
        ApiProxy.setDelegate(null);
        ApiProxy.setEnvironmentForCurrentThread(null);
    }

}
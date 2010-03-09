package es.engade.thearsmonsters.test;

import org.junit.After;
import org.junit.Before;

import com.google.appengine.api.datastore.dev.LocalDatastoreService;
import com.google.appengine.tools.development.ApiProxyLocal;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;
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
	
    private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalTaskQueueTestConfig());

	@Before
    public final void setEnvironmentForCurrentThread() throws Exception {
	    helper.setUp();
        ApiProxy.setEnvironmentForCurrentThread(new TestEnvironment());
        ApiProxy.setDelegate(LocalServiceTestHelper.getApiProxyLocal());
        ApiProxyLocal proxy = (ApiProxyLocal) ApiProxy.getDelegate();
        proxy.setProperty(LocalDatastoreService.NO_STORAGE_PROPERTY, Boolean.TRUE.toString());
    }

	@After
    public final void unsetEnvironmentForCurrentThread() throws Exception {
	    ApiProxyLocal proxy = (ApiProxyLocal) ApiProxy.getDelegate();
        LocalDatastoreService datastoreService = (LocalDatastoreService) proxy.getService("datastore_v3");
        datastoreService.clearProfiles();
        helper.tearDown();
    }

}
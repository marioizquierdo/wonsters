package es.engade.thearsmonsters.util.configuration;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AppContext {

    private static final String SPRING_CONFIG_FILE = "applicationContext.xml";
    
    private static AppContext instance;
    private ClassPathXmlApplicationContext appContext;
    
    public ClassPathXmlApplicationContext getAppContext() {
        return appContext;
    }

    private AppContext() {
        appContext = new ClassPathXmlApplicationContext(
                new String[] {SPRING_CONFIG_FILE});
    }
    
    public static AppContext getInstance() {
        if (instance == null) {
            instance = new AppContext();
        }
        return instance;
    }
}

package catalog;

import java.io.IOException;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;


public class VcapProcessor implements ApplicationContextInitializer<ConfigurableApplicationContext> {
	
  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    try {
        System.out.println("In VcapProcessor initialize");
        String[] activeProfiles = applicationContext.getEnvironment().getActiveProfiles();
        
        Resource resource = null;
        if (activeProfiles != null && activeProfiles.length > 0) {
        		for (String profile : activeProfiles) {
        	        resource = applicationContext.getResource(String.format("classpath:application-%s.yml", profile));
        	        if (resource.exists()) break;
        		}
        }
        if (resource == null || !resource.exists()) {
        		resource = applicationContext.getResource("classpath:application.yml");
        }
        YamlPropertySourceLoader sourceLoader = new YamlPropertySourceLoader();
        PropertySource<?> yamlTestProperties = sourceLoader.load("yamlTestProperties", resource, null);
        applicationContext.getEnvironment().getPropertySources().addFirst(yamlTestProperties);
        String vcapURI = applicationContext.getEnvironment().getProperty("vcap.uri");
        System.out.println("TEST: "+vcapURI);
        if (vcapURI.startsWith("mysql")) {
            int ustart = vcapURI.indexOf("://")+3;
            String username = vcapURI.substring(ustart,vcapURI.indexOf(":",ustart));
            String password = vcapURI.substring(vcapURI.indexOf(":",ustart)+1,vcapURI.indexOf("@"));
            String uri = "jdbc:mysql://"+vcapURI.substring(vcapURI.indexOf("@")+1,vcapURI.indexOf("/",ustart))+"/inventorydb";
            String ymlSrc = "spring: \n  datasource:\n    username: "+username+"\n    password: "+password+"\n    url: "+uri;
            System.out.println(ymlSrc);
            ByteArrayResource bar = new ByteArrayResource(ymlSrc.getBytes());
            PropertySource<?> newDataSource = sourceLoader.load("newDataSource",bar,null);
            applicationContext.getEnvironment().getPropertySources().addFirst(newDataSource);
            System.out.println("****: "+applicationContext.getEnvironment().getProperty("spring.datasource.url"));
        }
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
  }
}

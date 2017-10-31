package catalog;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@EnableWebMvc
@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration(initializers=VcapProcessor.class, loader=AnnotationConfigWebContextLoader.class)
public class VcapProcessorTest {
	
	@Autowired
	private Environment env;
	
	@Test
	public void canary() {
		assertThat(true, is(true));
	}
	
	@Test
	public void databaseProperties() {
		assertThat(env.getProperty("spring.datasource.username"), is(equalTo("test")));
		assertThat(env.getProperty("spring.datasource.password"), is(equalTo("password")));
		assertThat(env.getProperty("spring.datasource.url"), is(equalTo("jdbc:mysql://mydbhost/inventorydb")));
	}

	@Configuration
	public static class Config {
	}
}

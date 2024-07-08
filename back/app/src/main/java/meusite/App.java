

package meusite;
import meusite.service.auth.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"meusite"})
public class App {

        private static final Logger LOG = LoggerFactory.getLogger(App.class);

        public static void main(String[] args) {

            LOG.info("aplicacao começou ");

            SpringApplication.run(App.class, args);

        }
}

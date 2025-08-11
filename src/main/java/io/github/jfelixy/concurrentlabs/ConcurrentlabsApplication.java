package io.github.jfelixy.concurrentlabs;

import io.github.joaofxs.fake_requisitions.config.FakeRequisitionsAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(FakeRequisitionsAutoConfiguration.class)
public class ConcurrentlabsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConcurrentlabsApplication.class, args);
	}

}

package br.com.postech.techchallange_admin;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import br.com.postech.techchallange_admin.domain.port.out.AdminRepositoryPort;
import br.com.postech.techchallange_admin.domain.port.out.AdminLogAcaoRepositoryPort;

@SpringBootTest
@TestPropertySource(properties = {
	"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration"
})
class TechchallangeAdminApplicationTests {

	@MockBean
	private AdminRepositoryPort adminRepositoryPort;

	@MockBean
	private AdminLogAcaoRepositoryPort adminLogAcaoRepositoryPort;

	@Test
	void contextLoads() {
	}

}

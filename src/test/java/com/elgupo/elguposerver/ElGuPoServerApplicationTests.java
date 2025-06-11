package com.elgupo.elguposerver;

import com.elgupo.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = TestConfig.class)
@ActiveProfiles("test")
class ElGuPoServerApplicationTests {

	@Test
	void contextLoads() {
	}

}

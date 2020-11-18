package com.qa.springust;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SpringustApplicationTests {

    @SuppressWarnings("static-access")
    @Test
    void contextLoads() {
        SpringustApplication app = new SpringustApplication();
        String[] args = {};
        app.main(args);
        assertThat(app).isInstanceOf(SpringustApplication.class);
    }

}

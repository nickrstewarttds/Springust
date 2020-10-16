package com.qa.springust;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SpringustApplicationTests {

//    private static ExtentReports report;
//    private ExtentTest test;
//
//    @BeforeAll
//    void init() {
//        Path root = FileSystems.getDefault().getPath("").toAbsolutePath();
//        Path filePath = Paths.get(root.toString(), "target\\reports\\extentreport.html");
//
//        System.out.println(filePath.toString());
//
//        report = new ExtentReports(filePath.toString(), true);
//    }

    @SuppressWarnings("static-access")
    @Test
    void contextLoads() {
        SpringustApplication app = new SpringustApplication();
        String[] args = {};
        app.main(args);
        assertThat(app).isInstanceOf(SpringustApplication.class);
    }

}

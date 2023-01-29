package com.coder.desgin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 需要重新指定包扫描的范围
 * @author coder
 */
@SpringBootApplication(scanBasePackages = {"com.coder.common", "com.coder.desgin"})
public class DesginApplication {

    public static void main(String[] args) {
        SpringApplication.run(DesginApplication.class, args);
    }

}

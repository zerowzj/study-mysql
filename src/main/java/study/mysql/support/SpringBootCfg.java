package study.mysql.support;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = "study.mysql.mapper")
@SpringBootApplication(scanBasePackages = {"study.mysql"})
public class SpringBootCfg {

}

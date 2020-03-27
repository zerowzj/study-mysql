package study.mysql.support;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = "study.springboot.dao.mapper")
@SpringBootApplication(scanBasePackages = {
        "study.springboot.dao.mapper",
        "study.springboot.dao.service"})
public class SpringBootCfg {

}

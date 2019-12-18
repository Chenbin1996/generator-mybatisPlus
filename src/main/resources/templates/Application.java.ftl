package ${package.packAge};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.mybatis.spring.annotation.MapperScan;

/**
 * 启动类
 *
 * @author ${author} on ${date}
 */
@SpringBootApplication
@ComponentScan(basePackages={"${package.packAge}"})
@MapperScan(basePackages = {"${package.mapper}.**"})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
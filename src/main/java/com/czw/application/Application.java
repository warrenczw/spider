package com.czw.application;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * @author 崔志伟
 * 联系方式：493067123@qq.com
 * 创建日期：2018年07月12日
 * www.cuizhiwei.com
 */
//springBoot应用标识
@SpringBootApplication
public class Application {
	
	/*@Bean    
	public SessionFactory sessionFactory(HibernateEntityManagerFactory hemf){    
	    return hemf.getSessionFactory();    
	} */   
	
	//程序启动入口
	public static void main(String[] args) throws Exception {
		Properties properties = new Properties();
		InputStream in = Application.class.getResourceAsStream("/application.properties");
		properties.load(in);
		SpringApplication app = new SpringApplication(Application.class);
		app.setDefaultProperties(properties);
		app.run(args);
    }
}

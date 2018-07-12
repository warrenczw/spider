package com.czw;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * @author 崔志伟
 * 联系方式：493067123@qq.com
 * 创建日期：2018年07月12日
 * www.cuizhiwei.com
 */
public class TomcatStartApplication extends SpringBootServletInitializer{

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(Application.class);
	}
}

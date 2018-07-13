package com.czw.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.czw.spider.util.ToutiaoRss;

/**
 * 创建者:崔志伟
 * emai：493067123@qq.com
 * 日期：2018年7月12日
 */
@Controller
@RequestMapping("/api/spider")
public class SpiderController {
	
	private static Logger logger = Logger.getLogger(SpiderController.class);
	
	@RequestMapping("/toutiao")
	@ResponseBody
	public Object alipay(HttpServletRequest request, 
    		HttpServletResponse response,
    		@RequestParam(value="key",required=false,defaultValue = "") String key,
    		@RequestParam(value="startno",required=false,defaultValue = "0") int startno,
    		@RequestParam(value="num",required=false,defaultValue = "10") int num){
		JSONObject result = new JSONObject();
		JSONArray resultform = ToutiaoRss.grabToutiao(key, startno, num);
		result.put("count", resultform.size());
		result.put("data", resultform);
		return result;
	}

}

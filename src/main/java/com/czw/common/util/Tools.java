package com.czw.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


public class Tools extends JavaTools{
	
	
	public static final String[] mobileAgents = { "iphone", "android","ipad", "phone", "mobile", "wap", "netfront", 
			"java", "opera mobi", "ipod","mqqbrowser","windows phone",
            "opera mini", "ucweb", "windows ce", "symbian", "series", "webos", "sony", "blackberry", "dopod",  
            "nokia", "samsung", "palmsource", "xda", "pieplus", "meizu", "midp", "cldc", "motorola", "foma",  
            "docomo", "up.browser", "up.link", "blazer", "helio", "hosin", "huawei", "novarra", "coolpad", "webos",  
            "techfaith", "palmsource", "alcatel", "amoi", "ktouch", "nexian", "ericsson", "philips", "sagem",  
            "wellcom", "bunjalloo", "maui", "smartphone", "iemobile", "spice", "bird", "zte-", "longcos",  
            "pantech", "gionee", "portalmmm", "jig browser", "hiptop", "benq", "haier", "^lct", "320x320",  
            "240x320", "176x220", "w3c ", "acs-", "alav", "alca", "amoi", "audi", "avan", "benq", "bird", "blac",  
            "blaz", "brew", "cell", "cldc", "cmd-", "dang", "doco", "eric", "hipt", "inno", "ipaq", "java", "jigs",  
            "kddi", "keji", "leno", "lg-c", "lg-d", "lg-g", "lge-", "maui", "maxo", "midp", "mits", "mmef", "mobi",  
            "mot-", "moto", "mwbp", "nec-", "newt", "noki", "oper", "palm", "pana", "pant", "phil", "play", "port",  
            "prox", "qwap", "sage", "sams", "sany", "sch-", "sec-", "send", "seri", "sgh-", "shar", "sie-", "siem",  
            "smal", "smar", "sony", "sph-", "symb", "t-mo", "teli", "tim-", "tosh", "tsm-", "upg1", "upsi", "vk-v",  
            "voda", "wap-", "wapa", "wapi", "wapp", "wapr", "webc", "winw", "winw", "xda", "xda-",  
            "googlebot-mobile" }; 

	public static String logHead(HttpServletRequest request)
	{
		long stime=Tools.toLong(request.getAttribute("_req_start_time"),System.currentTimeMillis());
		return Tools.logString(System.currentTimeMillis() - stime
				, request.getMethod()
				, request.getServerName()
				, getRemoteAddr(request)
		        , Tools.removeAll(request.getRequestURI(), "|")
		        , Tools.removeAll(request.getQueryString(), "|")
		         , Tools.objectToJsonString(request.getParameterMap())
		        );
	}
	
	/**
	 * 判断请求是否来自移动端
	 * @param request
	 * @return
	 */
	public static boolean isMobileBrowser(HttpServletRequest request){
		boolean isMobile = false;
		 if (request.getHeader("User-Agent") != null) {  
	            String agent=request.getHeader("User-Agent");  
	            for (String mobileAgent : mobileAgents) {  
	                if (agent.toLowerCase().indexOf(mobileAgent) >= 0 && agent.toLowerCase().indexOf("windows nt")<=0 && agent.toLowerCase().indexOf("macintosh")<=0) {  
	                	isMobile = true;  
	                    break;  
	                }  
	            }  
	     }  
	    return isMobile;  
	}
	
	/**
	 * 获取域名ip
	 * @param request
	 * @return
	 */
	public static String getRemoteAddr(HttpServletRequest request) {
		if(Tools.isIpString(request.getHeader("X-Forwarded-For")))
			return request.getHeader("X-Forwarded-For").trim();
		return request.getRemoteAddr();
	}
	
	
	
	/**
	 * 判断是否是ajax请求
	 * @param request
	 * @return
	 */
	public static boolean isAjaxRequest(HttpServletRequest request){
		String header = request.getHeader("X-Requested-With");  
		boolean isAjax = "XMLHttpRequest".equals(header) ? true:false;  
		return isAjax;  
	}
	
	/**
	 * 将map按照key值字典顺序排列,返回字符串
	 * @param params
	 * @return
	 */
	public static String sortMap(Map<String, String> params){
		 StringBuffer content = new StringBuffer();
	        List<String> keys = new ArrayList<String>(params.keySet());
	        Collections.sort(keys);
	        for (int i = 0; i < keys.size(); i++) {
	            String key = keys.get(i);
	            String value = params.get(key);
	            content.append((i == 0 ? "" : "&") + key + "=" + value);
	        }
	        return content.toString();
	}
	
	/**
	 * 获取签名sign
	 * @param request
	 * @param signKey
	 * @return
	 */
	public static String getSign(HttpServletRequest request,String signKey){
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
				try {
					valueStr = URLDecoder.decode(valueStr, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			params.put(name, valueStr);
		}
		params.remove("sign");
		String mapStr = sortMap(params);
		String enc = mapStr + signKey;
		return Md5String.md5(enc);
	}
	
	/**
	 * 签名是否验证通过
	 * @param request
	 * @param signKey
	 * @return
	 */
	public static boolean isSignPass(HttpServletRequest request,String signKey){
		String sign = getSign(request, signKey);
		String outSign = request.getParameter("sign");
		return sign.equals(outSign);
	}
}

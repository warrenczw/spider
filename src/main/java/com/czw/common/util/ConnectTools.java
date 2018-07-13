package com.czw.common.util;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author 崔志伟
 * 联系方式：493067123@qq.com
 * 创建日期：2018年7月13日
 * www.cuizhiwei.com
 */
public class ConnectTools {
	
	/**
	 * 模拟浏览器访问目标url（可以防反爬）
	 * @param url
	 * @return
	 */
	public static String broswerConnect(String url){
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		webClient.getOptions().setThrowExceptionOnScriptError(false);//当JS执行出错的时候是否抛出异常，不需要
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);//当HTTP的状态非200时是否抛出异常, 不需要
		webClient.getOptions().setJavaScriptEnabled(true);//启用js，以渲染出最终html页面
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setActiveXNative(false);
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());//设置支持ajax，利于页面展现
		Page htmlpg = null;
		String result = "";
		try {
			htmlpg = webClient.getPage(url);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			webClient.close();
		}
		webClient.waitForBackgroundJavaScript(20000);//异步js执行时间，设置10秒
		if(htmlpg.isHtmlPage()){
			result = ((HtmlPage) htmlpg).asXml();
		}else{
			result = htmlpg.getWebResponse().getContentAsString();
		}
		return result;
	}

	/**
	 * jsoup方式访问目标url（适合处理静态页面，操作类似jquery）
	 * @param url
	 * @return
	 */
	public static Document JsoupConnection(String url){
		Document doc = null;
		try {
			//System.out.println(url);
			doc = Jsoup.connect(url).timeout(60000).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
}

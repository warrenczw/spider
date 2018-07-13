package com.czw.spider.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.czw.common.util.ConnectTools;
import com.czw.common.util.Tools;
import com.czw.spider.pojo.Article;

public class ToutiaoRss {
	/**
	 * 抓取头条新闻相关文章
	 * @param key 关键字
	 * @param number 提取数量
	 * @return
	 */
	 public static JSONArray grabToutiao(String key,Integer startno,Integer number){
		 String keystr="";
		 try {
			 keystr = URLEncoder.encode(key,"utf-8");
		 } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		 }
		 String url = "http://www.toutiao.com/search_content/?offset="+startno+"&format=json&keyword="+keystr+"&autoload=true&count="+number+"&cur_tab=1&from=search_tab";
		 String ArticleListRes = ConnectTools.broswerConnect(url);
		 JSONArray articleArray = parseToutiaoListResult(ArticleListRes);
		 return articleArray;
	 }
	 
	 
	 
	/**
	 * 解析头条文章列表
	 * @param result
	 * @return
	 */
	 private static JSONArray parseToutiaoListResult(String result){
		 JSONArray resArray = new JSONArray();
		 JSONObject listResultJson = JSONObject.parseObject(result);
		 if(listResultJson!=null){
			 JSONArray childs= listResultJson.getJSONArray("data");
		        if(childs!=null && !childs.equals("")){
		            for (int i = 0; i < childs.size(); i++) {
		            	if(!childs.getJSONObject(i).containsKey("article_url"))
		            		continue;
		                String articleUrl = childs.getJSONObject(i).getString("article_url"); 
		                if(articleUrl.indexOf("toutiao.com/group/")==-1)
		                	continue;
		                if("true".equals(childs.getJSONObject(i).getString("has_gallery")) || "true".equals(childs.getJSONObject(i).getString("has_video")))
		                	continue;
		                if(Tools.stringIsNotNull(articleUrl)){
		                	String sequenceId = childs.getJSONObject(i).getString("id");
		                	String aurl = "http://www.toutiao.com/a"+sequenceId;
		                	Article article = new Article();
		                	article.setTitle(childs.getJSONObject(i).getString("title"));
		                	article.setCoverUrl(childs.getJSONObject(i).getString("large_image_url"));
		                	article.setDescribe(childs.getJSONObject(i).getString("abstract"));
		                	article.setSequence(sequenceId);
		                	article.setSourceUrl(childs.getJSONObject(i).getString("article_url"));
		                	parseArticle(aurl,article);
		                	resArray.add(JSONObject.toJSON(article));
		                }
		            }  
		        }
		 }
		 return resArray;
	 }
	 
	 /**
	  * 解析头条单篇文章
	  * @param articleUrl
	  * @return
	  */
	 private static void parseArticle(String articleUrl,Article article){
		 Map<String, Object> articleMap = new HashMap<String,Object>();
		 String result = ConnectTools.broswerConnect(articleUrl);
		 Document doc = Jsoup.parse(result);
		 if(doc!=null){
			Element contentElement = doc.select("div.article-content").first();
			if(contentElement!=null){
				if(contentElement.html().contains("div")){
					contentElement = doc.select("div.article-content>div").first();
				}
			}
			String content = "";
			if(contentElement!=null){
				content = contentElement.html().toString();
			}
			article.setContent(delTouTiaoNote(content));
		}
	 }
	 
    /**
	 * 处理头条声明部分
	 * @param content
	 * @return
	 */
	private static String delTouTiaoNote(String content){
		StringBuffer contentBuffer = new StringBuffer();
		String []temp = content.split("<p>");
		for(String singleStr:temp){
			if(!(singleStr.contains("@") || singleStr.contains("转载") || singleStr.contains("微信") || singleStr.contains("关注我的") || singleStr.contains("订阅")
				|| singleStr.contains("小编推荐") || singleStr.contains("toutiao") || singleStr.contains("欢迎关注")  || singleStr.contains("声明"))){
				if(singleStr!=null && !"".equals(singleStr)){
					contentBuffer.append("<p>"+singleStr);
				}else{
					contentBuffer.append(singleStr);
				}
			}
		}
		return contentBuffer.toString();
	}
}


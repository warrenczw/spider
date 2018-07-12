package com.czw.spider.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ToutiaoRss {
	
	/**
	 * 
	 * @param url
	 * @return
	 */
	private static Document getConnection(String url){
		Document doc = null;
		try {
			//System.out.println(url);
			doc = Jsoup.connect(url).timeout(60000).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	/**
	 * 页面解析
	 * @param doc
	 * @return
	 */
	private static Map<String, String> toutiaoArticle(Document doc){
		Map<String, String> articleMap = new HashMap<String,String>();
		Element listElement = doc.getElementById("pagelet-feedlist");
		return articleMap;
	}
	
	/**
	 * 爬取头条文章
	 * @param doc
	 * @return
	 */
	@SuppressWarnings("static-access")
	private static Map<String, Object> ToutiaoList(String url,String key){
		Map<String, Object> articleListMap = new HashMap<String,Object>();
		//获取文章列表json
		StringBuilder json = new StringBuilder();  
        try {  
            URL urlObject = new URL(url);  
            //System.out.println(url);
            HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("GET");
            connection.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));  
            String inputLine = null;  
            while ( (inputLine = in.readLine()) != null) {  
                json.append(inputLine);  
            }  
            in.close();  
            connection.disconnect();
        } catch (MalformedURLException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        String jsonStr = json.toString(); 
        //解析json
        JSONObject jsonObject = new JSONObject();  
        jsonObject = JSONObject.parseObject(jsonStr);
        
        JSONArray childs= jsonObject.getJSONArray("data");
        if(childs!=null && !childs.equals("")){
        	int length = childs.size();  
            for (int i = 0; i < length; i++) {  
                String childName = childs.getJSONObject(i).getString("article_url"); 
                if(childName!=null && childName.indexOf("toutiao.com/group/")!=-1){
                	String aurl = "http://www.toutiao.com/a"+childName.replaceAll("http://toutiao.com", "").replaceAll("group", "").replaceAll("/", "");
                	Map<String, String> articleMap = toutiaoArticle(aurl,key);
                	String []urlArray = childName.split("/");
                	if(articleMap!=null && articleMap.size()>0){
                		articleListMap.put(urlArray[4], articleMap);
                	}
                }
            }  
        }
		return articleListMap;
	}
	
	/**
	 * 获取头条单个文章
	 * @param url
	 * @return
	 */
	private static Map<String, String> toutiaoArticle(String url,String key){
		Map<String, String> articleMap = new HashMap<String,String>();
		Document doc = getConnection(url);
		if(doc!=null){
			Elements titlediv = doc.select(".article-title");
			if(titlediv!=null && titlediv.size()>0){
				Element titleElement = titlediv.first().select("h1").first();
				Element contentElement = doc.select("div.article-content").first();
				Element srcElement = doc.select("span.src").first();
				if(contentElement.html().contains("div")){
					contentElement = doc.select("div.article-content>div").first();
				}
				String title = titleElement.html().toString();
				String content = contentElement.html().toString();
				String src = srcElement.html().toString();
				articleMap.put("src", src);
				articleMap.put("title", title);
				articleMap.put("content", delTouTiaoNote(content));
				articleMap.put("url", url);
				articleMap.put("tags", key);
			}
		}
		return articleMap;
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
	//------------抓取方法调用
	/**
	 * 抓取头条新闻相关文章
	 * @param key 关键字
	 * @param number 提取数量
	 * @return
	 */
	 public static Map<String, Object> grabToutiao(String key,Integer number){
		 String keystr="";
		 try {
			 keystr = URLEncoder.encode(key,"utf-8");
		 } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		 }
		 String url = "http://www.toutiao.com/search_content/?offset=0&format=json&keyword="+keystr+"&autoload=true&count="+number;
		 Map<String, Object> articleMap = ToutiaoList(url,key);
		 return articleMap;
	 }
	
	
	
	
	
	
	/*public static void main(String[] args) {
		String key="";
		try {
			key = URLEncoder.encode("宠物","utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String number = "20";
		ToutiaoRss rss = new ToutiaoRss();
		
		System.out.println(rss.ToutiaoList("http://www.toutiao.com/search_content/?offset=0&format=json&keyword="+key+"&autoload=true&count="+number,key));
	}*/
	/* public static void main(String[] args) {
		 ToutiaoRss rss = new ToutiaoRss();
		 Map<String, String> articleMap = rss.toutiaoArticle("http://toutiao.com/group/6252242944688357634/","chongwu");
		 System.out.println(articleMap.get("content"));
	}*/
}


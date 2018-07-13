package com.czw.spider.pojo;
/**
 * @author 崔志伟
 * 联系方式：493067123@qq.com
 * 创建日期：2018年7月13日
 * www.cuizhiwei.com
 */
public class Article {

	private String title;	//题目
	private String content;	//内容
	private String sourceUrl;	//源地址
	private String describe;	//摘要
	private String coverUrl;	//封面图片
	private String sequence;	//平台文章流水
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSourceUrl() {
		return sourceUrl;
	}
	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public String getCoverUrl() {
		return coverUrl;
	}
	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	
	
}

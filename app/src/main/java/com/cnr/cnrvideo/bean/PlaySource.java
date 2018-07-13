package com.cnr.cnrvideo.bean;

/**
 * 播放源实体类
 * 
 * @author xingzhiqiao 2015-10-30
 */
public class PlaySource {
	/**
	 * 播放源ID
	 */
	public String sourceId;
	/**
	 * 播放源名称
	 */
	public String sourceName;
	/**
	 * 播放源地址
	 */
	private String sourceUrl;

	private int quality;

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

}

package com.cnr.cnrvideo.event;

import com.cnr.cnrvideo.bean.PlaySource;

import java.util.List;

/**
 * 播放源事件
 * 
 * @author xingzhiqiao 2015-12-3
 */
public class PlaySourceEvent {

	public List<PlaySource> playSources;

	public PlaySourceEvent(List<PlaySource> playSources) {
		this.playSources = playSources;
	}

	public List<PlaySource> getPlaySources() {
		return playSources;
	}

	public void setPlaySources(List<PlaySource> playSources) {
		this.playSources = playSources;
	}

}

package com.scheshire.starlingtest.dto;

import com.scheshire.starlingtest.models.Gallery;

import lombok.Getter;

@Getter
public class GalleryInfo {
	private String name;
	private String url;
	
	public GalleryInfo(Gallery gallery)
	{
		name = gallery.getName();
		url = getUrl(gallery.getId());
	}

	private static String getUrl(Long id)
	{
		return "/gallery/" + id;
	}
}

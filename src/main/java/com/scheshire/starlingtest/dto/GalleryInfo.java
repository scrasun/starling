package com.scheshire.starlingtest.dto;

import com.scheshire.starlingtest.models.Gallery;

import lombok.Getter;

/**
 * DTO for gallery info
 */
@Getter
public class GalleryInfo {
	// name of gallery
	private String name;
	// URL of gallery
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

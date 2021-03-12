package com.scheshire.starlingtest.dto;

import com.scheshire.starlingtest.models.Image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
public class ImageInfo {
	private String imageUrl;
	private String thumbnailUrl;
	
	public ImageInfo(Image image)
	{
		imageUrl = getUrl(image.getId());
		if (image.getThumbnail() != null)
		{
			thumbnailUrl = getUrl(image.getThumbnail().getId());
		}
		else
		{
			thumbnailUrl = imageUrl;
		}
	}
	
	private static String getUrl(Long id)
	{
		return "/images/" + id;
	}
}

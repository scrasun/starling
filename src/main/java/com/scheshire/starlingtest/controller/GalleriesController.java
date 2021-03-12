package com.scheshire.starlingtest.controller;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.scheshire.starlingtest.models.Gallery;
import com.scheshire.starlingtest.models.Image;
import com.scheshire.starlingtest.models.User;
import com.scheshire.starlingtest.repo.GalleryRepo;
import com.scheshire.starlingtest.repo.UserRepo;

@Controller
public class GalleriesController {
	@Autowired
	private GalleryRepo galleryRepo;
	@Autowired
	private UserRepo userRepo;

	@GetMapping("/galleries")
	public String getAll(Model model)
	{
		Map<Long, String> galleries = new HashMap<Long, String>();
		for (Gallery gallery : galleryRepo.findAll())
		{
			galleries.put(gallery.getId(), gallery.getName());
		}
		
		model.addAttribute("galleries", galleries);
		return "galleries";
	}

	@GetMapping("/my-galleries")
	public String getMine(Model model, Authentication authentication)
	{
		User user = userRepo.findByEmail(authentication.getName());
		
		Map<Long, String> galleries = new HashMap<Long, String>();
		for (Gallery gallery : user.getGalleries())
		{
			galleries.put(gallery.getId(), gallery.getName());
		}
		
		model.addAttribute("galleries", galleries);
		return "galleries";
	}

	@GetMapping("/gallery/{galleryId:.+}")
	public String getGallery(Model model, Authentication authentication, @PathVariable Long galleryId)
	{
		if (!galleryRepo.existsById(galleryId))
		{
			Map<String, String> images = new HashMap<String, String>();
			model.addAttribute("editable", true);
			model.addAttribute("images", images);
			model.addAttribute("gallery", galleryId);
			return "gallery";
		}
		
		Gallery gallery = galleryRepo.getOne(galleryId);

		Map<String, String> images = new HashMap<String, String>();
		
		for (Image image : gallery.getImages())
		{
			System.out.println("image!");
			images.put(image.getThumbnail() == null ? image.getFile() : image.getThumbnail().getFile(), image.getFile());
		}
		
		model.addAttribute("images", images);
		model.addAttribute("gallery", galleryId);
		
		if (authentication == null)
		{
			return "gallery";
		}

		User user = userRepo.findByEmail(authentication.getName());
		
		if (gallery.getUser().equals(user))
		{
			model.addAttribute("editable", true);
		}
		
		return "gallery";
	}
	
}

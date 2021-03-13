package com.scheshire.starlingtest.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scheshire.starlingtest.dto.GalleryInfo;
import com.scheshire.starlingtest.dto.ImageInfo;
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
	
	@PostMapping("/gallery")
	public String createGallery(Model model, Authentication authentication, @RequestParam(value = "name") String name) {
		User user = userRepo.findByEmail(authentication.getName());

		Gallery gallery = new Gallery();
		gallery.setUser(user);
		gallery.setName(name);
		
		System.out.println(user);
		System.out.println(gallery);

		return getGallery(model, authentication, galleryRepo.save(gallery).getId());
	}

	@GetMapping("/galleries")
	public String getAll(Model model)
	{
		model.addAttribute("galleries", galleryRepo.findAll().stream().map(GalleryInfo::new).collect(Collectors.toList()));
		return "galleries";
	}

	@GetMapping("/my-galleries")
	public String getMine(Model model, Authentication authentication)
	{
		User user = userRepo.findByEmail(authentication.getName());
		
		model.addAttribute("galleries", user.getGalleries().stream().map(GalleryInfo::new).collect(Collectors.toList()));
		return "galleries";
	}

	@GetMapping("/gallery/{galleryId:.+}")
	public String getGallery(Model model, Authentication authentication, @PathVariable Long galleryId)
	{
		Gallery gallery = galleryRepo.getOne(galleryId);
		
		model.addAttribute("gallery", galleryId);

		if (gallery.getImages() != null)
		{
			model.addAttribute("images", gallery.getImages().stream().map(ImageInfo::new).collect(Collectors.toList()));
		}
		
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

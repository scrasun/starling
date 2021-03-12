package com.scheshire.starlingtest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scheshire.starlingtest.models.Gallery;
import com.scheshire.starlingtest.models.User;
import com.scheshire.starlingtest.repo.GalleryRepo;
import com.scheshire.starlingtest.repo.UserRepo;

@RestController
public class EditController {
	@Autowired
	private GalleryRepo galleryRepo;
	@Autowired
	private UserRepo userRepo;
	
	@GetMapping("/new")
	public String createGallery(Authentication authentication, @RequestParam(value="name") String name)
	{
		User user = userRepo.findByEmail(authentication.getName());
		
		if (user == null)
		{
			return "no";
		}
		
		Gallery gallery = new Gallery();
		gallery.setUser(user);
		gallery.setName(name);
		
		galleryRepo.save(gallery);
		
		return "yo";
	}

}

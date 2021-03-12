package com.scheshire.starlingtest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.scheshire.starlingtest.ImageStore;
import com.scheshire.starlingtest.dto.GalleryInfo;
import com.scheshire.starlingtest.dto.ImageInfo;
import com.scheshire.starlingtest.models.Gallery;
import com.scheshire.starlingtest.models.Image;
import com.scheshire.starlingtest.models.User;
import com.scheshire.starlingtest.repo.GalleryRepo;
import com.scheshire.starlingtest.repo.ImageRepo;
import com.scheshire.starlingtest.repo.UserRepo;

@RestController
public class EditController {
	@Autowired
	private GalleryRepo galleryRepo;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private ImageStore imageStore;
	@Autowired
	private ImageRepo imageRepo;

	@PostMapping("/gallery")
	public GalleryInfo createGallery(Authentication authentication, @RequestParam(value = "name") String name) {
		User user = userRepo.findByEmail(authentication.getName());

		if (user == null) {
			return (long) 0;
		}

		Gallery gallery = new Gallery();
		gallery.setUser(user);
		gallery.setName(name);

		return new GalleryInfo(galleryRepo.save(gallery));
	}

	@PostMapping("/upload/{galleryId:.+}")
	public ImageInfo uploadImage(@RequestParam("file") MultipartFile file, Authentication authentication,
			@PathVariable Long galleryId) throws Exception {
		try {
			User user = userRepo.findByEmail(authentication.getName());
			Gallery gallery = galleryRepo.getOne(galleryId);
			if (gallery == null) {
				throw new Exception("not a gallery");
			}
			if (!gallery.getUser().equals(user)) {
				throw new Exception("not your gallery");
			}

			Image img = imageStore.saveImage(file, gallery);

			return new ImageInfo(img);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@DeleteMapping("/images/{imageId:.+}")
	public void deleteImage(Authentication authentication, @PathVariable Long imageId) throws Exception
	{
		User user = userRepo.findByEmail(authentication.getName());
		Image image = imageRepo.getOne(imageId);
		if (!image.getGallery().getUser().equals(user))
		{
			throw new Exception("not your image");
		}
		
		imageStore.deleteImage(image);
	}
}

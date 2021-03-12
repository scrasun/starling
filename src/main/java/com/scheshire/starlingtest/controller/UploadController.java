package com.scheshire.starlingtest.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.scheshire.starlingtest.ImageStore;
import com.scheshire.starlingtest.models.Gallery;
import com.scheshire.starlingtest.models.Image;
import com.scheshire.starlingtest.models.User;
import com.scheshire.starlingtest.repo.GalleryRepo;
import com.scheshire.starlingtest.repo.ImageRepo;
import com.scheshire.starlingtest.repo.UserRepo;

@Controller
public class UploadController {
	@Autowired
	private ImageStore imageStore;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private ImageRepo imageRepo;
	@Autowired
	private GalleryRepo galleryRepo;
	
	@PostMapping("/upload/{galleryId:.+}")
	public String handleFileUpload(@RequestParam("file") MultipartFile file, Authentication authentication, @PathVariable Long galleryId) throws Exception
	{
		try
		{
		User user = userRepo.findByEmail(authentication.getName());
		Gallery gallery = galleryRepo.getOne(galleryId);
		if (gallery == null)
		{
			throw new Exception("not a gallery");
		}
		if (!gallery.getUser().equals(user))
		{
			throw new Exception("not your gallery");
		}
		
		Image img = imageStore.saveImage(file, gallery);
		System.out.println(img.toString());
		}
		catch (Exception e)
		{
			System.out.println(e);
			throw e;
		}

		return "upload";
	}
	
	@GetMapping("/images/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws Exception {
		Image example = new Image();
		example.setFile(filename);
		Optional<Image> image = imageRepo.findOne(Example.of(example));
		
		if (image.isEmpty())
		{
			return ResponseEntity.notFound().build();
		}

		Resource file = imageStore.loadImage(image.get());
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment").body(file);
	}
}

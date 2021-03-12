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
public class ImageFileController {
	@Autowired
	private ImageStore imageStore;
	@Autowired
	private ImageRepo imageRepo;
	
	@GetMapping("/images/{imageId:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveImage(@PathVariable Long imageId) throws Exception {
		if (!imageRepo.existsById(imageId))
		{
			return ResponseEntity.notFound().build();
		}
		Image image = imageRepo.getOne(imageId);

		Resource file = imageStore.loadImage(image);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment").body(file);
	}
}

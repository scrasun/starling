package com.scheshire.starlingtest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.scheshire.starlingtest.models.Gallery;
import com.scheshire.starlingtest.models.Image;
import com.scheshire.starlingtest.models.User;
import com.scheshire.starlingtest.repo.ImageRepo;

/**
 * Image filestore service
 */
@Service
public class ImageStore {
	@Autowired
	private ImageRepo imageRepo;
	
	private final Path rootLocation;

	/**
	 * Create new image store
	 * @throws IOException if ./image_store could not be created
	 */
	@Autowired
	public ImageStore() throws IOException {
		String path = "./image_store";
		rootLocation = Paths.get(path);
		Files.createDirectories(rootLocation);
	}
	
	/**
	 * Removes image from store
	 * @param image Image object to remove
	 * @throws IOException if image file could not be deleted
	 */
	public void deleteImage(Image image) throws IOException
	{
		Files.deleteIfExists(getFileName(image));
		imageRepo.delete(image);
		if (image.getThumbnail() != null)
		{
			Files.deleteIfExists(getFileName(image.getThumbnail()));
			imageRepo.delete(image.getThumbnail());
		}
	}

	/**
	 * Save file to image store and add to gallery
	 * @param file The file to save
	 * @param gallery The gallery to add it to
	 * @return The new image object
	 * @throws Exception if image could not be saved
	 */
	public Image saveImage(MultipartFile file, Gallery gallery) throws Exception {
		Image image = new Image();
		image.setGallery(gallery);
		image.setFile(UUID.randomUUID().toString());
		
		if (file.isEmpty()) {
			throw new Exception("Failed to store empty file.");
		}
		
		Path destinationFile = getFileName(image);
		Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
		
		BufferedImage img = ImageIO.read(file.getInputStream());
		// Save image without thumbnail if we can't parse it
		if (img == null)
		{
			return imageRepo.save(image);
		}
		
		// Generate thumbnail
		Image thumbnail = new Image();
		thumbnail.setFile(UUID.randomUUID().toString());
		int width = img.getWidth();
		int height = img.getHeight();
		if (width > height)
		{
			height = 100 * height / width;
			width = 100;
		}
		else
		{
			width = 100 * width / height;
			height = 100;
		}

		BufferedImage thumb = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		thumb.createGraphics().drawImage(img.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH), 0, 0, null);
		ImageIO.write(thumb, "png", getFileName(thumbnail).toFile());
		
		image.setThumbnail(imageRepo.save(thumbnail));
		
		// return image object
		return imageRepo.save(image);
	}

	/**
	 * Load image file
	 * @param image The image object to load
	 * @return The resource coresponding to the image file
	 * @throws Exception If image could not be loaded
	 */
	public Resource loadImage(Image image) throws Exception {
		Path file = getFileName(image);
		Resource resource = new UrlResource(file.toUri());
		if (resource.exists() || resource.isReadable()) {
			return resource;
		}
		else {
			throw new Exception("Could not read file: " + file.toString());
		}
	}
	
	/**
	 * Get path to store image
	 * @param image The image object
	 * @return The path where the file is stored
	 */
	private Path getFileName(Image image)
	{
		return this.rootLocation.resolve(Paths.get(image.getFile())).normalize().toAbsolutePath();
	}
}

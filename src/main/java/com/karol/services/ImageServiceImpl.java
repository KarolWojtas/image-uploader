package com.karol.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.TimeZone;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Pageable;



import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import com.karol.domain.AcceptedImageFormats;
import com.karol.domain.CustomUserDetails;
import com.karol.domain.ImageHolder;
import com.karol.domain.ImageHolderDTO;
import com.karol.domain.PageDto;
import com.karol.domain.mappers.ImageHolderMapper;
import com.karol.domain.mappers.ImagePageMapper;
import com.karol.exceptions.BadFormatException;
@Service
public class ImageServiceImpl implements ImageService{
	private ImageHolderRepository imageRepository;
	private ImageHolderMapper mapper;
	private LinkService linkService;
	private ImagePageMapper imagePageMapper;
	
	@Autowired
	public ImageServiceImpl(ImageHolderRepository imageRepository, ImageHolderMapper mapper, LinkService linkService) {
		super();
		this.imageRepository = imageRepository;
		this.mapper = mapper;
		this.linkService = linkService;
		this.imagePageMapper = new ImagePageMapper(mapper, linkService);
	}

	@Override
	public ImageHolder getImage(Long imageId) {
		
		return imageRepository.findById(imageId).get();
	}

	@Override
	
	public boolean saveImage(MultipartFile image, CustomUserDetails user,String description, boolean isPublic) throws IOException, BadFormatException {
		
		ImageHolder imageHolder = ImageHolder.builder().description(description)
								.imageFormat(extractFormat(image))
								.image(this.compressImage(image, 0.5f))
								.user(user)
								.isPublic(isPublic)
								.timestamp(Instant.now().atZone(ZoneId.of("GMT+00:00")))
								.build();
		return imageRepository.save(imageHolder) != null;
	}
	private String extractFormat(MultipartFile file) throws BadFormatException {
		
		String suffix = file.getOriginalFilename().split("\\.")[1];
		return Arrays.asList(AcceptedImageFormats.values()).stream()
				.map(obj -> (AcceptedImageFormats) obj)
				.map(AcceptedImageFormats::getValue)
				.filter(value -> value.equals(suffix))
				.findFirst().orElseThrow(()-> new BadFormatException("file has wrong format"));
	}

	@Override
	public ImageHolderDTO getImageDto(Long imageId) {
		ImageHolderDTO dto = mapper.imageHolderToImageHolderDto(imageRepository.findById(imageId).get());
		dto.addLink(this.linkService.getImagelink(dto));
		return dto;
	}

	@Override
	public void deleteImage(Long imageId) {
		imageRepository.deleteById(imageId);
		
	}
	private ImageHolderDTO addSelfLink(ImageHolderDTO dto) {
		dto.addLink(linkService.getImagelink(dto));
		return dto;
	}

	@Override
	@Transactional
	public PageDto<ImageHolderDTO> getAllImagesByUser(CustomUserDetails user, Pageable page, TimeZone tz) {
		return imagePageMapper.pageToPageDto(imageRepository.findAllByUserOrderByTimestampDesc(user, page),false,tz);
		
	}

	@Override
	public PageDto<ImageHolderDTO> getPaginatedPublicImages(Pageable page, TimeZone tz) {
		return imagePageMapper.pageToPageDto(imageRepository.findByIsPublicOrderByTimestampDesc(true, page), true, tz);
	}
	private byte[] compressImage(MultipartFile file, float compressionQuality) {
		byte[] imageCompressed = null;
		try (ByteArrayOutputStream os = new ByteArrayOutputStream() ; ImageOutputStream ios = ImageIO.createImageOutputStream(os)){
			BufferedImage image = ImageIO.read(file.getInputStream());
			
			ImageWriter imageWriter = ImageIO.getImageWritersByFormatName("jpg").next();
			imageWriter.setOutput(ios);
			ImageWriteParam param = imageWriter.getDefaultWriteParam();
			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			param.setCompressionQuality(compressionQuality);
			imageWriter.write(null, new IIOImage(image, null, null), param);
			imageCompressed = os.toByteArray();
			
			
			imageWriter.dispose();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return imageCompressed;
	}

	

}

package com.karol.services;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.CrudRepository;

import com.karol.domain.CustomUserDetails;
import com.karol.domain.ImageHolder;

public interface ImageHolderRepository extends CrudRepository<ImageHolder, Long>{
	Slice<ImageHolder> findAllByIsPublic(boolean isPublic, Pageable pageable);
	List<ImageHolder> findAllByIsPublic(boolean isPublic);
	List<ImageHolder> findAllByUser(CustomUserDetails user);
}

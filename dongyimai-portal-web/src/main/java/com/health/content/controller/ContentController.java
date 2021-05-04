package com.health.content.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.content.service.ContentService;
import com.health.pojo.TbContent;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * InnoDB free: 5120 kBcontroller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/content")
public class ContentController {

	@Reference
	private ContentService contentService;
	
	@RequestMapping("/findContentById")
	public List<TbContent> findContentById(Long categoryId){
		return contentService.findContentById(categoryId);
	}
}

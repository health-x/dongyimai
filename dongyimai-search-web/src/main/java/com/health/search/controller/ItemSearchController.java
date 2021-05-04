package com.health.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.search.service.ItemSearchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/search")
public class ItemSearchController {

    @Reference(timeout = 3000)
    private ItemSearchService searchService;

    @RequestMapping("/search")
    public Map<String,Object> search(@RequestBody Map searchMap){
        return searchService.search(searchMap);
    }
}
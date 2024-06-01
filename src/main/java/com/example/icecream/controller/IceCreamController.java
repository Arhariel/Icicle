package com.example.icecream.controller;

import com.example.icecream.exception.ResourceNotFoundException;
import com.example.icecream.model.IceCream;
import com.example.icecream.model.Category;
import com.example.icecream.repository.IceCreamRepository;
import com.example.icecream.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class IceCreamController {

    @Autowired
    private IceCreamRepository iceCreamRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private static final Logger logger = LoggerFactory.getLogger(IceCreamController.class);

    @GetMapping("/icecreams")
    public List<IceCream> getAllIceCreams() {
        return iceCreamRepository.findAll();
    }

    @PostMapping("/icecreams")
    public IceCream createIceCream(@RequestBody IceCream iceCream) {
        logger.info("Received request to create ice cream: " + iceCream.getName());

        // Check if category exists
        Category category = categoryRepository.findByName(iceCream.getCategory().getName());
        if (category == null) {
            // Create new category
            logger.info("Category not found, creating new category: " + iceCream.getCategory().getName());
            category = new Category();
            category.setName(iceCream.getCategory().getName());
            category = categoryRepository.save(category);
        }
        iceCream.setCategory(category);
        IceCream savedIceCream = iceCreamRepository.save(iceCream);
        logger.info("Ice cream created successfully: " + savedIceCream.getId());
        return savedIceCream;
    }

    @PutMapping("/icecreams/{id}")
    public IceCream updateIceCream(@PathVariable Long id, @RequestBody IceCream iceCreamDetails) {
        IceCream iceCream = iceCreamRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("IceCream not found for this id :: " + id));
        iceCream.setName(iceCreamDetails.getName());
        iceCream.setPrice(iceCreamDetails.getPrice());

        Category category = categoryRepository.findByName(iceCreamDetails.getCategory().getName());
        if (category == null) {
            category = new Category();
            category.setName(iceCreamDetails.getCategory().getName());
            category = categoryRepository.save(category);
        }
        iceCream.setCategory(category);

        final IceCream updatedIceCream = iceCreamRepository.save(iceCream);
        return updatedIceCream;
    }

    @DeleteMapping("/icecreams/{id}")
    public Map<String, Boolean> deleteIceCream(@PathVariable Long id) {
        IceCream iceCream = iceCreamRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("IceCream not found for this id :: " + id));
        Category category = iceCream.getCategory();
        iceCreamRepository.delete(iceCream);

        // Check if category has no more ice creams
        if (iceCreamRepository.findByCategory(category).isEmpty()) {
            categoryRepository.delete(category);
        }

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }


    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @PostMapping("/categories")
    public Category createCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }


}

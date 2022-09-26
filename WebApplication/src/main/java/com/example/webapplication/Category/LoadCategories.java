package com.example.webapplication.Category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class LoadCategories {

    private final CategoryRepository categoryRepository;

    @Bean("LoadCategories")
    public CommandLineRunner StoreCategories(CategoryRepository categoryRepository){

        categoryRepository.save(new Category(1L,"Collectibles"));
        categoryRepository.save(new Category(2L,"Technology"));
        categoryRepository.save(new Category(3L,"Art"));
        categoryRepository.save(new Category(4L,"Gaming"));
        categoryRepository.save(new Category(5L,"Sports"));
        categoryRepository.save(new Category(6L,"Mobile"));

        return args -> {
            log.info("Store Categories to Database!");
        };
    }
}


/*
INSERT INTO categories(category_id,name) VALUE (1,'Collectibles');
INSERT INTO categories(category_id,name) VALUE (2,'Technology');
INSERT INTO categories(category_id,name) VALUE (3,'Art');
INSERT INTO categories(category_id,name) VALUE (4,'Gaming');
INSERT INTO categories(category_id,name) VALUE (5,'Sports');
INSERT INTO categories(category_id,name) VALUE (6,'Mobile');
*/
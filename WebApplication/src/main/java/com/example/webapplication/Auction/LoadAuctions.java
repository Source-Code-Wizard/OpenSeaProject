package com.example.webapplication.Auction;


import com.example.webapplication.Category.Category;
import com.example.webapplication.Category.CategoryRepository;
import com.example.webapplication.Seller.Seller;
import com.example.webapplication.Seller.SellerRepository;
import com.example.webapplication.User.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class LoadAuctions {
    private final AuctionRepository auctionRepository;
    private final CategoryRepository categoryRepository;
    private final SellerRepository sellerRepository;



    @Bean("LoadAuction")
    @DependsOn({"LoadCategories","LoadAdministrator"})
    public CommandLineRunner StoreAuctions(AuctionRepository auctionRepository,
                                           CategoryRepository categoryRepository,
                                           SellerRepository sellerRepository){

        /* Retrieve all categories*/
        Category Technology = categoryRepository.findByName("Technology");
        Category Gaming = categoryRepository.findByName("Gaming");
        Category Art = categoryRepository.findByName("Art");
        Category Collectibles = categoryRepository.findByName("Collectibles");
        Category Sports = categoryRepository.findByName("Sports");
        Category Mobile = categoryRepository.findByName("Mobile");

        /* Retrive seller */
        Seller auctionSeller = sellerRepository.findById(1L).get();

        /* Build Auction */
        if(auctionRepository.findByName("Iphone 14").isEmpty()){
            Auction Iphone_14 = new Auction("Iphone 14",0,1300.00,0.00,0,"Athens",
                    LocalDateTime.now(),LocalDateTime.now().plusHours(5L),"New Iphone 14 has finally released!");
            Iphone_14.setSeller(auctionSeller);
            System.out.println("Item id is "+Iphone_14.getItemId());
            Set<Category> Iphone_14_categories = new HashSet<>();
            Iphone_14_categories.add(Technology);
            Iphone_14_categories.add(Mobile);
            Iphone_14_categories.add(Gaming);
            Iphone_14.setCategories(Iphone_14_categories);
            auctionRepository.save(Iphone_14);
        }

        if(auctionRepository.findByName("Iphone 13").isEmpty()){
            Auction Iphone_13 = new Auction("Iphone 13",0,1200.00,0.00,0,"Athens",
                    LocalDateTime.now(),LocalDateTime.now().plusHours(5L),"New Iphone 13 has finally released!");
            Iphone_13.setSeller(auctionSeller);
            System.out.println("Item id is "+Iphone_13.getItemId());
            Set<Category> Iphone_13_categories = new HashSet<>();
            Iphone_13_categories.add(Technology);
            Iphone_13_categories.add(Mobile);
            Iphone_13_categories.add(Gaming);
            Iphone_13.setCategories(Iphone_13_categories);
            auctionRepository.save(Iphone_13);
        }

        if(auctionRepository.findByName("Iphone 12").isEmpty()){
            Auction Iphone_12 = new Auction("Iphone 12",0,1200.00,0.00,0,"Athens",
                    LocalDateTime.now(),LocalDateTime.now().plusHours(5L),"New Iphone 12 has finally released!");
            Iphone_12.setSeller(auctionSeller);
            System.out.println("Item id is "+Iphone_12.getItemId());
            Set<Category> Iphone_12_categories = new HashSet<>();
            Iphone_12_categories.add(Technology);
            Iphone_12_categories.add(Mobile);
            Iphone_12_categories.add(Gaming);
            Iphone_12.setCategories(Iphone_12_categories);
            auctionRepository.save(Iphone_12);
        }

        if(auctionRepository.findByName("Xbox X").isEmpty()){
            Auction Xbox_X = new Auction("Xbox X",0,800.00,0.00,0,"Athens",
                    LocalDateTime.now(),LocalDateTime.now().plusHours(5L),"Xbox X has finally released!");
            Xbox_X.setSeller(auctionSeller);
            System.out.println("Item id is "+Xbox_X.getItemId());
            Set<Category> Xbox_X_categories = new HashSet<>();
            Xbox_X_categories.add(Technology);
            Xbox_X_categories.add(Gaming);
            Xbox_X.setCategories(Xbox_X_categories);
            auctionRepository.save(Xbox_X);
        }

        if(auctionRepository.findByName("PlayStation 5").isEmpty()){
            Auction PlayStation_5 = new Auction("PlayStation 5",0,800.00,0.00,0,"Athens",
                    LocalDateTime.now(),LocalDateTime.now().plusHours(5L),"PlayStation 5 has finally released!");
            PlayStation_5.setSeller(auctionSeller);
            System.out.println("Item id is "+PlayStation_5.getItemId());
            Set<Category> PlaySyation_5_categories = new HashSet<>();
            PlaySyation_5_categories.add(Technology);
            PlaySyation_5_categories.add(Gaming);
            PlayStation_5.setCategories(PlaySyation_5_categories);
            auctionRepository.save(PlayStation_5);
        }

        if(auctionRepository.findByName("Mona Liza").isEmpty()){
            Auction Mona_Liza = new Auction("Mona Liza",0,10000.00,0.00,0,"Athens",
                    LocalDateTime.now(),LocalDateTime.now().plusHours(5L),"Mona Liza, the legendary painting!");
            Mona_Liza.setSeller(auctionSeller);
            System.out.println("Item id is "+Mona_Liza.getItemId());
            Set<Category> Mona_Liza_categories = new HashSet<>();
            Mona_Liza_categories.add(Art);
            Mona_Liza_categories.add(Collectibles);
            Mona_Liza.setCategories(Mona_Liza_categories);
            auctionRepository.save(Mona_Liza);
        }

        if(auctionRepository.findByName("Ancient Sword").isEmpty()){
            Auction Ancient_Sword = new Auction("Ancient Sword",0,6000.00,0.00,0,"Athens",
                    LocalDateTime.now(),LocalDateTime.now().plusHours(5L),"Ancient Sword, almost 3000 years old!");
            Ancient_Sword.setSeller(auctionSeller);
            System.out.println("Item id is "+Ancient_Sword.getItemId());
            Set<Category> Ancient_Sword_categories = new HashSet<>();
            Ancient_Sword_categories.add(Art);
            Ancient_Sword_categories.add(Collectibles);
            Ancient_Sword.setCategories(Ancient_Sword_categories);
            auctionRepository.save(Ancient_Sword);
        }

        if(auctionRepository.findByName("Jordans").isEmpty()){
            Auction Jordans = new Auction("Jordans",0,1200.00,0.00,0,"Athens",
                    LocalDateTime.now(),LocalDateTime.now().plusHours(5L),"The best sneakers in the market!");
            Jordans.setSeller(auctionSeller);
            System.out.println("Item id is "+Jordans.getItemId());
            Set<Category> Jordans_categories = new HashSet<>();
            Jordans_categories.add(Art);
            Jordans_categories.add(Collectibles);
            Jordans.setCategories(Jordans_categories);
            auctionRepository.save(Jordans);
        }


        return args -> {
            log.info("Auctions Stored to Database!");
        };
    }
}

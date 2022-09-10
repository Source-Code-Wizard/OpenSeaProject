package com.example.webapplication.Matrix_factorization;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@Slf4j
@EnableScheduling
public class LoadMatrixFactorization {

    MatrixFactorization matrixFactorization;

    @Bean("MatrixFactorizationInit")
    public CommandLineRunner initMatrixFactorization(MatrixFactorization matrixFactorization) {
        this.matrixFactorization = matrixFactorization;
        return args -> {
            log.info("Initialized matrix factorization");
        };
    }

    @DependsOn({"MatrixFactorizationInit"})
    @Scheduled(fixedRate = 1200 * 1000) /* 1200 seconds */
    public void run_mf(){
        log.info("Running matrix factorization for Auctions");
        matrixFactorization.AuctionMatrixFactorization();
    }

}

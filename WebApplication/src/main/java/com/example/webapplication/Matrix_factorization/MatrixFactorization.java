package com.example.webapplication.Matrix_factorization;


import com.example.webapplication.Auction.Auction;
import com.example.webapplication.Auction.AuctionRepository;
import com.example.webapplication.AuctionView.AuctionView;
import com.example.webapplication.AuctionView.AuctionViewRepository;
import com.example.webapplication.Bid.Bid;
import com.example.webapplication.Bid.BidRepository;
import com.example.webapplication.User.User;
import com.example.webapplication.User.UserRepository;
import org.ejml.simple.SimpleMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.min;

@Service("ms")
public class MatrixFactorization {

    private SimpleMatrix auction_dataMatrix;
    private SimpleMatrix auction_recommendationsMatrix;

    List<Long> auction_ids;
    List<Long> auction_user_ids;

    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;

    private final BidRepository bidRepository;

    private final AuctionViewRepository auctionViewRepository;

    @Autowired
    public MatrixFactorization(UserRepository userRepository, AuctionRepository auctionRepository,
                               AuctionViewRepository auctionViewRepository,BidRepository bidRepository) {
        this.userRepository = userRepository;
        this.auctionRepository = auctionRepository;
        this.auctionViewRepository=auctionViewRepository;
        this.bidRepository=bidRepository;
    }

    public List<Long> auction_recommendations(Long user_id, int size){
        int user_index = auction_user_ids.indexOf(user_id); /* get index of user */

        /* put all ratings in a map{auction_index->rating}*/
        Map<Integer, Double> ratings = new HashMap<>();
        for (int i = 0; i< auction_recommendationsMatrix.numCols(); i++){
            if (auction_dataMatrix.get(user_index, i) != 0){
                ratings.put(i, auction_recommendationsMatrix.get(user_index, i));
            }
        }
        /* sort by rating */
        List<Map.Entry<Integer, Double>> list = new ArrayList<>(ratings.entrySet());
        list.sort(Map.Entry.comparingByValue());

        /* keep only ids */
        List<Long> ids_ordered = new ArrayList<>();
        int index;
        for (Map.Entry<Integer, Double> entry : list) {
            index = entry.getKey();
            ids_ordered.add(auction_ids.get(index));
        }
        System.out.println(ids_ordered);

        return ids_ordered.subList(0, min(ids_ordered.size(), size));
    }
    public void AuctionMatrixFactorization(){
        /* Applies matrix factorization for auctions.
         * It fills a users*auction matrix with ratings based on views.
         */

        auction_user_ids = userRepository.findAll().stream().map(User::getUserId).collect(Collectors.toList()); /* get all user ids in a list */
        auction_ids = auctionRepository.findAll().stream().map(Auction::getItemId).collect(Collectors.toList()); /* get all auction ids in a list */

        System.out.println("after lists");

        auction_dataMatrix = new SimpleMatrix(auction_user_ids.size(),auction_ids.size()); /* dataMatrix' size will be number_of_users*number_of_auctions */

        System.out.println("Iterate by the User Ids");

        /* Iterate by the User Ids*/
        for (Long eachUserId : auction_user_ids){

            System.out.println("User id :"+eachUserId);

            int userIndex =  auction_user_ids.indexOf(eachUserId);

            List<AuctionView> userAuctionViewsHistory = auctionViewRepository.findAuctionViewByUser(eachUserId);
            List<Bid> userBidHistory = bidRepository.findBidByUser(eachUserId);

            System.out.println("Bid list size : "+userBidHistory.size());
            System.out.println("ViewList is :"+userAuctionViewsHistory.size());

                for (Bid eachBid : userBidHistory){
                    //Long auction_id=eachBid.getAuction().getItemId();
                    Long auction_id=eachBid.getItem_Id();
                    int auction_index = auction_ids.indexOf(auction_id);
                    auction_dataMatrix.set(userIndex, auction_index, auction_dataMatrix.get(userIndex, auction_index) + 1);
                }

                for (AuctionView eachAuctionView: userAuctionViewsHistory){
                    Long auction_id = eachAuctionView.getAuction().getItemId();
                    System.out.println("Auction id:"+ auction_id);
//                    System.out.println("list_ids:"+post_user_ids);
                    int auction_index = auction_ids.indexOf(auction_id);
//                    System.out.println("post index:"+post_index);
                    auction_dataMatrix.set(userIndex, auction_index, auction_dataMatrix.get(userIndex, auction_index) + 1);
                }
        }

        int k=3;
//        double h=0.00001;
        Random rand = new Random();
        List<Double> list_of_h = new ArrayList<Double>(){{
            add(0.01);
            add(0.001);
            add(0.0001);
            add(0.00001);
        }};
        double min_error = 999999, error;
        double best_h = 0.01;
        SimpleMatrix best_matrix = null;
        for (double h: list_of_h){
            SimpleMatrix V = SimpleMatrix.random_DDRM(auction_dataMatrix.numRows(),k,1,5,rand);
            SimpleMatrix F = SimpleMatrix.random_DDRM(k, auction_dataMatrix.numCols(),1,5,rand);

            Pair<SimpleMatrix, Double> tuple = algorithm(auction_dataMatrix, V, F, k, h);
//            System.out.println("for h=" + h + " error is " + tuple.getSecond());
            if (tuple.getSecond()<min_error) {
                min_error = tuple.getSecond();
                best_h = h;
                best_matrix = tuple.getFirst();
            }
        }
        System.out.println("Best h=" + best_h + " with error " + min_error);
        auction_recommendationsMatrix = best_matrix;
        best_matrix.print();

    }

    private Pair<SimpleMatrix, Double> algorithm(SimpleMatrix dataMatrix, SimpleMatrix V, SimpleMatrix F, int k, double h){
        int max_iters = 10000;
        double err=999999,e,prev_err;
        double x_;
        for(int iter=0; iter<=max_iters; iter++){
            for(int i=0; i<dataMatrix.numRows(); i++){
                for(int j=0; j<dataMatrix.numCols(); j++) {
                    if (dataMatrix.get(i, j) > 0){
                        x_=0;
                        for(int n=0; n<k; n++)
                            x_ += V.get(i,n)*F.get(n,j);
                        e = dataMatrix.get(i, j) - x_;
                        for (int n = 0; n < k; n++) {
                            V.set(i, n, V.get(i, n) + h * 2 * e * F.get(n, j));
                            F.set(n, j, F.get(n, j) + h * 2 * e * V.get(i, n));
                        }
                    }
                }
            }
            prev_err=err;
            err=0;
            for(int i=0; i<dataMatrix.numRows(); i++){
                for(int j=0; j<dataMatrix.numCols(); j++) {
                    if (dataMatrix.get(i, j) > 0) {
                        x_=0;
                        for(int n=0; n<k; n++)
                            x_ += V.get(i,n)*F.get(n,j);
                        err += Math.pow(dataMatrix.get(i, j) - x_, 2);
                    }
                }
            }
            if(prev_err <= err ){
                System.out.println("Iter: "+iter);
                break;
            }
        }
//        System.out.println("err: "+err);
//        System.out.println("Initial: ");
//        dataMatrix.print();
        SimpleMatrix recommendationsMatrix = V.mult(F);
//        System.out.println("Result: ");
//        recommendationsMatrix.print();
//        Pair<SimpleMatrix, Double> tuple = Pair.of(recommendationsMatrix,err);
        return Pair.of(recommendationsMatrix,err);
    }
}

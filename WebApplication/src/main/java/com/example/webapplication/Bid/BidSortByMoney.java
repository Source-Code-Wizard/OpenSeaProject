package com.example.webapplication.Bid;

import java.util.Comparator;

public class BidSortByMoney implements Comparator<Bid> {
    public int compare(Bid a, Bid b)
    {
        return (int) (a.getMoneyAmount() - b.getMoneyAmount());
    }
}

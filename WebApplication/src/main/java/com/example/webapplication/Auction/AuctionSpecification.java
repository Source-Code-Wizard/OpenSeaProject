package com.example.webapplication.Auction;

import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;

@Service
public class AuctionSpecification implements Specification<Auction> {
    private List<SearchCriteria> list;

    public void MovieSpecification() {
        this.list = new ArrayList<>();
    }

    public void add(SearchCriteria criteria) {
        list.add(criteria);
    }

    @Override
    public Predicate toPredicate(Root<Auction> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        //create a new predicate list
        List<Predicate> predicates = new ArrayList<>();

        //add add criteria to predicates
        for (SearchCriteria criteria : list) {
            if (criteria.getOperation().equals(SearchOperation.GREATER_THAN)) {
                predicates.add((Predicate) builder.greaterThan(
                        root.get(criteria.getKey()), criteria.getValue().toString()));
            } else if (criteria.getOperation().equals(SearchOperation.LESS_THAN)) {
                predicates.add((Predicate) builder.lessThan(
                        root.get(criteria.getKey()), criteria.getValue().toString()));
            } else if (criteria.getOperation().equals(SearchOperation.GREATER_THAN_EQUAL)) {
                predicates.add((Predicate) builder.greaterThanOrEqualTo(
                        root.get(criteria.getKey()), criteria.getValue().toString()));
            } else if (criteria.getOperation().equals(SearchOperation.LESS_THAN_EQUAL)) {
                predicates.add((Predicate) builder.lessThanOrEqualTo(
                        root.get(criteria.getKey()), criteria.getValue().toString()));
            } else if (criteria.getOperation().equals(SearchOperation.NOT_EQUAL)) {
                predicates.add((Predicate) builder.notEqual(
                        root.get(criteria.getKey()), criteria.getValue()));
            } else if (criteria.getOperation().equals(SearchOperation.EQUAL)) {
                predicates.add((Predicate) builder.equal(
                        root.get(criteria.getKey()), criteria.getValue()));
            } else if (criteria.getOperation().equals(SearchOperation.MATCH)) {
                predicates.add((Predicate) builder.like(
                        builder.lower(root.get(criteria.getKey())),
                        "%" + criteria.getValue().toString().toLowerCase() + "%"));
            } else if (criteria.getOperation().equals(SearchOperation.MATCH_END)) {
                predicates.add((Predicate) builder.like(
                        builder.lower(root.get(criteria.getKey())),
                        criteria.getValue().toString().toLowerCase() + "%"));
            } else if (criteria.getOperation().equals(SearchOperation.MATCH_START)) {
                predicates.add((Predicate) builder.like(
                        builder.lower(root.get(criteria.getKey())),
                        "%" + criteria.getValue().toString().toLowerCase()));
            } else if (criteria.getOperation().equals(SearchOperation.IN)) {
                predicates.add((Predicate) builder.in(root.get(criteria.getKey())).value(criteria.getValue()));
            } else if (criteria.getOperation().equals(SearchOperation.NOT_IN)) {
                predicates.add((Predicate) builder.not(root.get(criteria.getKey())).in(criteria.getValue()));
            }
        }

        return (Predicate) builder.and(predicates.toArray(new Predicate[0]));
    }
}

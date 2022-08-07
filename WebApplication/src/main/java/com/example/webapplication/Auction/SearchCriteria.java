package com.example.webapplication.Auction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SearchCriteria {
    private String key; /* key — Represents the entity field name, i.e. title, genre, category etc. */
    private Object value; /* value — Represents the parameter value, i.e. price, mobile  etc. */
    private SearchOperation operation; /* operation — Indicates the search operation, i.e. equality, match, comparison, etc. */

    public SearchCriteria(String key, Object value, SearchOperation operation) {
        this.key = key;
        this.value = value;
        this.operation = operation;
    }
}

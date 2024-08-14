package com.hired.onlineshopping.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class OnlineShoppingUser {
    public String id;
    public String name;
    public String email;
}

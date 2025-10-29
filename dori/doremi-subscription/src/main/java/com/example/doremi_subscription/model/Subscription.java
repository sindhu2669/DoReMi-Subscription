package com.example.doremi_subscription.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {
    private String category;
    private String plan;
    private int amount;
    private int duration;
    private String renewalDate;
    private String renewBefore;
}

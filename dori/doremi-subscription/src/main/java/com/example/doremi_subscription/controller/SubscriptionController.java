package com.example.doremi_subscription.controller;

import com.example.doremi_subscription.model.Subscription;
import com.example.doremi_subscription.service.SubscriptionService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService service;

    public SubscriptionController(SubscriptionService service) {
        this.service = service;
    }

    @PostMapping("/calculate")
    public Map<String, Object> calculate(@RequestBody Map<String, Object> payload) {
        try {
            String startDate = (String) payload.get("startDate");
            if (startDate == null || startDate.isEmpty()) {
                throw new IllegalArgumentException("Start date is required");
            }

            Map<String, Object> userSubsRaw = (Map<String, Object>) payload.get("subscriptions");
            Map<String, String> userSubs = new HashMap<>();
            if (userSubsRaw != null) {
                userSubsRaw.forEach((key, value) -> {
                    if (value != null) userSubs.put(key, String.valueOf(value));
                });
            }

            String topUp = payload.get("topUp") != null ? payload.get("topUp").toString() : null;
            int months = 0;
            Object monthsObj = payload.get("topUpMonths");
            if (monthsObj instanceof Number) {
                months = ((Number) monthsObj).intValue();
            } else if (monthsObj instanceof String && !((String) monthsObj).isEmpty()) {
                months = Integer.parseInt((String) monthsObj);
            }

            List<Subscription> subs = service.calculateSubscriptions(startDate, userSubs);
            int total = service.calculateTotalAmount(subs, topUp, months);

            return Map.of(
                    "subscriptions", subs,
                    "total", total,
                    "message", "Calculation successful"
            );

        } catch (Exception e) {
            e.printStackTrace();
            return Map.of(
                    "error", "Failed to calculate subscription",
                    "details", e.getMessage()
            );
        }
    }
}

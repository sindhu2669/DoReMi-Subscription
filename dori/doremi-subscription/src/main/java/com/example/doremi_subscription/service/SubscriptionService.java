package com.example.doremi_subscription.service;

import com.example.doremi_subscription.model.Subscription;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class SubscriptionService {

    private final Map<String, Map<String, Integer>> planPrices = new HashMap<>();
    private final Map<String, Integer> planDurations = new HashMap<>();

    public SubscriptionService() {
        // 💰 Prices (Rs)
        planPrices.put("MUSIC", Map.of("FREE", 0, "PERSONAL", 100, "PREMIUM", 250));
        planPrices.put("VIDEO", Map.of("FREE", 0, "PERSONAL", 200, "PREMIUM", 500));
        planPrices.put("PODCAST", Map.of("FREE", 0, "PERSONAL", 100, "PREMIUM", 300));

        // 🕒 Durations (months)
        planDurations.put("FREE", 1);
        planDurations.put("PERSONAL", 1);
        planDurations.put("PREMIUM", 3);
    }

    public List<Subscription> calculateSubscriptions(String startDate, Map<String, String> userSubs) {
        List<Subscription> result = new ArrayList<>();

        LocalDate start;
        //Support both yyyy-MM-dd and dd-MM-yyyy
        try {
            if (startDate.contains("-") && startDate.indexOf('-') == 2) {
                start = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            } else {
                start = LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format. Please use dd-MM-yyyy or yyyy-MM-dd");
        }

        for (String category : userSubs.keySet()) {
            String plan = userSubs.get(category).toUpperCase();
            int duration = planDurations.getOrDefault(plan, 1);
            int amount = planPrices.getOrDefault(category.toUpperCase(), Map.of()).getOrDefault(plan, 0);

            // 🟩 Calculate renewal & reminder dates
            LocalDate renewalDate = start.plusMonths(duration);
            LocalDate reminderDate = renewalDate.minusDays(10); // 🔔 10 days before expiry

            String formattedRenewal = renewalDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            String formattedReminder = reminderDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            result.add(new Subscription(category, plan, amount, duration, formattedRenewal, formattedReminder));
        }

        return result;
    }

    public int calculateTotalAmount(List<Subscription> subs, String topUp, int months) {
        int total = subs.stream().mapToInt(Subscription::getAmount).sum();

        if (topUp != null && !topUp.isEmpty() && months > 0) {
            int topUpCost = topUp.equalsIgnoreCase("FOUR_DEVICE") ? 50 : 100;
            total += topUpCost * months;
        }

        return total;
    }
}

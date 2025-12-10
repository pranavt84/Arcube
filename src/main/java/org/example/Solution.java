package org.example;

import java.util.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

class SearchCriteria {

    private String currency;
    private Integer maxStops;
    private Integer maxTotalDuration;
    Boolean requiredBag;

    public SearchCriteria(String currency, Integer maxStops, Integer maxTotalDuration, Boolean requiredBag) {
        this.currency = currency;
        this.maxStops = maxStops;
        this.maxTotalDuration = maxTotalDuration;
        this.requiredBag = requiredBag;
    }

    public String getCurrency() {
        return currency;
    }

    public Integer getMaxStops() {
        return maxStops;
    }

    public Integer getMaxTotalDuration() {
        return maxTotalDuration;
    }

    public Boolean getRequiredBag() {
        return requiredBag;
    }
}

class Offer {

    private String offerId;
    private String itineraryId;
    private String providerCode;
    private Double price;
    private String currency;
    private Integer stops;
    private Integer totalDuration;
    private Boolean includesBag;
    private Boolean refundable;

    public Offer(String offerId, String itineraryId, String providerCode, Double price, String currency, Integer stops, Integer totalDuration, Boolean includesBag, Boolean refundable) {
        this.offerId = offerId;
        this.itineraryId = itineraryId;
        this.providerCode = providerCode;
        this.price = price;
        this.currency = currency;
        this.stops = stops;
        this.totalDuration = totalDuration;
        this.includesBag = includesBag;
        this.refundable = refundable;
    }

    public String getOfferId() {
        return offerId;
    }

    public String getItineraryId() {
        return itineraryId;
    }

    public String getProviderCode() {
        return providerCode;
    }

    public Double getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public Integer getStops() {
        return stops;
    }

    public Integer getTotalDuration() {
        return totalDuration;
    }

    public Boolean getIncludesBag() {
        return includesBag;
    }

    public Boolean getRefundable() {
        return refundable;
    }
}

public class Solution {

    static void main() {

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))){

            String[] criteriaLine =  bufferedReader.readLine().trim().split(" ");

            SearchCriteria searchCriteria = new SearchCriteria(
                    criteriaLine[0],
                    Integer.parseInt(criteriaLine[1]),
                    Integer.parseInt(criteriaLine[2]),
                    Boolean.parseBoolean(criteriaLine[3])
            );

            int n = Integer.parseInt(bufferedReader.readLine());

            List<Offer> offers = new ArrayList<>();

            for(int i =0; i<n ;i++) {

                String[] offerLine = bufferedReader.readLine().split(" ");
                if (offerLine.length < 9) continue;

                try {
                    Offer offer = new Offer(
                            offerLine[0], offerLine[1], offerLine[2],
                            Double.parseDouble(offerLine[3]), offerLine[4],
                            Integer.parseInt(offerLine[5]),
                            Integer.parseInt(offerLine[6]),
                            Boolean.parseBoolean(offerLine[7]),
                            Boolean.parseBoolean(offerLine[8])
                    );

                    offers.add(offer);
                } catch (Exception e) {
                    System.out.println("Invalid input " + e.getMessage());
                    continue;
                }
            }

            List<String> bestOffers = getBestOffers(offers, searchCriteria);

            bestOffers.forEach(offer -> System.out.println(offer));

        } catch (Exception e) {
            System.out.println("exception in main program " + e.getMessage());
        }
    }

    static List<String> getBestOffers(List<Offer> offers, SearchCriteria searchCriteria) {

        ArrayList<String> res = new ArrayList<>();

        Map<String, List<Offer>> groupedOffers = offers.stream()
                .filter(offer -> offer != null && isValidOffer(offer, searchCriteria))
                .collect(Collectors.groupingBy(offer -> offer.getItineraryId()));

        Map<String, Offer> bestOffer = groupedOffers.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> bestOffer(entry.getValue()),
                        (o1, o2) -> o1,
                        TreeMap::new
                ));

        for(Map.Entry<String, Offer> entry : bestOffer.entrySet()) {
            String curOffer = entry.getKey() + " " + entry.getValue().getOfferId();

            res.add(curOffer);
        }

        return res;
    }

    static Offer bestOffer(List<Offer> offers) {

        Offer bestOffer = offers.stream()
                .min(
                  Comparator.comparing((Offer offer) -> offer.getPrice())
                          .thenComparing(o -> !o.getIncludesBag())
                          .thenComparing(o -> !o.getRefundable())
                          .thenComparing(o -> o.getTotalDuration())
                          .thenComparing(o -> o.getProviderCode())
                ).orElse(null);

        return bestOffer;
    }

    static boolean isValidOffer(Offer offer, SearchCriteria searchCriteria) {

        return offer.getOfferId() != null && !offer.getOfferId().trim().isEmpty()
                && offer.getItineraryId() != null && !offer.getItineraryId().trim().isEmpty()
                && offer.getCurrency() != null && !offer.getCurrency().trim().isEmpty() && offer.getCurrency().equals(searchCriteria.getCurrency())
                && offer.getPrice() > 0
                && offer.getStops() <= searchCriteria.getMaxStops()
                && offer.getTotalDuration() <= searchCriteria.getMaxTotalDuration()
                && (!searchCriteria.getRequiredBag() || offer.getIncludesBag());

    }
}

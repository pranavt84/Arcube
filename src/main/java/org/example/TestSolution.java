package org.example;

import org.junit.Assert;
import org.junit.Test;
import java.util.*;

public class TestSolution {


    @Test
    public void testSampleTestCase() {

        SearchCriteria searchCriteria = getSearchCriteria("EUR 2 600 true");

        int n = 5;

        List<String> offersLine = Arrays.asList(
          "OFF001 ITN100 6E 150.00 EUR 1 320 true false",
          "OFF002 ITN100 MMT 150.00 EUR 1 300 true true",
          "OFF003 ITN100 EK 200.00 EUR 0 280 true true",
          "OFF004 ITN200 LH 180.00 EUR 1 400 false false",
          "OFF005 ITN200 AI 190.00 EUR 1 350 true false"
        );

        List<Offer> offers = new ArrayList<>();

        for (String line : offersLine) {
            Offer offer =   getOffer(line);
            if (offer == null) continue;
            offers.add(offer);
        }

        List<String> actual = Solution.getBestOffers(offers, searchCriteria);

        List<String> expected = Arrays.asList("ITN100 OFF002", "ITN200 OFF005");

        Assert.assertEquals(actual, expected);
    }


    private SearchCriteria getSearchCriteria(String searchCriteriaInput) {

        String[] criteriaLine = searchCriteriaInput.split(" ");

        SearchCriteria searchCriteria = new SearchCriteria(
                criteriaLine[0],
                Integer.parseInt(criteriaLine[1]),
                Integer.parseInt(criteriaLine[2]),
                Boolean.parseBoolean(criteriaLine[3])
        );

        return searchCriteria;
    }

    private Offer getOffer(String input) {
        String[] offerLine = input.split(" ");

        if (offerLine.length < 9) return null;

        return new Offer(
                    offerLine[0], offerLine[1], offerLine[2],
                    Double.parseDouble(offerLine[3]), offerLine[4],
                    Integer.parseInt(offerLine[5]),
                    Integer.parseInt(offerLine[6]),
                    Boolean.parseBoolean(offerLine[7]),
                    Boolean.parseBoolean(offerLine[8])
            );
    }
}

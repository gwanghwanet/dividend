package com.zerobase.scraper;

import com.zerobase.model.Company;
import com.zerobase.model.ScrapedResult;

public class NeverFinanceScraper implements Scraper {

    @Override
    public Company scrapCompanyByTicker(String ticker) {
        return null;
    }

    @Override
    public ScrapedResult scrap(Company company) {
        return null;
    }
}

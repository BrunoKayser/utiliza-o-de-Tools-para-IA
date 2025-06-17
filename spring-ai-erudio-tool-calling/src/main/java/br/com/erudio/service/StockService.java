package br.com.erudio.service;

import br.com.erudio.api.DailyStockData;
import br.com.erudio.api.StockData;
import br.com.erudio.api.StockRequest;
import br.com.erudio.api.StockResponse;
import br.com.erudio.settings.APISettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.function.Function;

public class StockService implements Function<StockRequest, StockResponse> {

    private static final Logger logger = LoggerFactory.getLogger(StockService.class);

    private RestTemplate restTemplate;

    @Value("${TWELVE_DATA_API_KEY:none}")
    private String apiKey;

    public StockService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public StockResponse apply(StockRequest stockRequest) {

        var url = String.format("%s?apikey=%s&interval=1day&symbol=%s&outputsize=1&format=JSON",
                APISettings.TWELVE_DATE_BASE_URL, apiKey, stockRequest.company());

        var data = restTemplate.getForObject(url, StockData.class);

        var latestData = data.getValues().get(0);

        return new StockResponse(Float.parseFloat(latestData.getClose()));
    }
}

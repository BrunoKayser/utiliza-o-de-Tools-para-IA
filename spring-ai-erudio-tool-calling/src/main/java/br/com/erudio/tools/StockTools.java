package br.com.erudio.tools;

import br.com.erudio.api.*;
import br.com.erudio.service.StockService;
import br.com.erudio.settings.APISettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class StockTools {

    private static final Logger logger = LoggerFactory.getLogger(StockTools.class);

    @Value("${TWELVE_DATA_API_KEY:none}")
    private String apiKey;

    private RestTemplate restTemplate;

    public StockTools(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Tool para a IA usar para consultar os preços das ações da empresa
     * @param company empresa a ser consultada o preço da ação
     * @return Objeto com o preço da ação
     */
    @Tool(description = "Últimos preços das ações")
    public StockResponse getLatestStockPrices(@ToolParam(description = "Nome da empresa") String company) {

        logger.info("Consulta preço das ações para empresa {}", company);

        var url = String.format("%s?apikey=%s&interval=1day&symbol=%s&outputsize=1&format=JSON",
                APISettings.TWELVE_DATE_BASE_URL, apiKey, company);

        var data = restTemplate.getForObject(url, StockData.class);

        var latestData = data.getValues().get(0);

        logger.info("Consulta de preço das ações ({}) -> {}", company, latestData);

        return new StockResponse(Float.parseFloat(latestData.getClose()));
    }

    /**
     * Tool para a IA usar para consultar o histórico dos preços das ações da empresa
     * @param company empresa a ser consultada o preço da ação
     * @param days Quantidade em dias para busca do histórico, a relação é 1x1, então days = 3 significa trazer os 3 últimos dias
     * @return Retorna uma lista contendo as informações de preço das ações.
     */
    @Tool(description = "Histórico dos Últimos preços das ações")
    public List<DailyShareQuote> getHistoricalStockPrices(
            @ToolParam(description = "Nome da empresa") String company,
            @ToolParam(description = "pesquisar dias anteriores") int days) {

        logger.info("Consulta histórico preço das ações para empresa {} para os dias {}", company, days);

        var url = String.format("%s?apikey=%s&interval=1day&symbol=%s&outputsize=%s&format=JSON",
                APISettings.TWELVE_DATE_BASE_URL, apiKey, company, days);

        var data = restTemplate.getForObject(url, StockData.class);

        return data.getValues()
                .stream()
                .map(dailyStockData -> new DailyShareQuote(company, Float.parseFloat(dailyStockData.getClose()), dailyStockData.getDateTime()))
                .toList();
    }

}

package br.com.erudio.config;

import br.com.erudio.api.StockRequest;
import br.com.erudio.api.StockResponse;
import br.com.erudio.api.WalletResponse;
import br.com.erudio.repositories.WalletRepository;
import br.com.erudio.service.StockService;
import br.com.erudio.service.WalletService;
import br.com.erudio.tools.StockTools;
import br.com.erudio.tools.WalletTools;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.web.client.RestTemplate;

import java.util.function.Function;
import java.util.function.Supplier;

@Configuration
public class WalletConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     *
     * Este bean serve de apoio para o LLM usar para ter maior precisão nas resposta
     * Então através deste bean a LLM poderá chamar essa service para realizar consultas na minha base de dados
     * para obter dados dos quais ela não foi treinada
     */
    @Bean
    //Description("Number of shares for each comapny in my portofolio")
    @Description("Número de ações de cada empresa em meu portfólio")
    public Supplier<WalletResponse> numberOfShares(WalletRepository repository) {
        return new WalletService(repository);
    }

    /**
     *
     * Este bean serve para apoio ao modelo LLM usar para ter maior precisão nas resposta
     * Então através deste bean a LLM poderá chamar essa service para realizar consultas na api de carteira de de ações,
     * afim de obter dados dos quais ela não foi treinada
     */
    @Bean
    //@Description("Latest stock prices")
    @Description("Últimos preços das ações")
    public Function<StockRequest, StockResponse> latestStockPrices() {
        return new StockService(restTemplate());
    }

    @Bean
    public WalletTools walletTools(WalletRepository walletRepository) {
        return new WalletTools(walletRepository);
    }

    @Bean
    public StockTools stockTools() {
       return new StockTools(restTemplate());
    }


}

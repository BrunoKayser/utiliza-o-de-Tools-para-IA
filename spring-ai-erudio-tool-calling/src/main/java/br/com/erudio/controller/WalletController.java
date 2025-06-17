package br.com.erudio.controller;

import br.com.erudio.tools.StockTools;
import br.com.erudio.tools.WalletTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("ai")
public class WalletController {

    private final ChatClient chatClient;
    private final StockTools stockTools;
    private final WalletTools walletTools;

    public WalletController(final ChatClient.Builder chatClientBuilder, StockTools stockTools, WalletTools walletTools) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
        this.stockTools = stockTools;
        this.walletTools = walletTools;
    }

    /**
     * Método para utilizar tools estáticas
     * Ela é estática por que as tools estão fixas
     */
    @GetMapping("/wallet")
    String calculateWalletValue() {
        PromptTemplate template = new PromptTemplate("""
                Qual é o valor corrente da minha carteira em dolares baseada na última cotaçao dos preços de cotação ?
                Para melhorar a leitura, adicione tabelas e quebra de linhas quando julgar necessário.
                """);

        //Aqui esta sendo usado a IA com o auxílio de Tools para fornecer respostas melhores
        //Em toolnames é setado os beans que quero que a IA utilize de auxílio, então preciso setar exatamente o nome do bean no toolNames
        //
        return this.chatClient.prompt(template.create(
                ToolCallingChatOptions
                        .builder()
                        .toolNames("numberOfShares", "latestStockPrices")
                        .build()))
                .call()
                .content();
    }

    /**
     * Método para utilizar tools dinamicamente
     */
    @GetMapping("/wallet/with-tools")
    String calculateWalletValueWithTools() {
        PromptTemplate template = new PromptTemplate("""
                Qual é o valor corrente da minha carteira em dolares baseada na última cotaçao dos preços de cotação ?
                Para melhorar a leitura, adicione tabelas e quebra de linhas quando julgar necessário.
                """);

        //Aqui esta sendo usado a IA com o auxílio de Tools para fornecer respostas melhores
        //Em tools eu insiro oas classes de tools que o algorítimo pode usar e ele identifica qual método dentro de cada tool é o mais ideal a ele
        return this.chatClient.prompt(template.create())
                .tools(stockTools, walletTools)
                .call()
                .content();
    }

    @GetMapping("/highest-day/{days}")
    String calculateHighestWalletValue(@PathVariable int days) {
        PromptTemplate template = new PromptTemplate("""
                Em qual dia durante os últimos {days} minha carteira teve o maior valor em dolares baseado nos preços históricos das ações
                """);

        //Aqui esta sendo usado a IA com o auxílio de Tools para fornecer respostas melhores
        //Em tools eu insiro oas classes de tools que o algorítimo pode usar e ele identifica qual método dentro de cada tool é o mais ideal a ele
        //Nota-se que não estou dizendo qual método da tool deve se usado, porém o algorítimo consegue entender que deve usar o método getHistoricalStockPrices da stockTools
        return this.chatClient.prompt(template.create(Map.of("days", days)))// Precisa passar o map para que o parâmetro days seja adicionado na mensagem do promptTemplate
                .tools(stockTools, walletTools)
                .call()
                .content();
    }
}

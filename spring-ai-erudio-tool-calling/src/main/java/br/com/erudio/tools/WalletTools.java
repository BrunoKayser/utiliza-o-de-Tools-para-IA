package br.com.erudio.tools;

import br.com.erudio.entities.Share;
import br.com.erudio.repositories.WalletRepository;
import org.springframework.ai.tool.annotation.Tool;

import java.util.List;

public class WalletTools {

    private final WalletRepository repository;

    public WalletTools(final WalletRepository repository) {
        this.repository = repository;
    }

    //Essa anotação que o spring usa para dizer a IA que ela pode consultar esse método para alguma dúvida complementar na geração da resposta
    // Precisa criar o Bean para a utilizar essa tool
    @Tool(description = "Números de ações para cada compania na minha carteira")
    public List<Share> getNumberOfShares() {
        return repository.findAll();
    }

}

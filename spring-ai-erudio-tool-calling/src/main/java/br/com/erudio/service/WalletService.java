package br.com.erudio.service;

import br.com.erudio.api.WalletResponse;
import br.com.erudio.repositories.WalletRepository;

import java.util.function.Supplier;

public class WalletService implements Supplier<WalletResponse> {

    private WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public WalletResponse get() {
        var shares = walletRepository.findAll();
        return new WalletResponse(shares);
    }
}

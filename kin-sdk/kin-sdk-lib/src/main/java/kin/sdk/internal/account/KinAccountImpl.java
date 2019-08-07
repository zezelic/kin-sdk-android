package kin.sdk.internal.account;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import kin.base.KeyPair;
import kin.sdk.*;
import kin.sdk.exception.AccountDeletedException;
import kin.sdk.exception.CryptoException;
import kin.sdk.exception.OperationFailedException;
import kin.sdk.internal.backuprestore.BackupRestore;
import kin.sdk.internal.blockchain.AccountInfoRetriever;
import kin.sdk.internal.blockchain.TransactionSender;
import kin.sdk.internal.blockchain.events.BlockchainEvents;
import kin.sdk.internal.blockchain.events.BlockchainEventsCreator;
import kin.sdk.transactiondata.PaymentTransaction;

import java.math.BigDecimal;


public final class KinAccountImpl extends AbstractKinAccount {

    private final KeyPair account;
    private final BackupRestore backupRestore;
    private final TransactionSender transactionSender;
    private final AccountInfoRetriever accountInfoRetriever;
    private final BlockchainEvents blockchainEvents;
    private boolean isDeleted = false;

    public KinAccountImpl(KeyPair account, BackupRestore backupRestore, TransactionSender transactionSender,
                          AccountInfoRetriever accountInfoRetriever, BlockchainEventsCreator blockchainEventsCreator) {
        this.account = account;
        this.backupRestore = backupRestore;
        this.transactionSender = transactionSender;
        this.accountInfoRetriever = accountInfoRetriever;
        this.blockchainEvents = blockchainEventsCreator.create(account.getAccountId());
    }

    @Override
    public String getPublicAddress() {
        if (!isDeleted) {
            return account.getAccountId();
        }
        return null;
    }

    @Override
    public PaymentTransaction buildTransactionSync(@NonNull String publicAddress, @NonNull BigDecimal amount,
                                                   int fee) throws OperationFailedException {
        checkValidAccount();
        return transactionSender.buildTransaction(account, publicAddress, amount, fee);
    }

    @Override
    public PaymentTransaction buildTransactionSync(@NonNull String publicAddress, @NonNull BigDecimal amount,
                                                   int fee, @Nullable String memo) throws OperationFailedException {
        checkValidAccount();
        return transactionSender.buildTransaction(account, publicAddress, amount, fee, memo);
    }

    @NonNull
    @Override
    public TransactionId sendTransactionSync(PaymentTransaction transaction) throws OperationFailedException {
        checkValidAccount();
        return transactionSender.sendTransaction(transaction);
    }

    @NonNull
    @Override
    public TransactionId sendWhitelistTransactionSync(String whitelist) throws OperationFailedException {
        checkValidAccount();
        return transactionSender.sendWhitelistTransaction(whitelist);
    }

    @NonNull
    @Override
    public Balance getBalanceSync() throws OperationFailedException {
        checkValidAccount();
        return accountInfoRetriever.getBalance(account.getAccountId());
    }

    @Override
    public int getStatusSync() throws OperationFailedException {
        checkValidAccount();
        return accountInfoRetriever.getStatus(account.getAccountId());
    }

    @Override
    public ListenerRegistration addBalanceListener(@NonNull EventListener<Balance> listener) {
        return blockchainEvents.addBalanceListener(listener);
    }

    @Override
    public ListenerRegistration addPaymentListener(@NonNull EventListener<PaymentInfo> listener) {
        return blockchainEvents.addPaymentListener(listener);
    }

    @Override
    public ListenerRegistration addAccountCreationListener(EventListener<Void> listener) {
        return blockchainEvents.addAccountCreationListener(listener);
    }

    @Override
    public String export(@NonNull String passphrase) throws CryptoException {
        return backupRestore.exportWallet(account, passphrase);
    }

    public KeyPair getKeyPair() {
        return account;
    }

    public void markAsDeleted() {
        isDeleted = true;
    }

    private void checkValidAccount() throws AccountDeletedException {
        if (isDeleted) {
            throw new AccountDeletedException();
        }
    }

}

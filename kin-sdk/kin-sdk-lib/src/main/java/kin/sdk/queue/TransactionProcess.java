package kin.sdk.queue;

import android.support.annotation.Nullable;
import kin.sdk.TransactionId;
import kin.sdk.transactiondata.Transaction;

import java.util.List;

/**
 * This class can be used to access both generated transaction and all of its associated PendingPayments.
 */
public interface TransactionProcess {

    /**
     * @return a transaction that consist of the list of pending payments.
     */
    Transaction transaction();

    /**
     * @return a transaction that consist of the list of pending payments.
     * Also add a memo to that transaction
     */
    Transaction transaction(String memo);

    /**
     * @return the list of pending payment that the current transaction consist of.
     * Can be null or empty.
     */
    @Nullable
    List<PendingPayment> payments();

    /**
     * Send the transaction with a Transaction object
     *
     * @param transaction the Transaction object to send
     * @return the transaction id.
     */
    TransactionId send(Transaction transaction);

    /**
     * Send the transaction with a whitelisted payload
     *
     * @param whitelistPayload the whitelist payload
     * @return the transaction id.
     */
    TransactionId send(String whitelistPayload);
}
package kin.sdk;

import android.text.TextUtils;
import kin.base.KeyPair;
import kin.base.Memo;
import kin.base.Operation;
import kin.base.TimeBounds;
import kin.base.Transaction;
import kin.base.Transaction.Builder;
import kin.base.TransactionBuilderAccount;

public class TransactionBuilder {

    private final Builder builder;
    private final KeyPair account;
    private final String appId;
    private String memo;

    /**
     * Construct a new transaction builder.
     *
     * @param account The source account for this transaction. This account is the account
     * @param sourceAccount The source account for this transaction. This account is the account who will use a sequence
     * number. When build() is called, the account object's sequence number will be incremented.
     * @param appId is a 3-4 character string which is added to each transaction memo to identify your application.
     *      * appId must contain only digits and upper and/or lower case letters. String length must be 3 or 4.
     */
    TransactionBuilder(KeyPair account, TransactionBuilderAccount sourceAccount, String appId) {
        this.account = account;
        builder = new Builder(sourceAccount);
        this.appId = appId;
    }

    public int getOperationsCount() {
        return builder.getOperationsCount();
    }

    /**
     * Adds a new operation to this transaction.
     *
     * @return Builder object so you can chain methods.
     * @see Operation
     */
    public TransactionBuilder addOperation(Operation operation) {
        builder.addOperation(operation);
        return this;
    }

    /**
     * Each transaction sets a fee that is paid by the source account.
     * If this fee is below the network minimum the transaction will fail.
     * The more operations in the transaction, the greater the required fee.
     * @param fee this transaction fee
     * @return Builder object so you can chain methods.
     */
    public TransactionBuilder setFee(int fee) {
        builder.addFee(fee);
        return this;
    }

    /**
     * Adds a memo to this transaction.
     * @param memo It's an optional parameter and should contain extra information
     *
     * @return Builder object so you can chain methods.
     * @see Memo
     */
    public TransactionBuilder setMemo(String memo) {
        this.memo = memo;
        return this;
    }

    /**
     * Adds a time-bounds to this transaction.
     *
     * @return Builder object so you can chain methods.
     * @see TimeBounds
     */
    public TransactionBuilder setTimeBounds(TimeBounds timeBounds) {
        builder.addTimeBounds(timeBounds);
        return this;
    }

    /**
     * Builds a transaction. It will increment sequence number of the source account.
     */
    public RawTransaction build() {
        Utils.validateMemo(memo);
        builder.addMemo(Memo.text(Utils.addAppIdToMemo(appId, TextUtils.isEmpty(memo) ? "" : memo)));

        Transaction baseTransaction = builder.build();
        if (baseTransaction.getFee() < 0) {
            throw new IllegalArgumentException("Fee can't be negative");
        }
        baseTransaction.sign(account);
        return new RawTransaction(baseTransaction);
    }


}
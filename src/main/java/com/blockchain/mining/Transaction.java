package com.blockchain.mining;

import java.util.UUID;

/** Represents single money transaction between 2 accounts */
public class Transaction {
  private AccountName fromAccount;
  private AccountName toAccount;
  private Integer money;
  private UUID transactionId;
  private TransactionStatus status;
  private int fee;

  public Transaction(AccountName fromAccount, AccountName toAccount, Integer money, int fee) {
    this.fromAccount = fromAccount;
    this.toAccount = toAccount;
    this.money = money;
    this.transactionId = UUID.randomUUID();
    this.status = TransactionStatus.UNCONFIRMED;
    this.fee = fee;
  }

  public AccountName getFromAccount() {
    return fromAccount;
  }

  public AccountName getToAccount() {
    return toAccount;
  }

  public Integer getMoney() {
    return money;
  }

  public UUID getTransactionId() {
    return transactionId;
  }

  public TransactionStatus getStatus() {
    return status;
  }

  public void setStatus(TransactionStatus status) {
    this.status = status;
  }

  public int getFee() {
    return fee;
  }

  @Override
  public String toString() {
    return "Transaction{"
        + "fromAccount="
        + fromAccount
        + ",toAccount="
        + toAccount
        + ",money="
        + money
        + ",fee="
        + fee
        + '}';
  }
}

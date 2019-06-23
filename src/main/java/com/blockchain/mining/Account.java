package com.blockchain.mining;

public class Account {
  private final AccountName accountName;
  private final BitCoinEngine bitCoinEngine;

  public Account(AccountName accountname, BitCoinEngine bitCoinEngine) {
    this.accountName = accountname;
    this.bitCoinEngine = bitCoinEngine;
  }

  public void sendBitcoin(AccountName toAccount, Integer amount, int fee) {
    System.out.println(this.accountName + " sending " + amount + " to " + toAccount);
    if (bitCoinEngine.getUnconfirmedTrasactions().stream()
        .anyMatch(
            t ->
                t.getStatus() == TransactionStatus.UNCONFIRMED
                    && t.getFromAccount() == accountName)) {
      System.out.println("Found unconfirmed transaction, ignore");
      return;
    }
    // Validate transaction
    if (amount > Utils.getCurrentBalanceForAccount(accountName, bitCoinEngine.getBlockChain())) {
      System.out.println("There is not enough balance, ignore");
      return;
    }
    Transaction transaction = new Transaction(accountName, toAccount, amount, fee);
    bitCoinEngine.addTransaction(transaction);
  }

  public void mineBlock(int difficulty) throws InterruptedException {
    synchronized (bitCoinEngine) {
      if (Utils.isEmpty(bitCoinEngine.getBlockChain())) {
        this.bitCoinEngine.wait();
      }
    }
    Block blockToBeMinned = bitCoinEngine.getBlockToBeMinned();
    if (blockToBeMinned != null && !blockToBeMinned.isBlockdMined().get()) {
      System.out.println(
          this.accountName
              + "-- Started minning block '"
              + blockToBeMinned.getId()
              + "' with difficulty +"
              + difficulty
              + "");

      if (blockToBeMinned.mineBlock(difficulty, accountName)) {
        System.out.println("|--------------------------------------------|");
        System.out.println(
            "| "
                + this.accountName
                + "-- finished minning block '"
                + blockToBeMinned.getId()
                + "'     |");
        System.out.println(
            "| "
                + this.accountName
                + "-- adding Block '"
                + blockToBeMinned.getId()
                + "' to BlockChain |");
        System.out.println("|--------------------------------------------|");
        bitCoinEngine.addBlockToChain();
        // Confirm the transactions
      } else {
        System.out.println(this.accountName + " failed to mine " + blockToBeMinned.getId());
      }

    } else {
      System.out.println(this.accountName + "-- Waiting for Block to be created..");
    }
  }
}

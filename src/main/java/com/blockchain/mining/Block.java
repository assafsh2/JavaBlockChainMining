package com.blockchain.mining;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Block {
  private String currentHash;
  private String previousHash;
  private AtomicBoolean isBlockdMined;
  private AccountName whoMinnedMe;
  private List<Transaction> transactions; // our data will be a simple message.
  private long timeStamp; // as number of milliseconds since 1/1/1970.
  private int nonce;
  private int id;
  private Object obj = new Object();

  // Block Constructor.
  public Block(int id, List<Transaction> transactions, String previousHash) {
    this.transactions = new ArrayList<>(transactions);
    this.id = id;
    this.previousHash = previousHash;
    this.timeStamp = new Date().getTime();
    this.currentHash = calculateHash(0);
    this.isBlockdMined = new AtomicBoolean(false);
  }

  public String calculateHash(int nonce) {
    return Utils.applySha256(
        previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + transactions);
  }

  public String calculateHash() {
    return calculateHash(nonce);
  }

  public boolean mineBlock(int difficulty, AccountName accountMinningBlock) {
    String target = new String(new char[difficulty]).replace('\0', '0');
    String tmpCurrentHash = currentHash;
    int tmpNonce = 0;
    while (!tmpCurrentHash.substring(0, difficulty).equals(target) && !isBlockdMined.get()) {
      tmpNonce++;
      tmpCurrentHash = calculateHash(tmpNonce);
    }

    // Critical section, only one account can win the block
    synchronized (obj) {
      if (tmpCurrentHash.substring(0, difficulty).equals(target) && !isBlockdMined.get()) {
        System.out.println(
            String.format("Account %s succeed to mine block %s ", accountMinningBlock, id));
        isBlockdMined.set(true);
        whoMinnedMe = accountMinningBlock;
        currentHash = tmpCurrentHash;
        nonce = tmpNonce;
        return true;
      }
    }
    System.out.print(String.format("Account %s failed to mine block %s ", accountMinningBlock, id));
    return false;
  }

  @Override
  public boolean equals(Object obj) {
    Block other = (Block) obj;
    if (currentHash.equals(other.currentHash)) return true;
    return false;
  }

  public void confirmAllTransitions() {
    transactions.forEach(transaction -> transaction.setStatus(TransactionStatus.CONFIRMED));
  }

  public List<Transaction> getTransactions() {
    return new ArrayList<>(transactions);
  }

  public int getId() {
    return id;
  }

  public AccountName getWhoMinnedMe() {
    return this.whoMinnedMe;
  }

  public void setWhoMinnedMe(AccountName accountName) {
    this.whoMinnedMe = accountName;
  }

  public AtomicBoolean isBlockdMined() {
    return this.isBlockdMined;
  }

  public String getCurrentHash() {
    return currentHash;
  }

  public String getPreviousHash() {
    return previousHash;
  }

  public AtomicBoolean getIsBlockdMined() {
    return isBlockdMined;
  }

  public Block copy() {
    Block block = new Block(id, new ArrayList<>(this.transactions), this.previousHash);
    block.whoMinnedMe = this.whoMinnedMe;
    block.currentHash = this.currentHash;
    block.isBlockdMined = this.isBlockdMined;
    return block;
  }

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("Block id=").append(id).append(" whoMinnedMe=").append(whoMinnedMe);
    transactions.forEach(transaction -> sb.append("\n").append(transaction));
    return sb.toString();
  }
}

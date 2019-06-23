package com.blockchain.mining;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// https://github.com/hunaidee007
public class BitCoinEngine {
  private Block blockToBeMinned;
  private List<Transaction> unconfirmedTrasactions =
      Collections.synchronizedList(new ArrayList<>());
  private List<Block> blockChain = new ArrayList<Block>();

  public BitCoinEngine() {}

  public void addTransaction(Transaction transaction) {
    unconfirmedTrasactions.add(transaction);
  }

  public void readyToMineNewBlock() {
    synchronized (this) {
      try {
        System.out.println(
            " readyToMineNewBlock  unconfirmedTrasactions size: " + unconfirmedTrasactions.size());
        System.out.println(" readyToMineNewBlock blockChain size: " + blockChain.size());
        if (blockChain.size() == 0) {
          blockToBeMinned = new Block(blockChain.size() + 1, unconfirmedTrasactions, "0");
        } else {
          if (blockToBeMinned == null) {
            blockToBeMinned =
                new Block(
                    blockChain.size() + 1,
                    unconfirmedTrasactions,
                    blockChain.get(blockChain.size() - 1).getCurrentHash());
          }
        }
        unconfirmedTrasactions.clear();
        System.out.println("-- Created block '" + blockToBeMinned.getId() + "' to be mined");
        this.notifyAll();

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public List<Block> getBlockChain() {
    return new ArrayList<>(blockChain);
  }

  public void addBlockToChain() {
    blockChain.add(blockToBeMinned.copy());
    blockToBeMinned.confirmAllTransitions();
    blockToBeMinned = null;
  }

  public List<Transaction> getUnconfirmedTrasactions() {
    return unconfirmedTrasactions;
  }

  public Block getBlockToBeMinned() {
    return blockToBeMinned;
  }

  public boolean isTransactionValid(Transaction transaction) {
    // TODO implements
    return true;
  }
}

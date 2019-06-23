package com.blockchain.mining;

import java.util.List;

public enum AccountName {
  A,
  B,
  C,
  D;
  //  E;

  public String toString(List<Block> blockChain) {
    return "AccountName :"
        + this.name()
        + " balance is :"
        + Utils.getCurrentBalanceForAccount(this, blockChain);
  }
}

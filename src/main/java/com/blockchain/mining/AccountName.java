package com.blockchain.mining;

import java.util.List;

public enum AccountName {
  A,
  B,
  C,
  D,
  E;

  public String toString(List<Block> blockChain) {
    return String.format(
        "AccountName :%s balance is :%d",
        this.name(), Utils.getCurrentBalanceForAccount(this, blockChain));
  }
}

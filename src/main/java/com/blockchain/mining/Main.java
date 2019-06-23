package com.blockchain.mining;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Main {
  public static void main(String[] args) {
    BitCoinEngine bitCoinEngine = new BitCoinEngine();
    Map<AccountName, Account> accounts =
        Arrays.stream(AccountName.values())
            .collect(Collectors.toMap(a -> a, a -> new Account(a, bitCoinEngine)));

    try (SetupEngine setupEngine = SetupEngine.create(bitCoinEngine, accounts, 3)) {
      setupEngine.run();
      TimeUnit timeUnit = TimeUnit.SECONDS;
      timeUnit.sleep(30);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    if (!Utils.isBlockChainValid(bitCoinEngine.getBlockChain())) {
      System.out.println("The blockchain is not valid");
      System.exit(-1);
    }
    StringBuilder sb = new StringBuilder();
    sb.append("The current blockchain is:");
    bitCoinEngine.getBlockChain().forEach(sb::append);

    sb.append("\n").append("The current balance for each account is:").append("\n");
    Arrays.stream(AccountName.values())
        .forEach(
            accountName ->
                sb.append(accountName.toString(bitCoinEngine.getBlockChain())).append("\n"));

    System.out.println(sb.toString());
  }
}

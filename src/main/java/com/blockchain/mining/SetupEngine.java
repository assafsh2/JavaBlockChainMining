package com.blockchain.mining;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SetupEngine implements AutoCloseable {
  private final BitCoinEngine bitCoinEngine;
  private final Map<AccountName, Account> accounts;
  private final int difficulty;
  private List<ScheduledExecutorService> executorServiceList;

  private SetupEngine(
      BitCoinEngine bitCoinEngine, Map<AccountName, Account> accounts, int difficulty) {
    this.bitCoinEngine = bitCoinEngine;
    this.accounts = accounts;
    this.difficulty = difficulty;
    this.executorServiceList = new ArrayList<>();
  }

  public static SetupEngine create(
      BitCoinEngine bitCoinEngine, Map<AccountName, Account> accounts, int difficulty) {
    return new SetupEngine(bitCoinEngine, accounts, difficulty);
  }

  public void run() {
    executeInFixedRate(bitCoinEngine::readyToMineNewBlock, 5, 5);
    executeInFixedRate(
        () -> {
          try {
            accounts.get(AccountName.A).mineBlock(difficulty);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        },
        3,
        2);

    executeInFixedRate(
        () -> {
          try {
            accounts.get(AccountName.B).mineBlock(difficulty);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        },
        2,
        2);

    executeInFixedRate(
        () -> {
          try {
            accounts.get(AccountName.C).mineBlock(difficulty);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        },
        2,
        3);

    executeInFixedRate(
        () ->
            accounts.forEach(
                (accountName, account) ->
                    account.sendMoney(
                        Utils.getRandomAccountName(accountName),
                        Utils.randomInt(),
                        Utils.randomInt())),
        2,
        2);

    executeInFixedRate(() -> Utils.isBlockChainValid(bitCoinEngine.getBlockChain()), 10, 5);
  }

  private void executeInFixedRate(Runnable command, long initialDelay, long delay) {
    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    scheduledExecutorService.scheduleWithFixedDelay(command, initialDelay, delay, TimeUnit.SECONDS);
    executorServiceList.add(scheduledExecutorService);
  }

  @Override
  public void close() {
    executorServiceList.forEach(scheduledExecutorService -> scheduledExecutorService.shutdownNow());
  }
}

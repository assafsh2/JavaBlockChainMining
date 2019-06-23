package com.blockchain.mining;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import javafx.util.Pair;

public final class Utils {
  public static final int INITIAL_BALANCE = 100;

  private Utils() {}

  // Applies Sha256 to a string and returns the result.
  public static String applySha256(String input) {

    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");

      // Applies sha256 to our input,
      byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

      StringBuffer hexString = new StringBuffer(); // This will contain hash as hexidecimal
      for (int i = 0; i < hash.length; i++) {
        String hex = Integer.toHexString(0xff & hash[i]);
        if (hex.length() == 1) hexString.append('0');
        hexString.append(hex);
      }
      return hexString.toString();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static AccountName getRandomAccountName(AccountName myAccountName) {
    List<AccountName> accountNames =
        Arrays.asList(AccountName.values()).stream()
            .filter(accountName -> accountName != myAccountName)
            .collect(Collectors.toList());

    Collections.shuffle(accountNames);
    return accountNames.get(0);
  }

  public static boolean isBlockChainValid(List<Block> blockChain) {
    if (blockChain.size() > 1) {
      for (int i = 1; i <= blockChain.size() - 1; i++) {
        Block currentBlock = blockChain.get(i - 1);
        Block nextBlock = blockChain.get(i);
        if (!(nextBlock.getPreviousHash().equals(currentBlock.getCurrentHash()))
            && currentBlock.getCurrentHash().equals(currentBlock.calculateHash())) {
          System.out.println(
              String.format("CHAIN VALIDATION chain size %d is not valid", blockChain.size()));
          return false;
        }
      }
    }
    System.out.println(String.format("CHAIN VALIDATION chain size %d is valid", blockChain.size()));
    return true;
  }

  public static <T> boolean isEmpty(Collection<T> collection) {
    return collection == null || collection.isEmpty();
  }

  public static int randomInt() {
    return ThreadLocalRandom.current().nextInt(10, 100);
  }

  public static int getCurrentBalanceForAccount(AccountName accountName, List<Block> blockChain) {
    return INITIAL_BALANCE
        + blockChain.stream()
            .flatMap(
                block ->
                    block.getTransactions().stream()
                        .filter(Utils::test)
                        .map(transaction -> new Pair<>(block.getWhoMinnedMe(), transaction)))
            .mapToInt((Pair<AccountName, Transaction> pair) -> getAmount(accountName, pair))
            .sum();
  }

  private static int getFactor(Transaction transaction, AccountName accountName) {
    if (transaction.getToAccount() != accountName && transaction.getFromAccount() != accountName) {
      return 0;
    } else if (transaction.getToAccount() == accountName) {
      return 1;
    }
    return -1;
  }

  private static int getAmount(AccountName accountName, Pair<AccountName, Transaction> pair) {
    return (pair.getValue().getMoney() * getFactor(pair.getValue(), accountName))
        + getFee(accountName, pair.getKey(), pair.getValue().getFee());
  }

  private static int getFee(AccountName accountName, AccountName whoMinnedMe, int fee) {
    if (accountName == whoMinnedMe) {
      return fee;
    }
    return 0;
  }

  private static boolean test(Transaction transaction) {
    return transaction.getStatus() == TransactionStatus.CONFIRMED;
  }
}

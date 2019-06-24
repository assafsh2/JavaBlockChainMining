package com.blockchain.mining;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class BlockchainTest {
    private static void accept(Transaction transaction) {
        transaction.setStatus(TransactionStatus.CONFIRMED);
    }
    @Test
    public void testBalancePerAccount() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(AccountName.A, AccountName.B, 30,50));
        transactions.add(new Transaction(AccountName.A, AccountName.B, 20,50));
        transactions.forEach(BlockchainTest::accept);
        Block block = new Block(1, transactions, null);
        block.setWhoMinnedMe(AccountName.B);

        Assert.assertEquals(150 + Utils.INITIAL_BALANCE,
                Utils.getCurrentBalanceForAccount(AccountName.B, Arrays.asList(block)));

        Assert.assertEquals( Utils.INITIAL_BALANCE - 50,
                Utils.getCurrentBalanceForAccount(AccountName.A, Arrays.asList(block)));
    }

    @Test
    public void testBalancePerAccount2() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(AccountName.A, AccountName.B, 30,50));
        transactions.add(new Transaction(AccountName.A, AccountName.B, 20,50));
        transactions.forEach(BlockchainTest::accept);
        Block block = new Block(1, transactions, null);
        block.setWhoMinnedMe(AccountName.B);

        Assert.assertEquals(150 + Utils.INITIAL_BALANCE,
                Utils.getCurrentBalanceForAccount(AccountName.B, Arrays.asList(block)));

        Assert.assertEquals( Utils.INITIAL_BALANCE - 50,
                Utils.getCurrentBalanceForAccount(AccountName.A, Arrays.asList(block)));
    }
}
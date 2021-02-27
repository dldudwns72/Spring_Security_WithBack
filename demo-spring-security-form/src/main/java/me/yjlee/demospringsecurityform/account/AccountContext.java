package me.yjlee.demospringsecurityform.account;

public class AccountContext {
    // ThreadLocal 생성
    private static final ThreadLocal<Account> ACCOUNT_THREAD_LOCAL
            = new ThreadLocal<>();

    public static void setAccount(Account account){
        ACCOUNT_THREAD_LOCAL.set(account);
    }

    public static Account getAccount(){
        return ACCOUNT_THREAD_LOCAL.get();
    }
}

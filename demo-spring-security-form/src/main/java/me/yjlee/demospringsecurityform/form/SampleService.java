package me.yjlee.demospringsecurityform.form;

import me.yjlee.demospringsecurityform.account.Account;
import me.yjlee.demospringsecurityform.account.AccountContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SampleService {
    public void dashboard(){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Object principal =  authentication.getPrincipal();
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        Object credentials = authentication.getCredentials();
//        boolean authenticated = authentication.isAuthenticated();

//        Account account = AccountContext.getAccount();

        // 인증이 된 사용자의 정보를 SecurityContextHolder 를 사용하여 ThreadLocal 에 저장할 수있다
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        System.out.println("--------------------------");
        System.out.println(userDetails.getUsername());

    }
}

package me.yjlee.demospringsecurityform.account;


import org.junit.runner.RunWith;
//import org.junit.jupiter.api.Test; // Junit5에서 사용되는 Test
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

//Junit5 에서는 @ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithAnonymousUser // 어노테이션을 이용한 anonymous일 경우 설정
    public void index_anonymous() throws Exception{
        mockMvc.perform(get("/"))//.with(anonymous())) // User 정보가 없을 떄
                .andDo(print())
                .andExpect(status().isOk());
    }

    // user가 view를 볼 떄 테스트
    @Test
    @WithUser
    public void index_user() throws Exception{
        // 현재 user 정보를 입력한데로 "/" (인덱스) 페이지에 접속하면 어떻게 될것인지에 대한 테스트
        mockMvc.perform(get("/"))//.with(user("YoungJun").roles("USER"))) // with(user 정보 입력) 가상의 User 등록
                .andDo(print())
                .andExpect(status().isOk());

    }

    // ADMIN 페이지에 User 권한을 가진 유저가 접속
    @Test
    public void admin_user() throws Exception{
        mockMvc.perform(get("/admin").with(user("YoungJun").roles("User")))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    // ADMIN 페이지에 ADMIN 권한에 정상 접속
    @Test
    public void admin_admin() throws Exception{
        mockMvc.perform(get("/admin").with(user("admin").roles("ADMIN")))
                .andDo(print())
                .andExpect(status().isOk());
    }


}

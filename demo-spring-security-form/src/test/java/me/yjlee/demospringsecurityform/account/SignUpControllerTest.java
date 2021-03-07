package me.yjlee.demospringsecurityform.account;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SignUpControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void signUpForm() throws Exception{
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("_csrf")));

    }
    @Test
    public void processSignUp() throws Exception{
        mockMvc.perform(post("/signup")
                        .param("username","lee")
                        .param("password","1234")
                        .with(csrf())) // 적절한 csrf 토큰 전달
                .andDo(print())
                .andExpect(status().is3xxRedirection())
        ;

    }


}

package me.yjlee.demospringsecurityform.form;

import me.yjlee.demospringsecurityform.account.AccountContext;
import me.yjlee.demospringsecurityform.account.AccountRepository;
import me.yjlee.demospringsecurityform.common.SecurityLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.concurrent.Callable;

@Controller
public class SampleController {

//    principal 사용자 정보를 담고있는 객체, 값이 없을 경우 null
//    spring-boot-starter-security maven 설정 시 최초 화면이 로그인으로 나오며 기본 아이디 : 'user' 비번 : "컨솔창에 나오는 GUID"
//    모든 요청은 인증을 필요로 함

    @Autowired
    SampleService sampleService;

    @Autowired
    AccountRepository accountRepository;


    @GetMapping("/")
    public String index(Model model, Principal principal){
        if(principal == null) {
            model.addAttribute("message","Hello Spring Security");
        }else{
            model.addAttribute("message","Hello, "+ principal.getName());
        }

        // view 이름을 리턴하면 spring WebMvc패턴에 따라서 templates 하위에 있는 view페이지를 보여준다.
        return "index";
    }

    // 아무런 제한 없이 접근 가능한 핸들러
    @GetMapping("/info")
    public String info(Model model){
        model.addAttribute("message","Info");
        return "info";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal){
        model.addAttribute("message","Hello, " + principal.getName());
        AccountContext.setAccount(accountRepository.findByUsername(principal.getName()));
        sampleService.dashboard();
        return "dashboard";
    }

    @GetMapping("/admin")
    public String admin(Model model,Principal principal){
        model.addAttribute("message","Hello Admin, " + principal.getName());
        return "admin";
    }

    @GetMapping("/user")
    public String user(Model model,Principal principal){
        model.addAttribute("message","Hello User, " + principal.getName());
        return "user";
    }

    @GetMapping("/async-handler")
    @ResponseBody
    public Callable<String> asyncHandler(){
        SecurityLogger.log("MVC, before async service");
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                SecurityLogger.log("Callable");
                return "Async Handler";
            }
        };
    }

    @GetMapping("/async-service")
    @ResponseBody
    public String asyncService(){
        SecurityLogger.log("MVC, before Async service");
        sampleService.asyncService();
        SecurityLogger.log("MVC after Async service");
        return "Async Service";
    }
}

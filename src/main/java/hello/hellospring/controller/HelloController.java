package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("data", "hello!");
        return "hello";
    }

    @GetMapping("hello-mvc")
    public String helloMvc(@RequestParam(value = "name", required = false) String name, Model model) {
        model.addAttribute("name", name);
        return "hello-template";
    }

    @GetMapping("hello-string")
    @ResponseBody // http의 통신 프로토콜에서 헤더와 바디중 바디부분을 직접 넣어주겠다
    public String helloString(@RequestParam("name") String name) {
        return "Hello" + name;
    }

    @GetMapping("hello-api")
    @ResponseBody
    public Hello helloApi(@RequestParam("name") String name){
        Hello hello = new Hello();

        hello.setName(name);
        return hello;

        /**
         * 객체를 Json으로 바궈주는 라이브러리가 있다
         * 1. Jackson 라이브러리 - 스프링이 기본 탑재 - 범용성 높음
         * 2. gson(구글) 라이브러리
         */
    }

    /**
     * static 사용하면 class 안에서 또 class를 사용할 수 있음
     */
    static class Hello {
        private String name;

        /**
         * java bean 규약
         * 필드 변수는 private이라 외부에서 바로 못 꺼내는데
         * 라이브러리 같은데서 쓰거나 본인이 사용할 때도 get,set메소드 사용한다
         * property 접근 방식
         */

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

package com.mysite.sbb.controller;

import com.mysite.sbb.domain.Question;
import com.mysite.sbb.service.QuestionService;
import com.mysite.sbb.validation.AnswerForm;
import com.mysite.sbb.validation.QuestionForm;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor // final이 붙은 속성을 포함하는 생성자를 자동으로 생성하는 역할,
@Controller
@RequestMapping("/question")
public class QuestionController {

    /** 스프링 의존성 주입 3가지 방식
     * @Autowired - 애너테이션을 적용해 객체 주입
     * 생성자 - 생성자 작성해 객체 주입 <- 권장
     * Setter - 메서드를 통한 객체 주입방식 <- 메서드에 @Autowired 적용
     */
//    private final QuestionRepository questionRepository; // 객체가 자동으로 주입 @RequiredArgsContstructor
    private final QuestionService questionService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page) { 
        // 모델 객체는 템플릿과 자바 클래스간 연결
        // URL에 따라 page가 전달되지 않으면 defaultValut를 0 으로 
        Page<Question> paging = this.questionService.getList(page);
        model.addAttribute("paging", paging);
        return "question_list";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question_detail";
    }

    @GetMapping(value = "/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }

    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult) {

        System.out.println(bindingResult);
        if(bindingResult.hasErrors()) {
            return "question_form";
        }

        this.questionService.create(questionForm.getSubject(), questionForm.getContent());

        return "redirect:/question/list";
    }
}

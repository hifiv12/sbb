package com.mysite.sbb;

import com.mysite.sbb.domain.Answer;
import com.mysite.sbb.domain.Question;
import com.mysite.sbb.repository.AnswerRepository;
import com.mysite.sbb.repository.QuestionRepository;
import com.mysite.sbb.service.QuestionService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// local server가 구동중이면 테스트 에러남. 끄고 해야됨
@SpringBootTest // 해당 클래스가 스프링 부트 테스트 클래스임을 의미,
class SbbApplicationTests {

    @Autowired // 스프링의 DI 기능, 해당 객체를 스프링이 자동으로 생성
    private QuestionRepository questionRepository;
    // 순환 참조의 문제와 같은 이유?로 생성자를 통한 객체 주입방식이 권장
    // 테스트 코드의 경우 생성자를 통한 객체의 주입이 불가해서 사용
    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerRepository answerRepository;

    @Test // 테스트 메서드, JUnit으로 실행, JUnit = 테스트 코드를 실행하기 위해 사용하는 자바의 테스트 프레임워크
    void testJpa() {
        Question q1 = new Question();
        q1.setSubject("What is sbb?");
        q1.setContent("I want to know sbb.");
        q1.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q1);

        Question q2 = new Question();
        q2.setSubject("한글되니?");
        q2.setContent("한글?");
        q2.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q2);
    }

    @Test
    void testJpaAll() {
        List<Question> all = this.questionRepository.findAll();
        assertEquals(2, all.size()); // 기대값과 실제값의 비교

        Question q = all.get(0); // 인덱스가 0인 것을 저장
        assertEquals("What is sbb?", q.getSubject()); // 인덱스 0의 subject를 비교
    }

    @Test
    void testJpaId() {
        // null 처리를 유연하게 처리하기 위해 사용하는 클래스
        // 객체 자체에 값을 보유하기에 바로 NullPointException을 뱉지 않음
        // method
            // 값이 null -> .empty
            // 값이 non null -> .of
            // 일수도 아닐수도 -> ofNullable()
        Optional<Question> oq = this.questionRepository.findById(1);

        if(oq.isPresent()) {
            Question q = oq.get(); // Optional 객체의 .get()
            assertEquals("What is sbb?", q.getSubject());
        }
    }


     // DI에 의해 스프링이 QuestionRepository 객체 생성 <- 프록시 패턴이 사용

    @Test
    void testJpaSubject() {
        Question q = this.questionRepository.findBySubject("What is sbb?");
        assertEquals(1, q.getId());
    }

     // by 이후에 오는 메서드명은 조건값으로 사용
     // 종류
     //    And, Or, Between, LessThan, GreaterthanEqual, Like, In, OrderBy -> SQL 조건과 동일
    @Test
    void testJpaSubjectAndContent() {
        Question q = this.questionRepository.findBySubjectAndContent("What is sbb?", "I want to know sbb.");

        assertEquals(1, q.getId());
    }

    @Test
    void testJpaLike() {
        // sbb% : 로 시작하는 문자열
        // %sbb : 로 끝나는 문자열
        // %sbb% : 포함하는 문자열
        List<Question> questionList = this.questionRepository.findBySubjectLike("%sbb%");
        Question q = questionList.get(0);

        assertEquals("What is sbb?", q.getSubject());
    }

    @Test
    void testJpaUpdate() {
        Optional<Question> oq = this.questionRepository.findById(1);
        assertTrue(oq.isPresent());
        Question q = oq.get();
        q.setSubject("수정된 제목");
        this.questionRepository.save(q); // 수정된 데이터를 저장하기
    }

    @Test
    void testJpaDelete() {
        assertEquals(2, this.questionRepository.count());
        Optional<Question> oq = this.questionRepository.findById(1);
        assertTrue(oq.isPresent());
        Question q = oq.get();
        this.questionRepository.delete(q);
        assertEquals(1, this.questionRepository.count());
    }

    @Test
    void testJpaAnswer() {
        Optional<Question> oq = this.questionRepository.findById(2);
        assertTrue(oq.isPresent());
        Question q = oq.get();

        Answer a = new Answer();
        a.setContent("네 자동으로 생성됩니다.");
        a.setQuestion(q);
        a.setCreateDate(LocalDateTime.now());
        this.answerRepository.save(a);
    }

    @Test
    void testJpaRead() {
        Optional<Answer> oa = this.answerRepository.findById(1);
        assertTrue(oa.isPresent());
        Answer a = oa.get();
        assertEquals(2, a.getQuestion().getId());
    }

    // findById를 호출해서 Question 객체를 조회하고 나면 DB세션이 끊어짐
    // 이후에 실행되는 메서드는 세션이 종료되어 오류 발생
    // 필요한 시점에 데이터를 가져오는 Lazy 방식
    // q 객체를 조회할때 답변 리스트를 모두 가져오는 Eager방식
    // @Transactional을 사용해 메서드가 종료 될때 까지 DB세션 유지
    @Transactional
    @Test
    void testJpaQuestionToAnswer() {
        Optional<Question> oq = this.questionRepository.findById(2);
        assertTrue(oq.isPresent());
        Question q = oq.get();

        List<Answer> answerList = q.getAnswerList();

        assertEquals(1, answerList.size());
        assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());
    }

    @Test
    void testJpaDummy() {

        for(int i=1; i <= 300; i++) {
            String subject = String.format("test data [%03d]", i);
            String content = "test" + i;
            this.questionService.create(subject, content);
        }

    }
}

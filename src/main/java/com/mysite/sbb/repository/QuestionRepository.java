package com.mysite.sbb.repository;

import com.mysite.sbb.domain.Question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> { // 리포지토리의 대상: Question, Integer: PK

    // findBy + 엔티티의 속성명을 쓰면 해당 속성값으로 데이터를 조회
    // application.properties 파일을 수정하면 쿼리 로그를 볼 수 있음
    Question findBySubject(String subject); // JPA가 해당 메서드명을 분석하여 쿼리를 만들고 실행
    Question findBySubjectAndContent(String subject, String content);
    List<Question> findBySubjectLike(String subject);
    Page<Question> findAll(Pageable pageable);
}

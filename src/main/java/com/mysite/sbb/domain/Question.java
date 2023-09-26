package com.mysite.sbb.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter // 데이터베이스와 연결되어 데이터를 자유롭게 변경 될 수 있어 권장되진 않음 > lombok의 @Builder 권장
@Entity // 데이터베이스 테이블과 매핑되는 자바 클래스
public class Question {

    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 독립적인 시퀀스를 생성하여 번호를 증가
    private Integer id;

    @Column(length = 200) // 테이블 컬럼과 컬럼명을 일치, length = 길이,
    private String subject;

    @Column(columnDefinition = "TEXT") // 컬럼의 속성을 정의, 글자수를 제한 없는 경우
    private String content;

    private LocalDateTime createDate; // camelCase가 snake_case로 실제 테이블명으로 반영

    private LocalDateTime modifyDate;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE) // 질문 하나에는 여러 답변, 그래서 질문이 삭제되면 답변 전부를 같이 삭제
    private List<Answer> answerList;
    // Question을 참조한 것이 왜 question으로 명시해야되는걸까? 일반적인 레퍼런스 변수를 쓸때랑 비슷한건가

    @ManyToOne
    private SiteUser author;
}

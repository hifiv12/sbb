## 점프투 스프링부트

  * 질문에 작성자 저장하기
    + QuestionService.create(SiteUser user) -> .setAuthor(user)
    + QuestionController
      - private final UserService userService
      - SiteUser siteUser = this.userService.getUser(pricipal.getName())
      - this.questionService.create(..., siteUser)
  - 로그인이 필요한 메서드
    * 로그아웃 상태에서 질문, 댓글을 등록하면 null
      + 로그인때 생성되는 Principal 객체가 null이기 떄문
      + @PreAuthorize("isauthenticated()") 를 사용
        - 질문이나 댓글 버튼을 누를 시 
        - 로그인 상태라면 등록 진행
        - 로그아웃이라면 로그인 페이지로 이동
    * QuestionController
      + /create get, post -> @PreAuthorize("isAuthenticated")
        - 메소드 실행전에 인증된 사용자만 해당 메소드에 접근할 수 있도록 허용
    * AnswerController
      + /created/{id} post -> @PreAuthorize("isAuthenticated")
    * SecurityConfig
      + @EnableMethodSecurity(prePostEnabled = true)
      + @PreAuthorize, @PostAuthorize를 사용해 메소드 레벨에서 보안규칙을 적용할 수 있게 함 
  - disabled
    * 로그아웃 상태에서 답변 등록을 진행중 버튼을 누르게 되고 로그인 화면으로 감 -> 답변 내용이 사라진다. 
    * question_detail.html

9. 글쓴이 표시
  - 질문 목록
    * question_list.html
      * css 조정 및 th:if, th:text를 추가
      * username  불러오기
  - 질문 상세
    * question_detail.html
      * 위와 마찬가지

10. 수정과 삭제
  - 수정 일시
    * Question, Answer -> private LocalDateTime modifyDate;
  - 질문 수정
    * 질문 수정
      + question_detail.html
        + th:href
        + sec:authorize
        + th:if
        + th:text
    * QuestionController
      + .questionModify()
      + 서비스 조회 > question 객체 저장
      + principal(로그인 정보)와 조회한 객체의 유저네임 비교
      + false -> 메세지 리턴
      + true -> QuestionForm 객체에 값 저장 내려주기 및 url 리턴 
    * question_form.html
      + form.th:action 삭제
        - csrf 값이 자동 생성이 않됨 -> 수동 추가
        - question_form을 가져올 떄 URL를 해당 url에 맞게 post로 보내기 위한 작업
    * QuestionService
      + .modify() 추가
    * QuestionController
      + QuestionForm 검증
      + 로그인 정보와 질문 작성자 검증
      + service.modify()호출하며 수정
      + 이후 url 리턴 
    * 질문 수정 확인
  - 질문 삭제
    * 질문 삭제 버튼
      + question_detail.html
        + th:href
        + sec:authorize
        + th:if
        + th:text
    * 자바스크립트
    * 자바 스크립트 블록
      + layout.html 하단에 추가
      + question_detail.html 하단에 script 태그 추가
    * QuestionService
      + public void delete(Question question) {repo.delete(question)}
    * QuestionController
      + @PreAuthorize("isAuthenticated()")
      + @GetMapping("/delete/{id}")
      + .questionDelete(Principal, @PathVariable Integerid)
    * 질문 삭제 확인
    +
  - 답변 수정
    * 답변 수정 버튼
      + question_detail.html
        - th:href
        - sec:authorize
        - th:if
        - th:text
    * AnswerService
      + .getAnswer(Integer id) return Answer
      + .modify(Answer answer, String content)
    * AnswerController
      + @PreAuthorize
      + @GetMapping
      + answerModify(...)
    * AnswerController
      + @PreAuthorize
      + @GetMapping
      + answerModify(...)
    * answer_form.html
      + th:action이 없는 form
      + hidden 속성의 _csrf.parameterName, _csrf.token을 따로 생성
        - 현재 요청된 URL로 폼 전송을 하기 위한 작업
    * AnswerController
      + post url mapping
      + answerForm 을 통한 인증
      + answer서비스 객체 조회 후 answer 객체 저장
      + 로긴 정보와 게시글 아이디 비교
      + service.modify()
      + 리다이렉트
    * 답변 수정 확인
      + 확인 완료
  - 답변 삭제
    * 답변 삭제 버튼
      + 수정과 동일
    * AnswerService
      + delete(answer)
    * AnswerController
      + 로그인 정보와 uri id 값 넣기
      + answer 객체 조회
      + answer 객체의 유저네임과 로그인 정보 유저네임을 비교
      + 삭제 서비스 실행
      + 리다이렉트
    * 답변 삭제 확인
      + 확인 완료
  - 수정일시 표시하기
    * question_detail.html

13. 마크다운
  - 마크다운 문법
    * 리스트
      + \*, \-, \+, 
    * 강조
      + \*\*강조\*\* 
    * 링크
      + \[link name\]\(link url\)
    * 소스 코드
      + '''
        '''
    * 인용
      + \> 인용
  - 마크다운 설치
    + implementation 'org.commonmark:commonmark:0.21.0'
    + 스프링 부트가 내부적으로 관리하지 않는 라이브러리는 버전 정보가 필요
  - 마크다운 컴포넌트
  - 템플릿에 마크다운 적용
    + question_detail.html
      - tx:utext -> 태그들이 escape 처리되어 그대로 화면에 보여짐
  - 마크다운 확인

14. 검색
  - 검색 기능
    * Specification
    * QuestionRepository
    * QuestionService
    * QuestionController
  - 검색 화면
  - 검색 확인
  - @Query
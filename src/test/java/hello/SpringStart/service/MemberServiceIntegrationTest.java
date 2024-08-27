package hello.SpringStart.service;

import hello.SpringStart.domain.Member;
import hello.SpringStart.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MemberServiceIntegrationTest {

  @Autowired MemberRepository repository;
  @Autowired MemberService service;

  @Test
  void 회원가입() {
    // given
    Member member = new Member();
    member.setName("member");

    // when
    Long saveId = service.join(member);

    // then
    Member findMember = service.findOne(saveId).get();
    assertThat(findMember.getName()).isEqualTo(member.getName());
  }
}

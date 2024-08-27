package hello.SpringStart.service;

import hello.SpringStart.domain.Member;
import hello.SpringStart.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class MemberServiceTest {

  MemoryMemberRepository repository;
  MemberService service;

  @BeforeEach
  void beforeEach() {
    repository  = new MemoryMemberRepository();
    service = new MemberService(repository);
  }

  @AfterEach
  void afterEach() {
    repository.clearStore();
  }

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

  @Test
  void 중복_회원_예외() {
    // given
    Member member1 = new Member();
    member1.setName("member");

    Member member2 = new Member();
    member2.setName("member");

    // when
    service.join(member1);

    // then
    IllegalStateException e = assertThrows(IllegalStateException.class, () -> service.join(member2));
    assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
  }

  @Test
  void findMembers() {
  }

  @Test
  void findOne() {
  }
}
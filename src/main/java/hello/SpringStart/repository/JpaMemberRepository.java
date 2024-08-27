package hello.SpringStart.repository;

import hello.SpringStart.domain.Member;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class JpaMemberRepository implements MemberRepository {

  private final EntityManager em;

  @Autowired
  public JpaMemberRepository(EntityManager em) {
    this.em = em;
  }

  @Override
  public Member save(Member member) {
    em.persist(member);

    return member;
  }

  @Override
  public Optional<Member> findById(Long id) {
    Member member = em.find(Member.class, id);
    return Optional.of(member);
  }

  @Override
  public Optional<Member> findByName(String name) {
    String sql = """
      SELECT
        m
      FROM
        Member AS m
      WHERE
        m.name = :name
    """;

    List<Member> result = em.createQuery(sql, Member.class)
      .setParameter("name", name)
      .getResultList();

    return result.stream().findAny();
  }

  @Override
  public List<Member> findAll() {
    String sql = """
      SELECT
        m
      FROM
        Member AS m
    """;

    return em.createQuery(sql, Member.class)
              .getResultList();
  }
}

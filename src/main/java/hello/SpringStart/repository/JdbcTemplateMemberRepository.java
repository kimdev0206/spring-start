package hello.SpringStart.repository;

import hello.SpringStart.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JdbcTemplateMemberRepository implements MemberRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public JdbcTemplateMemberRepository(DataSource dataSource) {
    jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public Member save(Member member) {
    SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
    jdbcInsert.withTableName("members").usingGeneratedKeyColumns("id");

    Map<String, Object> parameters = new HashMap<>();
    parameters.put("name", member.getName());

    Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
    member.setId(key.longValue());

    return member;
  }

  @Override
  public Optional<Member> findById(Long id) {
    String sql = """
      SELECT
        *
      FROM
        members
      WHERE
        id = ?;
    """;

    List<Member> result = jdbcTemplate.query(sql, memeberRowMapper(), id);
    return result.stream().findAny();
  }

  @Override
  public Optional<Member> findByName(String name) {
    String sql = """
      SELECT
        *
      FROM
        members
      WHERE
        name = ?;
    """;

    List<Member> result = jdbcTemplate.query(sql, memeberRowMapper(), name);
    return result.stream().findAny();
  }

  @Override
  public List<Member> findAll() {
    String sql = """
      SELECT
        *
      FROM
        members;
    """;

    return jdbcTemplate.query(sql, memeberRowMapper());
  }

  private RowMapper<Member> memeberRowMapper() {
    return (rs, rowNum) -> {
      Member member = new Member();
      member.setId(rs.getLong("id"));
      member.setName(rs.getString("name"));

      return member;
    };
  }
}

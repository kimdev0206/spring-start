package hello.SpringStart.repository;

import hello.SpringStart.domain.Member;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcMemberRepository implements MemberRepository {

  private final DataSource dataSource;

  public JdbcMemberRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  private Connection getConnection() {
    return DataSourceUtils.getConnection(dataSource);
  }

  private void close(Connection con) throws SQLException {
    DataSourceUtils.releaseConnection(con, dataSource);
  }

  private void close(Connection con, PreparedStatement pstmt, ResultSet rs) {
    try {
      if (rs != null) rs.close();

    } catch (SQLException e) {
      e.printStackTrace();
    }

    try {
      if (pstmt != null) pstmt.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    try {
      if (con != null) close(con);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Member save(Member member) {
    String sql = """
      INSERT INTO
        members (name)
      VALUES
        (?);
    """;

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
      con = getConnection();

      pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      pstmt.setString(1, member.getName());
      pstmt.executeUpdate();

      rs = pstmt.getGeneratedKeys();

      if (rs.next()) {
        member.setId(rs.getLong(1));
      } else {
        throw new SQLException("id 조회 실패");
      }

      return member;
    } catch (Exception e) {
      throw new IllegalStateException(e);
    } finally {
      close(con, pstmt, rs);
    }
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

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
      con = getConnection();

      pstmt = con.prepareStatement(sql);
      pstmt.setLong(1, id);

      rs = pstmt.executeQuery();

      if (rs.next()) {
        Member member = new Member();
        member.setId(rs.getLong("id"));
        member.setName(rs.getString("name"));;

        return Optional.of(member);
      } else {
        return Optional.empty();
      }
    } catch (Exception e) {
      throw new IllegalStateException(e);
    } finally {
      close(con, pstmt, rs);
    }
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

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
      con = getConnection();

      pstmt = con.prepareStatement(sql);
      pstmt.setString(1, name);

      rs = pstmt.executeQuery();

      if (rs.next()) {
        Member member = new Member();
        member.setId(rs.getLong("id"));
        member.setName(rs.getString("name"));;

        return Optional.of(member);
      } else {
        return Optional.empty();
      }
    } catch (Exception e) {
      throw new IllegalStateException(e);
    } finally {
      close(con, pstmt, rs);
    }
  }

  @Override
  public List<Member> findAll() {
    String sql = """
      SELECT
        *
      FROM
        members;
    """;

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
      con = getConnection();
      pstmt = con.prepareStatement(sql);;
      rs = pstmt.executeQuery();

      List<Member> members = new ArrayList<>();

      while (rs.next()) {
        Member member = new Member();
        member.setId(rs.getLong("id"));
        member.setName(rs.getString("name"));;
        members.add(member);
      }

      return members;
    } catch (Exception e) {
      throw new IllegalStateException(e);
    } finally {
      close(con, pstmt, rs);
    }
  }
}

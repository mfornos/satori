package db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import models.metro.Card;
import models.metro.MetroPage;

import org.apache.commons.lang3.StringUtils;
import org.h2.jdbcx.JdbcConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class H2 {

    public static interface DBCall {
        void call(Connection con) throws SQLException;
    }

    private static final Logger log = LoggerFactory.getLogger(H2.class);
    private static final int ITEMS_PER_PAGE = 7;

    private static final JdbcConnectionPool cp = JdbcConnectionPool.create("jdbc:h2:test", "sa", "sa");

    static {
        createDB();
    }

    public static Card get(final String id) {
        final Card card = new Card();
        execute(new DBCall() {
            @Override
            public void call(Connection con) throws SQLException {
                PreparedStatement st = con.prepareStatement("SELECT * FROM CIA WHERE id = ?");
                st.setString(1, id);
                st.execute();
                ResultSet rs = st.getResultSet();
                rs.first();
                card.from(rs);
            }
        });
        return card;
    }

    public static MetroPage search(final String q, int p) {

        final MetroPage mp = new MetroPage(p, ITEMS_PER_PAGE);
        mp.setQuery(q);

        execute(new DBCall() {

            @Override
            public void call(Connection con) throws SQLException {
                count(q, mp, con);
                PreparedStatement st = buildStatement(q, mp, con);
                st.execute();
                ResultSet rs = st.getResultSet();
                while (rs.next()) {
                    mp.add(new Card(rs));
                }
            }

            private PreparedStatement buildStatement(final String q, final MetroPage mp, Connection con)
                    throws SQLException {
                PreparedStatement st;
                if (StringUtils.isBlank(q)) {
                    st = con.prepareStatement("SELECT * FROM CIA LIMIT ? OFFSET ?");
                    st.setInt(1, mp.getItemsPerPage());
                    st.setInt(2, mp.getOffset());
                } else {
                    st = con.prepareStatement("SELECT T.* FROM FT_SEARCH_DATA(?, ?, ?) FT, CIA T WHERE FT.TABLE='CIA' AND T.ID=FT.KEYS[0];");
                    st.setString(1, q);
                    st.setInt(2, mp.getItemsPerPage());
                    st.setInt(3, mp.getOffset());
                }
                return st;
            }

            private void count(final String q, final MetroPage mp, Connection con) throws SQLException {
                PreparedStatement st;
                if (StringUtils.isBlank(q)) {
                    st = con.prepareStatement("SELECT COUNT(*) FROM CIA");
                } else {
                    st = con.prepareStatement("SELECT COUNT(*) FROM FT_SEARCH(?, 0, 0)");
                    st.setString(1, q);
                }
                st.execute();
                ResultSet rs = st.getResultSet();
                rs.first();
                mp.setItemsNum(rs.getInt(1));
                rs.close();
            }
        });
        return mp;
    }

    private static void createDB() {

        execute(new DBCall() {

            @Override
            public void call(Connection con) throws SQLException {
                Statement stat = con.createStatement();
                stat.execute("CREATE ALIAS IF NOT EXISTS FT_INIT FOR \"org.h2.fulltext.FullText.init\"");
                stat.execute("CALL FT_INIT()");
                if (tableNotExists(con, "CIA")) {
                    stat.execute("DROP TABLE IF EXISTS CIA");
                    stat.execute("CREATE TABLE CIA AS SELECT * FROM CSVREAD('src/main/resources/data/cia-data-all.csv')");
                    stat.execute("ALTER TABLE CIA ALTER COLUMN ID SET NOT NULL");
                    stat.execute("ALTER TABLE CIA ADD PRIMARY KEY (ID)");
                    stat.execute("CALL FT_CREATE_INDEX('PUBLIC', 'CIA', NULL)");
                }
            }

            private boolean tableNotExists(Connection con, String table) throws SQLException {
                DatabaseMetaData dbm = con.getMetaData();
                ResultSet rs = dbm.getTables(null, null, table, null);
                boolean exists = rs.next();
                rs.close();
                return !exists;
            }

        });
    }

    private static void execute(final DBCall db) {
        Connection con = null;
        try {
            con = cp.getConnection();
            db.call(con);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

}

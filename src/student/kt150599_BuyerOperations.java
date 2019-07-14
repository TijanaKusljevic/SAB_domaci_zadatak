package student;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import operations.BuyerOperations;

public class kt150599_BuyerOperations implements BuyerOperations {

	@Override
	public int createBuyer(String name, int cityId) {
		Connection connection = DB.getInstance().getConnection();
		String insertQuery = "insert into Buyer (Name, IdCity, Credit) values(?, ?, 0)";

		try {
			PreparedStatement ps = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);

			ps.setString(1, name);
			ps.setInt(2, cityId);
			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			int k = -1;

			while (rs.next()) {
				k = rs.getInt(1);

			}
			return k;

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return -1;
	}

	@Override
	public int setCity(int buyerId, int cityId) {
		Connection connection = DB.getInstance().getConnection();
		String upd = "update Buyer set IdCity=? where Id=? ";

		int ret = -1;
		try {
			PreparedStatement ps = connection.prepareStatement(upd);
			ps.setInt(1, cityId);
			ps.setInt(2, buyerId);
			ps.executeUpdate();
			return 1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public int getCity(int buyerId) {
		Connection connection = DB.getInstance().getConnection();
		String s = "select IdCity from Buyer where Id=?";

		try {
			PreparedStatement ps = connection.prepareStatement(s);
			ps.setInt(1, buyerId);
			ResultSet rs = ps.executeQuery();
			int ret = -1;
			while (rs.next()) {
				ret = rs.getInt(1);
			}

			return ret;
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return -1;
	}

	@Override
	public BigDecimal increaseCredit(int buyerId, BigDecimal credit) {
		Connection connection = DB.getInstance().getConnection();
		String s = "update Buyer set credit= (select credit from Buyer where Id=?)+? where Id=?";

		try {
			PreparedStatement ps = connection.prepareStatement(s);
			ps.setInt(1, buyerId);
			ps.setBigDecimal(2, credit);
			ps.setInt(3, buyerId);
			ps.executeUpdate();

			String ss = "select Credit from Buyer where id=?";

			PreparedStatement p = connection.prepareStatement(ss);
			p.setInt(1, buyerId);
			ResultSet rs = p.executeQuery();

			BigDecimal ret = null;
			while (rs.next()) {
				ret = rs.getBigDecimal(1);
				// ret.
			}
			ret=ret.setScale(3);
			return ret;
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return null;
	}

	@Override
	public int createOrder(int buyerId) {
		Connection connection = DB.getInstance().getConnection();
		String insert = "insert into [Order](price, status, discount, IdBuyer) values (0, 'created', 0, ?)";

		try {
			PreparedStatement ps = connection.prepareStatement(insert, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setInt(1, buyerId);
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			int ret = -1;
			while (rs.next()) {
				ret = rs.getInt(1);
			}

			return ret;
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return -1;
	}

	@Override
	public List<Integer> getOrders(int buyerId) {
		Connection connection = DB.getInstance().getConnection();
		String s = "select id from [Order] where IdBuyer=?";

		List<Integer> list = new ArrayList<Integer>();

		try {
			PreparedStatement stmt = connection.prepareStatement(s);
			stmt.setInt(1, buyerId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				list.add(rs.getInt(1));
			}
			return list;

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return null;
	}

	@Override
	public BigDecimal getCredit(int buyerId) {
		Connection connection = DB.getInstance().getConnection();
		String ss = "select Credit from Buyer where id=?";
		try {
			PreparedStatement p = connection.prepareStatement(ss);
			p.setInt(1, buyerId);
			ResultSet rs = p.executeQuery();

			BigDecimal ret = null;
			while (rs.next()) {
				ret = rs.getBigDecimal(1);
				//ret=ret.setScale(0, RoundingMode.CEILING);
				ret=ret.setScale(3);
			}

			return ret;
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return null;
	}

}

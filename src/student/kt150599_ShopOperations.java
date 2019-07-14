package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import operations.ShopOperations;

public class kt150599_ShopOperations implements ShopOperations {

	@Override
	public int createShop(String name, String cityName) {
		Connection connection = DB.getInstance().getConnection();
		String selectQuery = "select id from City where name=?";

		PreparedStatement p;
		int idCity = 0;
		try {
			p = connection.prepareStatement(selectQuery, PreparedStatement.RETURN_GENERATED_KEYS);
			p.setString(1, cityName);
			ResultSet r = p.executeQuery();

			while (r.next()) {
				idCity = r.getInt(1);

			}
		} catch (SQLException e2) {

			e2.printStackTrace();
		}

		String insertQuery = "insert into Shop (Name, IdCity, Discount) values(?, ?, 0)";

		try {
			PreparedStatement ps = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);

			ps.setString(1, name);
			ps.setInt(2, idCity);
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
	public int setCity(int shopId, String cityName) {
		Connection connection = DB.getInstance().getConnection();
		String upd = "update Shop set IdCity=? where Id=?";

		String selectQuery = "select id from City where name=?";

		PreparedStatement p;
		int idCity = 0;
		try {
			p = connection.prepareStatement(selectQuery);
			p.setString(1, cityName);
			ResultSet r = p.executeQuery();

			while (r.next()) {
				idCity = r.getInt(1);

			}
		} catch (SQLException e2) {

			e2.printStackTrace();
			return -1;
		}

		try {
			PreparedStatement ps = connection.prepareStatement(upd);
			ps.setInt(1, idCity);
			ps.setInt(2, shopId);
			ps.executeUpdate();
			return 1;

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return -1;
	}

	@Override
	public int getCity(int shopId) {
		Connection connection = DB.getInstance().getConnection();
		String str = "select IdCity from Shop where Id=?";

		PreparedStatement p;

		try {
			p = connection.prepareStatement(str);
			p.setInt(1, shopId);
			ResultSet r = p.executeQuery();
			int id = -1;
			while (r.next()) {
				id = r.getInt(1);

			}
			return id;
		} catch (SQLException e2) {

			e2.printStackTrace();
			return -1;
		}

	}

	@Override
	public int setDiscount(int shopId, int discountPercentage) {
		Connection connection = DB.getInstance().getConnection();
		String s = "update Shop set discount=? where Id=?";

		try {
			PreparedStatement ps = connection.prepareStatement(s);
			ps.setInt(1, discountPercentage);
			ps.setInt(2, shopId);
			ps.executeUpdate();
			return 1;

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return -1;
	}

	@Override
	public int increaseArticleCount(int articleId, int increment) {
		Connection connection = DB.getInstance().getConnection();
		String s = "update Artical set count=(select count from Artical where Id=?) + ? where Id=?";

		try {
			PreparedStatement ps = connection.prepareStatement(s);
			ps.setInt(1, articleId);
			ps.setInt(2, increment);
			ps.setInt(3, articleId);
			ps.executeUpdate();

			String ss = "select count from Artical where id=?";

			PreparedStatement p = connection.prepareStatement(ss);
			p.setInt(1, articleId);

			ResultSet rs = p.executeQuery();

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
	public int getArticleCount( int articleId) {
		Connection connection = DB.getInstance().getConnection();

		String ss = "select count from Artical where id=?";
		try {
			PreparedStatement p = connection.prepareStatement(ss);
			p.setInt(1, articleId);

			ResultSet rs = p.executeQuery();

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
	public List<Integer> getArticles(int shopId) {
		Connection connection = DB.getInstance().getConnection();

		String ss = "select Id from Artical where IdShop=?";
		
		PreparedStatement p;
		try {
			p = connection.prepareStatement(ss);
			p.setInt(1, shopId);
			ResultSet rs = p.executeQuery();
			
			List<Integer> list=new ArrayList<Integer>();
			
			while(rs.next()) {
				list.add(rs.getInt(1));
			}
			
			return list;
			
		} catch (SQLException e) {
			
			
			e.printStackTrace();
		}
		
		
		return null;
	}

	@Override
	public int getDiscount(int shopId) {
		Connection connection = DB.getInstance().getConnection();
		String str = "select discount from Shop where Id=?";

		PreparedStatement p;

		try {
			p = connection.prepareStatement(str);
			p.setInt(1, shopId);
			ResultSet r = p.executeQuery();
			int d = -1;
			while (r.next()) {
				d = r.getInt(1);

			}
			return d;
		} catch (SQLException e2) {

			e2.printStackTrace();
			return -1;
		}

	}

}

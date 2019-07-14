package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import operations.ArticleOperations;

public class kt150599_ArticleOperation implements ArticleOperations {

	@Override
	public int createArticle(int shopId, String articleName, int articlePrice) {
		Connection connection=DB.getInstance().getConnection();
		String insert="insert into Artical(Name, Price, IdShop, Count) values (?,?,?,0)";
		
		try {
			PreparedStatement ps=connection.prepareStatement(insert, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1, articleName);
			ps.setInt(2, articlePrice);
			ps.setInt(3, shopId);
			ps.executeUpdate();
			
			ResultSet rs=ps.getGeneratedKeys();
            int k=-1;
            
            while(rs.next()) {
            	k=rs.getInt(1);
            	
            }
            return k;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}

}

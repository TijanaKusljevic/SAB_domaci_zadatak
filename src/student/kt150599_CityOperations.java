package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import operations.CityOperations;

public class kt150599_CityOperations implements CityOperations{

	@Override
	public int createCity(String name) {
		Connection connection=DB.getInstance().getConnection();
		String insertQuery="insert into City (Name) values (?)";
		
		try {
			PreparedStatement ps=connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            
            ps.setString(1, name);
            ps.executeUpdate(); 
            
            ResultSet rs=ps.getGeneratedKeys();
            int k=-1;
            
            while(rs.next()) {
            	k=rs.getInt(1);
            	
            }
           
            return k;
            
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
        
		
		
		return -1;
	}

	@Override
	public List<Integer> getCities() {
		Connection connection=DB.getInstance().getConnection();
		String selectQuery="select * from City";
		List<Integer> list=new ArrayList<Integer>();
		try {
			Statement stmt=connection.createStatement();
			ResultSet rs=stmt.executeQuery(selectQuery);
			while(rs.next()) {
				list.add(rs.getInt(1));
			}
			return list;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}

	@Override
	public int connectCities(int cityId1, int cityId2, int distance) {
		Connection connection=DB.getInstance().getConnection();
		String insertQuery="insert into Connected ( IdCity1, IdCity2, Distance) values(?,?,?)";
		
		try {
			PreparedStatement ps=connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            
            ps.setInt(1, cityId1);
            ps.setInt(2, cityId2);
            ps.setInt(3, distance);
            ps.executeUpdate(); 
            
            ResultSet rs=ps.getGeneratedKeys();
            int k=-1;
            
            while(rs.next()) {
            	k=rs.getInt(1);
            	
            }
            
            return k;
            
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
        
		
		return -1;
	}

	@Override
	public List<Integer> getConnectedCities(int cityId) {
		
		Connection connection=DB.getInstance().getConnection();
		String selectQuery="select IdCity2 from Connected where IdCity1=? union select idCity1 from Connected where IdCity2=?";
		List<Integer> list=new ArrayList<Integer>();
		
		try {
			PreparedStatement stmt=connection.prepareStatement(selectQuery);
			stmt.setInt(1, cityId);
			stmt.setInt(2, cityId);
			ResultSet rs=stmt.executeQuery();
			while(rs.next()) {
				list.add(rs.getInt(1));
			}
			return list;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public List<Integer> getShops(int cityId) {
		Connection connection=DB.getInstance().getConnection();
		String selectQuery="select Id from Shop where IdCity=?";
		List<Integer> list=new ArrayList<Integer>();
		
		try {
			PreparedStatement stmt=connection.prepareStatement(selectQuery);
			stmt.setInt(1, cityId);
			ResultSet rs=stmt.executeQuery();
			while(rs.next()) {
				list.add(rs.getInt(1));
			}
			return list;
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		return null;
	}

}

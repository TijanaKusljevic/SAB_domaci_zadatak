package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import operations.GeneralOperations;

public class kt150599_GeneralOperations implements GeneralOperations{
	
	private static Calendar myCalendar;

	@Override
	public void setInitialTime(Calendar time) {
		myCalendar=(Calendar) time.clone();
		
	}

	@Override
	public Calendar time(int days) {
		myCalendar.add(Calendar.DAY_OF_YEAR, days);
		
		String s="select o.id, l.arrivingtime from [order] o, location l where o.id=l.idorder and o.status='sent' and l.arrivingtime= ("
				+ " select max(lo.arrivingtime) from location lo where lo.idorder=o.id)"
				+ " and l.arrivingtime <= ? ";
		
		Connection connection = DB.getInstance().getConnection();
		
		try {
			PreparedStatement ps=connection.prepareStatement(s);
			Calendar cal = (Calendar) myCalendar.clone();
			java.util.Date d = cal.getTime();
			java.sql.Date sqlDate = new java.sql.Date(d.getTime());
			ps.setDate(1, sqlDate);
			
			ResultSet rs=ps.executeQuery();
			
			while(rs.next()) {
				
				int order=rs.getInt(1);
				java.sql.Date received =rs.getDate(2);
				
				String ss="update [order] set status='arrived', receivedTime=? where id=?";
				PreparedStatement pss=connection.prepareStatement(ss);
				pss.setDate(1, received);
				pss.setInt(2, order);
				pss.executeUpdate();
				
			}
			return cal;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return myCalendar;
	}

	@Override
	public Calendar getCurrentTime() {
		
		return myCalendar;
	}

	@Override
	public void eraseAll() {
		Connection connection=DB.getInstance().getConnection();
		String del1="delete from Connected";
		String del2="delete from Location";
		String del3="delete from ItemLocation";
		String del4="delete from Item";
		String del5="delete from Artical";
		String del6="delete from [Transaction]";
		String del7="delete from [Order]";
		String del8="delete from Buyer";
		String del9="delete from Shop";
		String del10="delete from City";
		String del11="delete from transactionshop";
		String del12="delete from transactionbuyer";
		try {
			Statement stmt=connection.createStatement();
			stmt.executeUpdate(del1);
			stmt.executeUpdate(del2);
			//stmt.executeUpdate(del3);
			stmt.executeUpdate(del4);
			stmt.executeUpdate(del5);
			stmt.executeUpdate(del6);
			stmt.executeUpdate(del7);
			stmt.executeUpdate(del8);
			stmt.executeUpdate(del9);
			stmt.executeUpdate(del10);
			stmt.executeUpdate(del11);
			stmt.executeUpdate(del12);
			
			
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		
	}

}

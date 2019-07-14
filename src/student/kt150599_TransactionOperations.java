package student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import operations.TransactionOperations;

public class kt150599_TransactionOperations implements TransactionOperations{

	@Override
	public BigDecimal getBuyerTransactionsAmmount(int buyerId) {
		Connection connection = DB.getInstance().getConnection();
		String s="select sum(amount) " + 
				"from [transaction] t, [TransactionBuyer] b " + 
				"where b.IdBuyer=? and b.id=t.Id";
		
		try {
			PreparedStatement st=connection.prepareStatement(s);
			st.setInt(1, buyerId);
			ResultSet rs=st.executeQuery();
			BigDecimal bd=null;
			if(rs.next()) {
				bd=rs.getBigDecimal(1).setScale(3);
			} else {
				bd=new BigDecimal("0");
				return bd;
			}
			
			return bd;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public BigDecimal getShopTransactionsAmmount(int shopId) {
		Connection connection = DB.getInstance().getConnection();
		String s="select sum(amount) " + 
				"from [transaction] t, [TransactionShop] b " + 
				"where b.IdShop=? and b.id=t.Id";
		
		try {
			PreparedStatement st=connection.prepareStatement(s);
			st.setInt(1, shopId);
			ResultSet rs=st.executeQuery();
			BigDecimal bd=null;
			if(rs.next()) {
				
				bd=rs.getBigDecimal(1);
				//System.out.println(bd);
				
				if(bd==null) {
					bd = new BigDecimal("0").setScale(3);
					return bd;
				}
				
				
			} else {
				bd = new BigDecimal("0").setScale(3);
				return null;
			}
			
			return bd;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return null;
	}

	@Override
	public List<Integer> getTransationsForBuyer(int buyerId) {
		Connection connection = DB.getInstance().getConnection();
		String s="select id from TransactionBuyer where IdBuyer=?";
		
		try {
			PreparedStatement st=connection.prepareStatement(s);
			st.setInt(1, buyerId);
			ResultSet rs=st.executeQuery();
			List<Integer> list=new ArrayList<Integer>();
			
			
			
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
	public int getTransactionForBuyersOrder(int orderId) {
		Connection connection = DB.getInstance().getConnection();
		String s="select t.id from [Transaction] t, [TransactionBuyer] b "
				+ "where t.IdOrder=? and t.id in (select id from TransactionBuyer)";
		
		PreparedStatement st;
		try {
			st = connection.prepareStatement(s);
			st.setInt(1, orderId);
			
			ResultSet rs=st.executeQuery();
			int ret=-1;
			
			while(rs.next()) {
				ret=rs.getInt(1);
			}
			
			return ret;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return -1;
	}

	@Override
	public int getTransactionForShopAndOrder(int orderId, int shopId) {
		Connection connection = DB.getInstance().getConnection();
		String s="select t.id from [Transaction] t, [TransactionShop] b "
				+ "where t.IdOrder=? and t.id in (select id from TransactionShop) and b.IdShop=?";
		
		PreparedStatement st;
		try {
			st = connection.prepareStatement(s);
			st.setInt(1, orderId);
			st.setInt(2, shopId);
			ResultSet rs=st.executeQuery();
			int ret=-1;
			
			while(rs.next()) {
				ret=rs.getInt(1);
				
			}
			
			return ret;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return -1;
	}

	@Override
	public List<Integer> getTransationsForShop(int shopId) {
		Connection connection = DB.getInstance().getConnection();
		String s="select id from TransactionShop where IdShop=?";
		
		try {
			PreparedStatement st=connection.prepareStatement(s);
			st.setInt(1, shopId);
			ResultSet rs=st.executeQuery();
			List<Integer> list=new ArrayList<Integer>();
			
			
			
			while(rs.next()) {
				list.add(rs.getInt(1));
			}
			
			if(list.size()==0) {
				return null;
			}
			
			
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Calendar getTimeOfExecution(int transactionId) {
		Connection connection = DB.getInstance().getConnection();
		String s="select time from [Transaction] where Id=?";
		
		try {
			PreparedStatement st=connection.prepareStatement(s);
			st.setInt(1, transactionId);
			
			ResultSet rs=st.executeQuery();
			
			Calendar cal=(Calendar) new kt150599_GeneralOperations().getCurrentTime().clone();
			
			while(rs.next()) {
				cal.setTime(rs.getTimestamp(1));
				
			}
			
			return cal;
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		
		return null;
	}

	@Override
	public BigDecimal getAmmountThatBuyerPayedForOrder(int orderId) {
		Connection connection = DB.getInstance().getConnection();
		String s="select amount from [Transaction] t, [TransactionBuyer] b "
				+ "where t.IdOrder=? and t.id in (select id from TransactionBuyer)";
		
		PreparedStatement st;
		try {
			st = connection.prepareStatement(s);
			st.setInt(1, orderId);
			
			ResultSet rs=st.executeQuery();
			BigDecimal b=null;
			
			while(rs.next()) {
				b=rs.getBigDecimal(1);
			}
			
			return b;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return null;
		
	}

	@Override
	public BigDecimal getAmmountThatShopRecievedForOrder(int shopId, int orderId) {
		Connection connection = DB.getInstance().getConnection();
		String s="select amount from [Transaction] t, [TransactionShop] b "
				+ "where t.IdOrder=? and t.id in (select id from TransactionShop) and b.IdShop=?";
		
		PreparedStatement st;
		try {
			st = connection.prepareStatement(s);
			st.setInt(1, orderId);
			st.setInt(2, shopId);
			
			ResultSet rs=st.executeQuery();
			BigDecimal b=null;
			
			while(rs.next()) {
				b=rs.getBigDecimal(1);
			}
			
			return b;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return null;
	}

	@Override
	public BigDecimal getTransactionAmount(int transactionId) {
		Connection connection = DB.getInstance().getConnection();
		String s="select t.amount from [Transaction] t where t.id=? ";
				
		
		PreparedStatement st;
		try {
			st = connection.prepareStatement(s);
			st.setInt(1, transactionId);
			
			ResultSet rs=st.executeQuery();
			BigDecimal bd=null;
			
			while(rs.next()) {
				bd=rs.getBigDecimal(1);
			}
			
			return bd;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public BigDecimal getSystemProfit() {
		Connection connection = DB.getInstance().getConnection();
		String s="select sum(amount) from [transaction] where id in (select id from transactionShop)";
		String ss="select sum(t.amount) from [transaction] t where t.id in (select id from transactionBuyer) "
				+ " and t.idorder in (select  tt.idorder from [transaction] tt where tt.id in (select id from transactionShop))";
		
		String shop="select * from [transaction]";
		String buyer="select sum(amount) from [transaction] where id in (select id from transactionBuyer)";
		
		
		//String sss="select tt.idorder, tt.id from [transaction] tt where tt.id in (select id from transactionShop)";
		
		try {
			/*Statement pom=connection.createStatement();
			ResultSet p=pom.executeQuery(sss);
			
			while(p.next()) {
				System.out.println("usao");
				System.out.println(p.getInt(1)+" nesto");
				System.out.println(p.getInt(2)+" nesto id");
				
			}*/
			
			
			Statement stmt= connection.createStatement();
			ResultSet rs=stmt.executeQuery(s);
			
			BigDecimal b=new BigDecimal("0");
			if(rs.next()) {
				b=rs.getBigDecimal(1);
				if(b==null) {
					b=new BigDecimal("0");
				}
			}
			
			System.out.println(b);
			
			
			
			
			Statement stmt1= connection.createStatement();
			ResultSet rs1=stmt1.executeQuery(ss);
			
			BigDecimal bd=new BigDecimal("0").setScale(3);
			if(rs1.next()) {
				
				bd=rs1.getBigDecimal(1);
				
				
				if(bd==null) {
					bd=new BigDecimal("0").setScale(3);
				}
				
			}
			
			System.out.println(bd);
			
			return bd.subtract(b);
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
				
		return null;
	}

}

package student;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import operations.OrderOperations;

public class kt150599_OrderOperations implements OrderOperations {

	@Override
	public int addArticle(int orderId, int articleId, int count) {
		Connection connection = DB.getInstance().getConnection();

		String s = "select count from Artical where id=?";
		boolean enough = false;

		try {
			PreparedStatement p = connection.prepareStatement(s);
			p.setInt(1, articleId);
			ResultSet r = p.executeQuery();
			int num = 0;
			while (r.next()) {
				num = r.getInt(1);
			}
			if (num < count) {
				return -1;
			}
		} catch (SQLException e1) {

			e1.printStackTrace();
			return -1;
		}

		String sel = "select Id from Item where IdOrder=? and IdArtical=?";
		String getprice="select a.Price * i.Count * (100-s.Discount)/100"
				+ " from [order] o, artical a, item i, shop s where "
				+ " o.id=? and a.id=?  and i.idorder=o.id and i.idartical=a.id and s.id=a.idshop";
		

		try {
			PreparedStatement pp = connection.prepareStatement(sel);
			pp.setInt(1, orderId);
			pp.setInt(2, articleId);

			ResultSet rr = pp.executeQuery();

			int item = 0;
			if (rr.next()) {
				item = rr.getInt(1);
			}

			if (item != 0) {
				String upd = "update Item set count=(select count from Item where Id=?)+? where IdOrder=? and IdArtical=?";

				PreparedStatement pp1 = connection.prepareStatement(upd);
				pp1.setInt(1, item);
				pp1.setInt(2, count);
				pp1.setInt(3, orderId);
				pp1.setInt(4, articleId);
				pp1.executeUpdate();

				String upd1 = "update Artical set count=(select count from Artical where Id=?)-? where Id=?";
				PreparedStatement pp2 = connection.prepareStatement(upd1);
				pp2.setInt(1, articleId);
				pp2.setInt(2, count);
				pp2.setInt(3, articleId);
				pp2.executeUpdate();
				
				PreparedStatement pp22 = connection.prepareStatement(getprice);
				pp22.setInt(1, orderId);
				pp22.setInt(2, articleId);
				ResultSet af=pp22.executeQuery();
				
				if(af.next()) {
					BigDecimal afbd=af.getBigDecimal(1);
					String f="update Item set price=? where id=?";
					PreparedStatement pp3 = connection.prepareStatement(f);
					
					pp3.setBigDecimal(1, afbd);
					pp3.setInt(2, item);
					pp3.executeUpdate();
				}
				return item;
			}
		} catch (SQLException e1) {

			e1.printStackTrace();
			return -1;
		}

		String insert = "insert into Item(IdOrder, IdArtical, Count, price) values (?,?,?, ?)";

		try {
			PreparedStatement ps = connection.prepareStatement(insert, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setInt(1, orderId);
			ps.setInt(2, articleId);
			ps.setInt(3, count);
			ps.setBigDecimal(4, new BigDecimal("0"));

			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			int id = -1;
			while (rs.next()) {
				id = rs.getInt(1);
			}

			String update = "update Artical set count=(select count from Artical where Id=?)-? where Id=?";
			PreparedStatement ps2 = connection.prepareStatement(update);

			ps2.setInt(1, articleId);
			ps2.setInt(2, count);
			ps2.setInt(3, articleId);
			ps2.executeUpdate();
			
			PreparedStatement pp22 = connection.prepareStatement(getprice);
			pp22.setInt(1, orderId);
			pp22.setInt(2, articleId);
			ResultSet af=pp22.executeQuery();
			
			if(af.next()) {
				BigDecimal afbd=af.getBigDecimal(1);
				String f="update Item set price=? where id=?";
				PreparedStatement pp3 = connection.prepareStatement(f);
				
				pp3.setBigDecimal(1, afbd);
				pp3.setInt(2, id);
				pp3.executeUpdate();
			}

			return id;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		return -1;
	}

	@Override
	public int removeArticle(int orderId, int articleId) {
		Connection connection = DB.getInstance().getConnection();
		String s = "select id, count from Item where IdOrder=? and IdArtical=?";

		try {
			PreparedStatement ps = connection.prepareStatement(s);
			ps.setInt(1, orderId);
			ps.setInt(2, articleId);

			ResultSet rs = ps.executeQuery();

			int item = 0;
			int count = 0;

			while (rs.next()) {
				item = rs.getInt(1);
				count = rs.getInt(2);
			}

			String del = "delete from Item where Id=?";
			String sub = "update Artical set count=(select count from Artical where id=?)+? where Id=?";

			PreparedStatement ps1 = connection.prepareStatement(del);
			ps1.setInt(1, item);
			ps1.executeUpdate();

			PreparedStatement ps2 = connection.prepareStatement(sub);
			ps2.setInt(1, articleId);
			ps2.setInt(2, count);
			ps2.setInt(3, articleId);
			ps2.executeUpdate();

			return 1;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return -1;
	}

	@Override
	public List<Integer> getItems(int orderId) {
		Connection connection = DB.getInstance().getConnection();
		String s = "select id from Item where IdOrder=?";

		try {
			PreparedStatement ps = connection.prepareStatement(s);
			ps.setInt(1, orderId);
			ResultSet rs = ps.executeQuery();
			List<Integer> list = new ArrayList<Integer>();

			while (rs.next()) {
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
	public int completeOrder(int orderId) {
		Connection connection = DB.getInstance().getConnection();
		String s = "update [Order] set status='sent' where id=?";
		String s2 = "update [Order] set price =? where id=?";
		String s1 = "update [Order] set sentTime =? where id=?";
		String s3 = "update [Order] set receivedTime =? where id=?";
		String s4 = "update [Order] set discount =? where id=?";
		String first = "select status from [Order] where id=?";

		Calendar cal = (Calendar) new kt150599_GeneralOperations().getCurrentTime().clone();
		java.util.Date d = cal.getTime();
		java.sql.Date sqlDate = new java.sql.Date(d.getTime());

		try {

			PreparedStatement ps = connection.prepareStatement(s);
			ps.setInt(1, orderId);
			ps.executeUpdate();

			PreparedStatement ps1 = connection.prepareStatement(s1);
			ps1.setInt(2, orderId);
			ps1.setDate(1, sqlDate);
			ps1.executeUpdate();

			BigDecimal bd = getFinalPrice(orderId);
			System.out.println(bd+"   bd");
			
			//System.out.println("konacna cena");
			//System.out.println(bd);

			PreparedStatement ps2 = connection.prepareStatement(s2);
			ps2.setInt(2, orderId);
			ps2.setBigDecimal(1, bd);
			ps2.executeUpdate();

			//BigDecimal b = getDiscountSum(orderId);

			String ss = "{call SP_FINAL_PRICE(?, ?,?, ?)}";
			CallableStatement stmt = connection.prepareCall(ss);
			stmt.setInt(1, orderId);
			Calendar cal1 = (Calendar) new kt150599_GeneralOperations().getCurrentTime().clone();
			java.util.Date d1 = cal1.getTime();
			java.sql.Date sqlDate1 = new java.sql.Date(d1.getTime());
			stmt.setDate(2, sqlDate);
			stmt.registerOutParameter(3, Types.DECIMAL);
			stmt.registerOutParameter(4, Types.INTEGER);
			stmt.execute();

			int discount = stmt.getInt(4);

			PreparedStatement ps3 = connection.prepareStatement(s4);
			ps3.setInt(2, orderId);
			ps3.setInt(1, discount);
			ps3.executeUpdate();

			String able = "select credit from Buyer where id=(select IdBuyer from [Order] where id=?)";
			PreparedStatement pp = connection.prepareStatement(able);
			pp.setInt(1, orderId);
			ResultSet rs = pp.executeQuery();

			BigDecimal amount = null;

			while (rs.next()) {
				amount = rs.getBigDecimal(1);
			}

			if (bd.compareTo(amount) == 1) {
				return -1;
			}

			String credit = "update Buyer set credit=(select credit from Buyer where id=(select IdBuyer from [Order] where id=?)) -?"
					+ " where id= (select IdBuyer from [Order] where id=?)";

			PreparedStatement ppp = connection.prepareStatement(credit);

			ppp.setInt(1, orderId);
			ppp.setBigDecimal(2, bd);
			ppp.setInt(3, orderId);

			ppp.executeUpdate();

			String create = "insert into [Transaction] (idorder, amount, time) values(?,?,?)";
			
			

			PreparedStatement pre = connection.prepareStatement(create, PreparedStatement.RETURN_GENERATED_KEYS);
			pre.setInt(1, orderId);
			pre.setBigDecimal(2, bd);
			pre.setDate(3, sqlDate);

			pre.executeUpdate();

			ResultSet rsi = pre.getGeneratedKeys();
			int k = 0;
			if (rsi.next()) {
				k = rsi.getInt(1);
			}

			String insert = "insert into TransactionBuyer (id,IdBuyer) values (?,(select IdBuyer from [Order] where id=?))";
			PreparedStatement pre1 = connection.prepareStatement(insert);

			pre1.setInt(1, k);
			pre1.setInt(2, orderId);
			pre1.executeUpdate();

			String shopCities = "select idCity from Shop s, Artical a, Item i where i.idOrder=? and i.idArtical=a.id and a.idshop=s.id ";
			String buyerCity = "select b.idCity from Buyer b, [Order] o where o.IdBuyer=b.Id and o.id=?";
			
			PreparedStatement pbuyer = connection.prepareStatement(buyerCity);
			pbuyer.setInt(1, orderId);

			ResultSet rsbuyer = pbuyer.executeQuery();
			int buyercity = 0;

			while (rsbuyer.next()) {
				buyercity = rsbuyer.getInt(1);
			}
			
			//novooooooooooooooooooooooooooooooooooooooooooooooooooooooo
			String nearestShop= "select idCity from Shop";
			Statement ns=connection.createStatement();
			ResultSet nsrs=ns.executeQuery(nearestShop);
			
			int nShop=0;
			int ndist=999999;
			int cand;
			while(nsrs.next()) {
				cand=nsrs.getInt(1);
				String call = "{call usp_Dijkstra (?, ?, ?) }";
				CallableStatement cstmt = connection.prepareCall(call);
				cstmt.setInt(1, buyercity);
				cstmt.setInt(2, cand);
				cstmt.registerOutParameter(3, Types.INTEGER);
				cstmt.execute();

				int distance = cstmt.getInt(3);
				
				if(distance<ndist) {
					ndist=distance;
					nShop=cand;
				}
			}
			//ovdeeeee

			

			

			PreparedStatement pscities = connection.prepareStatement(shopCities);
			pscities.setInt(1, orderId);
			ResultSet rscities = pscities.executeQuery();
			int min = 999999;
			int max = 0;
			int city = 0;

			

			String call = "{call usp_Dijkstra (?, ?, ?) }";

			int nearest = 0;
			int farest = 0;

			while (rscities.next()) {
				city = rscities.getInt(1);
				CallableStatement cstmt = connection.prepareCall(call);
				// cstmt.setInt(1, orderId);

				cstmt.setInt(1, buyercity);
				cstmt.setInt(2, city);
				cstmt.registerOutParameter(3, Types.INTEGER);
				cstmt.execute();

				int distance = cstmt.getInt(3);
				

				if (distance < min) {
					min = distance;
					nearest = city;
				}
				if (distance > max) {
					max = distance;
					farest = city;
				}

			}
			

			Calendar calf = (Calendar) new kt150599_GeneralOperations().getCurrentTime().clone();
			java.util.Date df = calf.getTime();
			java.sql.Date sqlDatef = new java.sql.Date(df.getTime());

			String sf = "{call make_locations(?, ?, ?, ?, ?, ?, ?, ?)}";
			CallableStatement stmt11 = connection.prepareCall(sf);

			//stmt11.setInt(1, nearest);
			stmt11.setInt(1, nShop);
			stmt11.setInt(2, farest);
			stmt11.setInt(3, buyercity);
			stmt11.setDate(4, sqlDatef);
			stmt11.setInt(5, orderId);
			stmt11.registerOutParameter(6, Types.VARCHAR);
			stmt11.registerOutParameter(7, Types.TIMESTAMP);
			stmt11.registerOutParameter(8, Types.INTEGER);
			stmt11.execute();

			String path = stmt11.getString(6);
			Calendar cal11 = (Calendar) new kt150599_GeneralOperations().getCurrentTime().clone();
			

			//System.out.println(stmt11.getInt(8));
			cal11.setTime(stmt11.getDate(7));
			

			
			String line = path;
			String[] parts = line.split("/");
			int[] ints = new int[parts.length];
			for (int i = 0; i < parts.length; i++) {
				ints[i] = Integer.parseInt(parts[i]);
			}

			int i = 0;
			int j = i + 1;

			while (true) {
				String ins = "insert into location (idcity, idorder, arrivingtime) values (?, ?, ?)";
				String callins = "{call usp_Dijkstra (?, ?, ?) }";
				int disins = 0;

				CallableStatement csins = connection.prepareCall(callins);
				// cstmt.setInt(1, orderId);

				csins.setInt(1, ints[i]);
				csins.setInt(2, ints[j]);
				csins.registerOutParameter(3, Types.INTEGER);
				csins.execute();

				disins = csins.getInt(3);
				cal11.add(Calendar.DAY_OF_YEAR, disins);

				java.util.Date jdate = cal11.getTime();
				java.sql.Date insdate = new java.sql.Date(jdate.getTime());

				PreparedStatement insps = connection.prepareStatement(ins);
				insps.setInt(1, ints[j]);
				insps.setInt(2, orderId);
				insps.setDate(3, insdate);

				insps.executeUpdate();
				i++;
				j++;

				if (j == ints.length) {
					
					break;
				}

			}

		} catch (SQLException e) {

			e.printStackTrace();
			return -1;
		}

		return 1;
	}

	@Override
	public BigDecimal getFinalPrice(int orderId) {
		Connection connection = DB.getInstance().getConnection();
		String s = "{call SP_FINAL_PRICE(?, ?,?, ?)}";
		
		String def="select price from [order] o where o.id=? ";
		

		try {
			PreparedStatement ps=connection.prepareStatement(def);
			ps.setInt(1, orderId);
			ResultSet rs=ps.executeQuery();
			
			if(rs.next()) {
				if(rs.getBigDecimal(1).signum()!=0) {
					return rs.getBigDecimal(1).setScale(3);
				}
			}
			
			
			CallableStatement stmt = connection.prepareCall(s);
			stmt.setInt(1, orderId);
			Calendar cal = (Calendar) new kt150599_GeneralOperations().getCurrentTime().clone();
			java.util.Date d = cal.getTime();
			java.sql.Date sqlDate = new java.sql.Date(d.getTime());
			stmt.setDate(2, sqlDate);
			stmt.registerOutParameter(3, Types.DECIMAL);
			stmt.registerOutParameter(4, Types.INTEGER);
			stmt.execute();
			
			//System.out.println()
			System.out.println("order id: "+orderId+" datum: "+sqlDate);
			System.out.println(stmt.getInt(4)+" "+" non ajdif "+stmt.getBigDecimal(3).setScale(3));

			return stmt.getBigDecimal(3).setScale(3);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public BigDecimal getDiscountSum(int orderId) {
		BigDecimal bd = getFinalPrice(orderId);
		Connection connection = DB.getInstance().getConnection();
		/*String s = "select cast(sum(a.Price * i.Count) as decimal(10,3)) "
				+ "	from item i, [order] o, artical a, shop s "
				+ "	where i.IdOrder=? and i.IdArtical=a.Id and a.IdShop=s.Id";*/
		
		String s = "select cast(sum(a.Price * i.Count) as decimal(10,3)) "
				+ "	from item i, artical a "
				+ "	where i.IdOrder=? and i.IdArtical=a.Id ";

		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(s);
			ps.setInt(1, orderId);
			ResultSet rs = ps.executeQuery();
			BigDecimal b = null;

			while (rs.next()) {
				b = rs.getBigDecimal(1).setScale(3);
			}
			
			System.out.println(b);
			System.out.println(bd);
			
			return b.subtract(bd);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public String getState(int orderId) {
		Connection connection = DB.getInstance().getConnection();
		String s = "select status from  [Order]  where id=?";

		try {
			PreparedStatement ps = connection.prepareStatement(s);
			ps.setInt(1, orderId);
			ResultSet rs = ps.executeQuery();
			String status = null;

			while (rs.next()) {
				status = rs.getString(1);
			}

			return status;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Calendar getSentTime(int orderId) {
		Connection connection = DB.getInstance().getConnection();
		String s = "select sentTime from  [Order]  where id=?";

		try {
			PreparedStatement ps = connection.prepareStatement(s);
			ps.setInt(1, orderId);
			ResultSet rs = ps.executeQuery();

			Date d = null;
			Calendar cal = (Calendar) new kt150599_GeneralOperations().getCurrentTime().clone();
			// cal.setTime(d);
			if (rs.next()) {

				d = rs.getDate(1);
				if (d == null) {
					return null;
				}
				cal.setTime(d);
			} else {
				return null;
			}

			return cal;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Calendar getRecievedTime(int orderId) {
		Connection connection = DB.getInstance().getConnection();
		String s = "select receivedTime from  [Order]  where id=?";

		try {
			PreparedStatement ps = connection.prepareStatement(s);
			ps.setInt(1, orderId);
			ResultSet rs = ps.executeQuery();

			Date d = null;
			Calendar cal = (Calendar) new kt150599_GeneralOperations().getCurrentTime().clone();
			//cal.setTime(d);
			while (rs.next()) {
				d = rs.getDate(1);
				//System.out.println("received date "+d);
				if (d == null) {
					return null;
				}
				cal.setTime(d);
			}

			return cal;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int getBuyer(int orderId) {
		Connection connection = DB.getInstance().getConnection();
		String s = "select IdBuyer from  [Order]  where id=?";

		try {
			PreparedStatement ps = connection.prepareStatement(s);
			ps.setInt(1, orderId);
			ResultSet rs = ps.executeQuery();

			int id = 0;

			while (rs.next()) {
				id = rs.getInt(1);
			}

			return id;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public int getLocation(int orderId) {
		Connection connection = DB.getInstance().getConnection();
		String first = "select status from [Order] where id=?";

		PreparedStatement psi;
		try {
			psi = connection.prepareStatement(first);
			psi.setInt(1, orderId);

			ResultSet rsf = psi.executeQuery();

			if (rsf.next()) {
				if (rsf.getString(1).equals("created")) {
					return -1;
				}
			}
			
			Calendar cal = (Calendar) new kt150599_GeneralOperations().getCurrentTime().clone();
			java.util.Date d = cal.getTime();
			java.sql.Date sqlDate = new java.sql.Date(d.getTime());
			
			String s="select idbuyer, assemblingtime from [order] where id=?";
			
			PreparedStatement ps=connection.prepareStatement(s);
			ps.setInt(1, orderId);
			ResultSet rs=ps.executeQuery();
			if(rs.next()) {
				int who=rs.getInt(1);
				java.sql.Date sqlDate1=rs.getDate(2);
				//java.util.Date javaDate=new java.util.Date(sqlDate.getTime());
				//java.util.Date javaDate1=new java.util.Date(sqlDate1.getTime());
				if(sqlDate.before(sqlDate1)) {
					
					String s1="select lo.idcity from location lo where arrivingtime = ? and lo.idorder=?";
							
					PreparedStatement ps1=connection.prepareStatement(s1);
					
					ps1.setDate(1, sqlDate1);
					ps1.setInt(2, orderId);
					ResultSet rs1=ps1.executeQuery();
					if(rs1.next()) {
						
						return rs1.getInt(1);
					}
					
				} else {
					String ss="select lo.idcity from location lo where lo.idorder=? and lo.arrivingtime = ?";
					String ssa= "select max(l.arrivingtime) from location l where l.idorder=? and l.arrivingtime <= ?";
					
					PreparedStatement psa=connection.prepareStatement(ssa);
					psa.setInt(1, orderId);
					psa.setDate(2, sqlDate);
					ResultSet rsa=psa.executeQuery();
					java.sql.Date sd=null;
					if(rsa.next()) {
						
						java.util.Date jd=rsa.getDate(1);
						sd=new java.sql.Date(jd.getTime());
					}
					
					
					PreparedStatement ps1=connection.prepareStatement(ss);
					
					ps1.setInt(1, orderId);
					//ps.setInt(2, orderId);
					ps1.setDate(2, sd);
					
					ResultSet rs1=ps1.executeQuery();
					if(rs1.next()) {
						
						return rs1.getInt(1);
					}
					
				}
			}
			
			
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return -1;
	}

}

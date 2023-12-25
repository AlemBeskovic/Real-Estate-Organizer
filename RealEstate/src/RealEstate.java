import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;




import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ListIterator;

import javax.swing.*;

public class RealEstate extends JFrame {

	private LocalDate lastUpdateDate;
	
	
	
	private JPanel contentPane;
	private static final long serialVersionUID = 2468249186397635984L;
	 static File log = new File("tenantData.txt");
	ObjectOutputStream oos = null;
	static ObjectInputStream ois = null;
	static ListIterator li = null;
	static ArrayList<RealEstate> tenantData = new ArrayList<RealEstate>();
	public String firstName;
	public String lastName;
	public int apartmentNumber;
	public String streetAddress;
	public int streetNumber;
	public String town;
	public String state;
	public int balance;
	public int deposit;
	public int monthRent;
	private JButton Add, Update, Delete;
	private final String button1Text = "Add";
	 private final String button2Text = "Update";
	 private final String button3Text = "Delete";
	 

	 
	 
	 String text;
	 DefaultListModel<RealEstate> model = new DefaultListModel<>();
	
	
	RealEstate( String firstName, String LastName, int apartmentNumber, String streetAddress, int streetNumber, String town, String state, int monthRent, int balance)
	 {
		
		 this.firstName = firstName;
		 this.lastName = LastName;
		 this.apartmentNumber = apartmentNumber;
		 this.streetAddress = streetAddress;
		 this.streetNumber = streetNumber;
		 this.town = town;
		 this.state = state;
		 this.monthRent = monthRent;
		 this.balance = balance;
		 this.lastUpdateDate = LocalDate.now();
	 }
	
	 
	 @Override
	 public String toString() {
		 String str =  firstName + " " + lastName + " | Apartment: " +apartmentNumber+ " | Street: " +streetNumber+ " "+ streetAddress+ ", " +town+ ", " +state+
	" | Monthly Rent: $"  + monthRent+  " | Balance: $" + balance ;
					 
			return str;

	}
	 // balance updater
	 public void updateBalanceAtStartOfMonth() {
		    LocalDate currentDate = LocalDate.now();
		    
		    if (currentDate.getDayOfMonth() == 1 && !currentDate.equals(lastUpdateDate)) {
		        // It's the first day of the month and the balance hasn't been updated yet
		        balance = monthRent + balance;
		        lastUpdateDate = currentDate;

		        
		        System.out.println("Balance updated to the monthly rent amount at the start of the month.");
		    }
		}
	 
	 
	 
	 
	 
	 
	 
	
	 public void updateBalanceDatabase(String firstName, String lastName, int balance) {
		 Connection connection = null;
		 PreparedStatement preparedStatement = null;
		 try {
			 Class.forName("com.mysql.cj.jdbc.Driver");
			 
			 connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/TenantEclipse", "root", "");
			 
			 String updateSQL = "UPDATE personfield1 SET balance = ? WHERE firstName = ? AND lastName = ?";
			 
			 preparedStatement = connection.prepareStatement(updateSQL);
			 
			 preparedStatement.setInt(1, balance);
			 preparedStatement.setString(2, firstName);
			 preparedStatement.setString(3, lastName);
			 preparedStatement.executeUpdate();
			 System.out.println("Data inserted into the database.");
		    } catch (Exception e) {
		        e.printStackTrace();
		    } finally {
		        try {
		            if (preparedStatement != null) {
		                preparedStatement.close();
		            }
		            if (connection != null) {
		                connection.close();
		            }
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		 }
		 
	 
	
	 
	 
	 
	 
	 // inserting the object's data into mySQL
	 public void insertIntoDatabase() {
		    Connection connection = null;
		    PreparedStatement preparedStatement = null;

		    try {
		        // Load the MySQL Connector/J driver
		        Class.forName("com.mysql.cj.jdbc.Driver");

		        // Establish a database connection
		        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/TenantEclipse", "root", "");

		        // Define your SQL insert statement
		        String insertSQL = "INSERT INTO personfield1 (firstName, lastName, apartmentNumber, streetAddress, streetNumber, town, state, monthRent, balance) " +
		                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		        // Create a prepared statement
		        preparedStatement = connection.prepareStatement(insertSQL);

		        // Set the values for the placeholders in the SQL statement
		        preparedStatement.setString(1, this.firstName);
		        preparedStatement.setString(2, this.lastName);
		        preparedStatement.setInt(3, this.apartmentNumber);
		        preparedStatement.setString(4, this.streetAddress);
		        preparedStatement.setInt(5, this.streetNumber);
		        preparedStatement.setString(6, this.town);
		        preparedStatement.setString(7, this.state);
		        preparedStatement.setInt(8, this.monthRent);
		        preparedStatement.setInt(9, this.balance);

		        // Execute the insert statement
		        preparedStatement.executeUpdate();

		        System.out.println("Data inserted into the database.");
		    } catch (Exception e) {
		        e.printStackTrace();
		    } finally {
		        try {
		            if (preparedStatement != null) {
		                preparedStatement.close();
		            }
		            if (connection != null) {
		                connection.close();
		            }
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		}
	 public void deletePersonFromDatabase(String firstName, String lastName) {
		    Connection connection = null;
		    PreparedStatement preparedStatement = null;

		    try {
		        // Load the MySQL Connector/J driver
		        Class.forName("com.mysql.cj.jdbc.Driver");

		        // Establish a database connection
		        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/TenantEclipse", "root", "");

		        // Define your SQL delete statement
		        String deleteSQL = "DELETE FROM personfield1 WHERE firstName = ? AND lastName = ?";

		        // Create a prepared statement
		        preparedStatement = connection.prepareStatement(deleteSQL);

		        // Set the values for the placeholders in the SQL statement
		        preparedStatement.setString(1, firstName);
		        preparedStatement.setString(2, lastName);

		        // Execute the delete statement
		        int rowsDeleted = preparedStatement.executeUpdate();

		        if (rowsDeleted > 0) {
		            System.out.println("Person deleted from the database.");
		        } else {
		            System.out.println("Person not found in the database.");
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    } finally {
		        try {
		            if (preparedStatement != null) {
		                preparedStatement.close();
		            }
		            if (connection != null) {
		                connection.close();
		            }
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		}


	
	
	 
	
	
	 public String getFirstName() {
		 return firstName;
	 }
	 public void setFirstName(String firstName) {
		 this.firstName = firstName;
	 }
	 public String getLastName() {
		 return lastName;
	 }
	 public void setLastName(String lastName) {
		 this.lastName = lastName;
	 }
	 public int getApartmentNum() {
		 return apartmentNumber;
	 }
	 public void setApartmentNum(int apartmentNumber) {
		 this.apartmentNumber = apartmentNumber;
	 }
	 public String getStreetAddress() {
		 return streetAddress;
	 }
	 public void setStreetAddress(String streetAddress) {
		 this.streetAddress = streetAddress;
	 }
	 public int getStreetNumber() {
		 return streetNumber;
	 }
	 public void setStreetNumber(int streetNumber) {
		 this.streetNumber = streetNumber;
	 }
	 public String getTown() {
		 return town;
	 }
	 public void setTown(String town) {
		 this.town = town;
	 }
	 public String getState1() {
		 return state;
	 }
	 public void setState(String state) {
		 this.state = state;
	 }
	 public int getBalance() {
		 return balance;
	 }
	 public void setBalance(int balance) {
		 this.balance = balance;
	 }
	 public int getMonthRent() {
		 return monthRent;
	 }
	 public void setMonthRent(int rent) {
		 this.monthRent = rent;
	 }
	 public LocalDate getDate() {
		 return lastUpdateDate;
	 }
	 
	 
	 public DefaultListModel<RealEstate> textAreaText() {
		
		for(RealEstate s : tenantData) {
			model.addElement(s);
		 }
		return model;
	 }
	 
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		
		   
		
		EventQueue.invokeLater(new Runnable() {
		        public void run() {
		            try {
		                RealEstate frame = new RealEstate();
		                frame.setVisible(true);
		            } catch (Exception e) {
		                e.printStackTrace();
		            }
		        }
		    });

		    if (log.isFile()) {
		        ois = new ObjectInputStream(new FileInputStream(log));
		        tenantData = (ArrayList<RealEstate>) ois.readObject();
		        ois.close();
		    }
		    
		    // Iterate through the tenantData list and update balances for each tenant
		    for (RealEstate tenant : tenantData) {
		        tenant.updateBalanceAtStartOfMonth();
		    }

		    // Print tenant information
		    li = tenantData.listIterator();
		    while (li.hasNext()) {
		        RealEstate z = (RealEstate) li.next();
		        System.out.println(z.toString());
		    }
		   
		}

	/**
	 * Create the frame.
	 */
	public RealEstate() {
		buttonHandler button = new buttonHandler();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(300, 300, 1000, 600);
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(6, 78, 263, 49);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Add Tenant");
		lblNewLabel_1.setBounds(6, 16, 136, 16);
		panel.add(lblNewLabel_1);
		
		Add = new JButton(button1Text);
		Add.setBounds(140, 11, 117, 29);
		panel.add(Add);
		Add.addActionListener(button);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(6, 139, 263, 49);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel_2 = new JLabel("Update Tenant");
		lblNewLabel_2.setBounds(6, 16, 131, 16);
		panel_1.add(lblNewLabel_2);
		
		Update = new JButton(button2Text);
		Update.setBounds(140, 11, 117, 29);
		panel_1.add(Update);
		Update.addActionListener(button);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(6, 200, 263, 49);
		getContentPane().add(panel_2);
		panel_2.setLayout(null);
		
		JLabel lblNewLabel_3 = new JLabel("Delete Tenant");
		lblNewLabel_3.setBounds(6, 16, 129, 16);
		panel_2.add(lblNewLabel_3);
		
		Delete = new JButton(button3Text);
		Delete.setBounds(140, 11, 117, 29);
		panel_2.add(Delete);
		Delete.addActionListener(button);
		
		
		
		JPanel panel_4 = new JPanel();
		panel_4.setBounds(6, 278, 263, 267);
		getContentPane().add(panel_4);
		panel_4.setLayout(null);
		
		JLabel lblNewLabel_4 = new JLabel("New label");
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_4.setIcon(new ImageIcon(RealEstate.class.getResource("/Image/RealEstatePic.png")));
		lblNewLabel_4.setBounds(6, 6, 251, 255);
		panel_4.add(lblNewLabel_4);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBounds(6, 6, 263, 34);
		getContentPane().add(panel_5);
		panel_5.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Real Estate Evaluator");
		lblNewLabel.setBounds(6, 6, 251, 22);
		panel_5.add(lblNewLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(281, 6, 713, 560);
		getContentPane().add(scrollPane);
		
	
		JList list = new JList((DefaultListModel) textAreaText());
		scrollPane.setViewportView(list);
		
		
		
		
		
	}
	 public class buttonHandler implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				String str = e.getActionCommand();
				if (str.equals(button1Text)) {
					 new AddTenant().setVisible(true);
				}else if(str.equals(button2Text)) {
					new Updatetenant().setVisible(true);;
				}
				else if(str.equals(button3Text)) {
					new Delete().setVisible(true);
					 
				}
}
	 }
	 
	 public class AddTenant extends JFrame{
		 
		 private static final long serialVersionUID = 1L;
		JLabel label1, label2, label3, label4, label5, label6, label7, label8;
		 JTextField textfield1, textfield2, textfield3, textfield4, textfield5, textfield6, textfield7, textfield8;
		 JButton addTenant;
		 private final String addTenantString = "Add Tenant";
		 public AddTenant() {
			 final int width = 1200;
				final int height = 500;
				setTitle("Add Tenant");
				setSize(width, height);
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				buildPanel1();
				getContentPane().setLayout(new GridLayout(10,1));
				setVisible(true);
		 }
		public void  buildPanel1(){
			JPanel panel = new JPanel();
			JPanel panel2 = new JPanel();
			JPanel panel3 = new JPanel();
			JPanel panel4 = new JPanel();
			JPanel panel5 = new JPanel();
			JPanel panel6 = new JPanel();
			JPanel panel7 = new JPanel();
			JPanel panel8 = new JPanel();
			JPanel panel9 = new JPanel();
			buttonHandler1 button1 = new buttonHandler1();
			
			label1 = new JLabel("First Name");
			textfield1 = new JTextField(20);
			
			label2 = new JLabel("Last Name");
			textfield2 = new JTextField(20);
			
			label3 = new JLabel("Street");
			textfield3 = new JTextField(20);
			
			label4 = new JLabel("Street Number");
			textfield4 = new JTextField(6);
			
			label5 = new JLabel("Apartment Number");
			textfield5 = new JTextField(3);
			
			label6 = new JLabel("Town");
			textfield6 = new JTextField(15);
			
			label7 = new JLabel("State ex. CT, NY, etc.");
			textfield7 = new JTextField(2);
			
			label8 = new JLabel("Monthly Rent");
			textfield8 = new JTextField(4);
			
			addTenant = new JButton(addTenantString);
			addTenant.addActionListener(button1);
			
			panel.add(label1);
			panel.add(textfield1);
			
			panel2.add(label2);
			panel2.add(textfield2);
			
			panel3.add(label3);
			panel3.add(textfield3);
			
			panel4.add(label4);
			panel4.add(textfield4);
			
			panel5.add(label5);
			panel5.add(textfield5);
			
			panel6.add(label6);
			panel6.add(textfield6);
			
			panel7.add(label7);
			panel7.add(textfield7);
			
			panel8.add(label8);
			panel8.add(textfield8);
			
			panel9.add(addTenant);
			add(panel);
			add(panel2);
			add(panel3);
			add(panel4);
			add(panel5);
			add(panel6);
			add(panel7);
			add(panel8);
			add(panel9);
			
		 }
		public class buttonHandler1 implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				String str = e.getActionCommand();
				if (str.equals(addTenantString)) {
					
					
					String firstName = textfield1.getText(), lastName = textfield2.getText(), Street = textfield3.getText(), 
							Town = textfield6.getText(), State = textfield7.getText();
					int streetNumber = Integer.parseInt(textfield4.getText()), apartmentNumber = Integer.parseInt(textfield5.getText());
					int MonthlyRent = Integer.parseInt(textfield8.getText());
					int balance = MonthlyRent;
					tenantData.add(new RealEstate(firstName, lastName, apartmentNumber, Street, streetNumber, Town, State, MonthlyRent, balance));
					tenantData.get(tenantData.size() - 1).insertIntoDatabase();
					
					
					
					
				}
				else {
					JOptionPane.showMessageDialog(null,"Something went wrong, check the information and try again");
				}
				try {
				oos = new ObjectOutputStream(new FileOutputStream(log));
				
					oos.writeObject(tenantData);
					oos.close();
					
				} catch (IOException e1) {
				
					e1.printStackTrace();
				}
				dispose();
				new RealEstate().setVisible(true);
	 }
}
	 }
	 public class Updatetenant extends JFrame{
			private static final long serialVersionUID = 1L;
			JLabel label, label2;
			JTextField field, paymentAmount;
			JButton update;
			private final String updateTenant = "Update";
			public Updatetenant() {
				 final int width = 1200;
					final int height = 500;
					setTitle("Delete Tenant");
					setSize(width, height);
					setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					buildPanel3();
					getContentPane().setLayout(new GridLayout(3,1));
					setVisible(true);
			}
			public void buildPanel3() {
				JPanel panel = new JPanel();
				ButtonHandler4 b = new ButtonHandler4();
				label = new JLabel("Enter first and last name of tenant you would like to update");
				field = new JTextField(25);
				label2 = new JLabel("Enter the amount that the tenant has payed");
				paymentAmount = new JTextField(4);
				update = new JButton(updateTenant);
				update.addActionListener(b);
				panel.add(label);
				panel.add(field);
				panel.add(label2);
				panel.add(paymentAmount);
				panel.add(update);
				add(panel);
			}
			public class ButtonHandler4 implements ActionListener{
				public void actionPerformed(ActionEvent e) {
					String str = e.getActionCommand();
					if(str.equals(updateTenant)) {
						String line = field.getText();
						String payment = paymentAmount.getText();
						int payment1 = Integer.parseInt(payment);
						String[] parts = line.split(" ");
						String firstName = parts[0];
						String lastName = parts[1];
						
						
						try {
							   if(log.isFile()) {
								   
								
									ois = new ObjectInputStream(new FileInputStream(log));
									Object obj = ois.readObject();
									tenantData = (ArrayList<RealEstate>) obj;
									
									ois.close();
									boolean found = false;
									li = tenantData.listIterator();
									
									while(li.hasNext()) {
										RealEstate q = (RealEstate)li.next();
										if(q.firstName.equalsIgnoreCase(firstName) && q.lastName.equalsIgnoreCase(lastName) ) {
											int newBalance = q.getBalance()-payment1;
											q.setBalance(newBalance);
								            found = true;
								            tenantData.get(tenantData.size() - 1).updateBalanceDatabase(firstName, lastName, newBalance);
										}
									}
									if(found) {
										oos = new ObjectOutputStream(new FileOutputStream(log));
										oos.writeObject(tenantData);
										oos.close();
										JOptionPane.showMessageDialog(null, "Balance Updated Successfully");
									}
									
									else {
										JOptionPane.showMessageDialog(null, "Person not found");
										
									}
							   }
							   
							  
								} catch (FileNotFoundException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} catch (ClassNotFoundException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
						dispose();
						new RealEstate().setVisible(true);
								
								}
						
			        
						
					}
				}
		 
	 }
	 
	 public class Delete extends JFrame{
		 private static final long serialVersionUID = 1L;
		JLabel label;
			JTextField field;
			JButton delete;
			private final String deleteTenant = "Delete";
			public Delete() {
				 final int width = 1200;
					final int height = 500;
					setTitle("Delete Tenant");
					setSize(width, height);
					setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					buildPanel2();
					getContentPane().setLayout(new GridLayout(3,1));
					setVisible(true);
			}
			public void buildPanel2() {
				JPanel panel = new JPanel();
				ButtonHandler3 b = new ButtonHandler3();
				label = new JLabel("Enter first and last name of tenant you would like to delete");
				field = new JTextField(25);
				delete = new JButton(deleteTenant);
				delete.addActionListener(b);
				panel.add(label);
				panel.add(field);
				panel.add(delete);
				add(panel);
			}
			public class ButtonHandler3 implements ActionListener{
				public void actionPerformed(ActionEvent e) {
					String str = e.getActionCommand();
					if (str.equals(deleteTenant)) {
						String line = field.getText();
						String[] parts = line.split(" ");
						String firstName = parts[0];
						String lastName = parts[1];
						tenantData.get(tenantData.size() - 1).deletePersonFromDatabase(firstName, lastName);
						try {
					   if(log.isFile()) {
						   
						
							ois = new ObjectInputStream(new FileInputStream(log));
							Object obj = ois.readObject();
							tenantData = (ArrayList<RealEstate>) obj;
							
							ois.close();
							boolean found = false;
							li = tenantData.listIterator();
							
							while(li.hasNext()) {
								RealEstate q = (RealEstate)li.next();
								if(q.firstName.equalsIgnoreCase(firstName) && q.lastName.equalsIgnoreCase(lastName) ) {
									li.remove();
						            found = true;
								}
							}
							if(found) {
								oos = new ObjectOutputStream(new FileOutputStream(log));
								oos.writeObject(tenantData);
								oos.close();
								JOptionPane.showMessageDialog(null, "Record Deleted Successfully.");
							}
							
							else {
								JOptionPane.showMessageDialog(null, "Person not found");
								
							}
					   }
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (ClassNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						
						}
					  
					   
						
					   
					        
					dispose();
					new RealEstate().setVisible(true);
					 
				
					}
			}
		 
	 }
	 
	 
	 
	
}
	 
	 
	 
	 


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author akira
 */
public class PurchasesDao {

    //instantiate the connection
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    //register purchase
    public boolean registerPurchaseQuery(int supplier_id, int employee_id, double total) {
        String query = "INSERT INTO purchases (supplier_id, employee_id, total, created)"
                + "VALUES (?,?,?,?)";

        Timestamp datetime = new Timestamp(new Date().getTime());

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, supplier_id);
            pst.setInt(2, employee_id);
            pst.setDouble(3, total);
            pst.setTimestamp(4, datetime);
            pst.execute();
            return true;

        } catch (SQLException e) {
            JOptionPane.showInternalMessageDialog(null, "error when inserting the purchase");
            return false;
        }
    }

    //register detail purchases
    public boolean registerPurchaseDetailQuery(int purchase_id, double purchase_price, int purchase_amount,
            double purchase_subtotal, int product_id) {

        String query = "INSERT INTO purchase_details (purchase_id, purchase_price, purchase_amount,"
                + "purchase_subtotal, purchase_date, product_id) VALUES (?,?,?,?,?,?)";

        Timestamp datetime = new Timestamp(new Date().getTime());

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, purchase_id);
            pst.setDouble(2, purchase_price);
            pst.setInt(3, purchase_amount);
            pst.setDouble(4, purchase_subtotal);
            pst.setTimestamp(5, datetime);
            pst.setInt(6, product_id);
            pst.execute();
            return true;

        } catch (SQLException e) {
            JOptionPane.showInternalMessageDialog(null, "error when recording purchase details");
            return false;
        }
    }

    //get the purchase id
    public int purchaseId() {
        int id = 0;
        String query = "SELECT MAX(id) AS id FROM purchases";

        try {

            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return id;
    }

    //list all purchases made
    public List listAllPurchasesQuery() {
        List<Purchases> list_purchase = new ArrayList();
        String query = "SELECT pu.*, su.name AS supplier_name FROM purchases pu, suppliers su "
                + "WHERE pu.supplier_id = su.id ORDER BY pu.id ASC";

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();

            while (rs.next()) {
                Purchases purchase = new Purchases();
                purchase.setId(rs.getInt("id"));
                purchase.setSupplier_name_product(rs.getString("supplier_name"));
                purchase.setTotal(rs.getDouble("total"));
                purchase.setCreated(rs.getString("created"));
                list_purchase.add(purchase);
            }

        } catch (SQLException e) {
            JOptionPane.showInternalMessageDialog(null, e.getMessage());
        }
        return list_purchase;
    }

    //list purchases to print invoices
    public List listPurchaseDetailQuery(int id) {
        List<Purchases> list_purchases = new ArrayList();
        String query = "SELECT pu.created, pude.purchase_price, pude.purchase_amount, pude.purchase_subtotal, su.name AS supplier_name,\n"
                + "pro.name AS product_name, em.full_name FROM purchases pu INNER JOIN purchase_details pude ON pu.id = pude.purchase_id\n"
                + "INNER JOIN products pro ON pude.product_id = pro.id INNER JOIN suppliers su ON pu.supplier_id = su.id\n"
                + "INNER JOIN employees em ON pu.employee_id = em.id WHERE pu.id = ?";

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            rs = pst.executeQuery();

            while (rs.next()) {
                Purchases purchase = new Purchases();
                purchase.setProduct_name(rs.getString("product_name"));
                purchase.setPurchase_amount(rs.getInt("purchase_amount"));
                purchase.setPurchase_price(rs.getDouble("purchase_price"));
                purchase.setPurchase_subtotal(rs.getDouble("purchases_subtotal"));
                purchase.setSupplier_name_product(rs.getString("supplier_name"));
                purchase.setCreated(rs.getString("created"));
                purchase.setPurcharser(rs.getString("full_name"));
                list_purchases.add(purchase);

            }

        } catch (SQLException e) {
            JOptionPane.showInternalMessageDialog(null, e.getMessage());
        }
        return list_purchases;
    }

}

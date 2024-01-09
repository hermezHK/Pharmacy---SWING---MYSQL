/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import models.DynamicComboBox;
import static models.EmployeesDao.rol_user;
import models.Products;
import models.ProductsDao;
import models.Purchases;
import models.PurchasesDao;
import views.SystemView;

/**
 *
 * @author akira
 */
public class PurchasesController implements KeyListener, ActionListener {

    private Purchases purchase;
    private PurchasesDao purchaseDao;
    private SystemView views;
    private int getIdSupplier = 0;
    private int item = 0;
    DefaultTableModel model = new DefaultTableModel();
    DefaultTableModel temp;

    //instance model product
    Products product = new Products();
    ProductsDao productDao = new ProductsDao();

    String rol = rol_user;

    public PurchasesController(Purchases purchase, PurchasesDao purchaseDao, SystemView views) {
        this.purchase = purchase;
        this.purchaseDao = purchaseDao;
        this.views = views;

        //label listening
        this.views.btn_add_product_to_buy.addActionListener(this);
        this.views.txt_purchase_product_code.addKeyListener(this);
        this.views.txt_purchase_price.addKeyListener(this);
    }

    //ActionListener
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_add_product_to_buy) {
            DynamicComboBox supplier_cmb = (DynamicComboBox) views.cmb_purchase_supplier.getSelectedItem();
            int supplier_id = supplier_cmb.getId();

            if (getIdSupplier == 0) {
                getIdSupplier = supplier_id;
            } else {
                if (getIdSupplier != supplier_id) {
                    JOptionPane.showMessageDialog(null, "You cannot make the same purchase from multiple suppliers.");
                } else {
                    int amount = Integer.parseInt(views.txt_purchase_amount.getText());
                    String product_name = views.txt_purchase_product_name.getText();
                    double price = Double.parseDouble(views.txt_purchase_price.getText());
                    int purchase_id = Integer.parseInt(views.txt_purchase_id.getText());
                    String supplier_name = views.cmb_purchase_supplier.getSelectedItem().toString();

                    if (amount > 0) {
                        temp = (DefaultTableModel) views.purchases_table.getModel();
                        for (int i = 0; i < views.purchases_table.getRowCount(); i++) {
                            if (views.purchases_table.getValueAt(i, 1).equals(views.txt_purchase_product_name.getText())) {
                                JOptionPane.showMessageDialog(null, "The product is already registered in the purchases table");
                                return;
                            }
                        }

                        ArrayList list = new ArrayList();
                        item = 1;
                        list.add(item);
                        list.add(purchase_id);
                        list.add(product_name);
                        list.add(amount);
                        list.add(price);
                        list.add(amount * price);
                        list.add(supplier_name);

                        Object[] obj = new Object[6];
                        obj[0] = list.get(1);
                        obj[1] = list.get(2);
                        obj[2] = list.get(3);
                        obj[3] = list.get(4);
                        obj[4] = list.get(5);
                        obj[5] = list.get(6);
                        temp.addRow(obj);
                        views.purchases_table.setModel(temp);
                        cleanFieldsPurchases();
                        views.cmb_purchase_supplier.setEditable(false);
                        views.txt_purchase_product_code.requestFocus();
                        calculatePurchase();

                    }

                }
            }
        }
    }

    //KeyListener
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource() == views.txt_purchase_product_code) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (views.txt_purchase_product_code.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Enter the code of the product to buy");
                } else {
                    int id = Integer.parseInt(views.txt_purchase_product_code.getText());
                    product = productDao.searchCode(id);
                    views.txt_purchase_product_name.setText(product.getName());
                    views.txt_purchase_id.setText("" + product.getId());
                    views.txt_purchase_amount.requestFocus();
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == views.txt_purchase_price) {
            int quantity;
            double price = Double.parseDouble(views.txt_purchase_price.getText());
            price = 0.0;

            if (views.txt_purchase_amount.getText().equals("")) {
                quantity = 1;
                views.txt_purchase_price.setText("" + price);
            } else {
                quantity = Integer.parseInt(views.txt_purchase_amount.getText());
                price = Double.parseDouble(views.txt_purchase_price.getText());
                views.txt_purchase_subtotal.setText("" + quantity * price);
            }
        }
    }
    
    //clean fields
    public void cleanFieldsPurchases(){
        views.txt_purchase_product_name.setText("");
        views.txt_purchase_price.setText("");
        views.txt_purchase_amount.setText("");
        views.txt_purchase_product_code.setText("");
        views.txt_purchase_subtotal.setText("");
        views.txt_purchase_id.setText("");
        views.txt_purchase_total_to_pay.setText("");
    }
    
    //calculate total to pay
    public void calculatePurchase(){
        double total = 0.00;
        int numRow = views.purchases_table.getRowCount();
        
        for(int i = 0; i < numRow; i++){
            //obtain the index of the column to be added
            total = total + Double.parseDouble(String.valueOf(views.purchases_table.getValueAt(i, 4)));
        }
        views.txt_purchase_total_to_pay.setText("" + total);
    }
    

}

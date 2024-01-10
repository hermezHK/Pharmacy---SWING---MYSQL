/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import models.Customers;
import models.CustomersDao;
import static models.EmployeesDao.id_user;
import static models.EmployeesDao.rol_user;
import models.Products;
import models.ProductsDao;
import models.Sales;
import models.SalesDao;
import views.SystemView;

/**
 *
 * @author akira
 */
public class SalesController implements ActionListener, MouseListener, KeyListener {

    private Sales sale;
    private SalesDao saleDao;
    private SystemView views;
    private int item = 0;
    String rol = rol_user;
    Products product = new Products();
    ProductsDao productDao = new ProductsDao();

    DefaultTableModel model = new DefaultTableModel();
    DefaultTableModel temp;

    public SalesController(Sales sale, SalesDao saleDao, SystemView views, DefaultTableModel temp) {
        this.sale = sale;
        this.saleDao = saleDao;
        this.views = views;
        this.temp = temp;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_confirm_sale) {
            insertSale();

        } else if (e.getSource() == views.btn_new_sale) {
            cleanAllFieldsSales();
            cleanTableTemp();

        } else if (e.getSource() == views.btn_remove_sale) {
            model = (DefaultTableModel) views.sales_table.getModel();
            model.removeRow(views.sales_table.getSelectedRow());
            calculateSales();
            views.txt_sale_product_code.requestFocus();

        } else if (!"".equals(e.getSource() == views.btn_add_product_sale)) {

            //Agregar productos a la tabla de ventas temporalmente
            int amount = Integer.parseInt(views.txt_sale_quantity.getText());
            String product_name = views.txt_sale_product_name.getText();
            double price = Double.parseDouble(views.txt_sale_price.getText());
            int sale_id = Integer.parseInt(views.txt_sale_product_id.getText());
            double subtotal = amount * price;
            int stock = Integer.parseInt(views.txt_sale_stock.getText());
            String full_name = views.txt_sale_customer_name.getText();

            if (stock >= amount) {
                item = item + 1;
                temp = (DefaultTableModel) views.sales_table.getModel();

                for (int i = 0; i < views.sales_table.getRowCount(); i++) {
                    if (views.sales_table.getValueAt(i, 2).equals(views.txt_sale_product_name.getText())) {
                        JOptionPane.showMessageDialog(null, "The product is already registered in the sales table");
                        return;
                    }
                }

                ArrayList list = new ArrayList();
                list.add(item);
                list.add(sale_id);
                list.add(product_name);
                list.add(amount);
                list.add(price);
                list.add(subtotal);
                list.add(full_name);

                Object[] obj = new Object[6];
                obj[0] = list.get(1);
                obj[1] = list.get(2);
                obj[2] = list.get(3);
                obj[3] = list.get(4);
                obj[4] = list.get(5);
                obj[5] = list.get(6);

                temp.addRow(obj);
                views.sales_table.setModel(temp);
                calculateSales();
                cleanFieldsSales();
                views.txt_sale_product_code.requestFocus();

            } else {
                JOptionPane.showMessageDialog(null, "Stock not available");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Enter quantity");
        }
    }

    //List all sales
    public void listAllSales() {
        if (rol.equals("Admin")) {
            List<Sales> list = saleDao.listAllSalesQuery();
            model = (DefaultTableModel) views.table_all_sales.getModel();

            //Recorrer la lista
            Object[] row = new Object[5];

            for (int i = 0; i < list.size(); i++) {
                row[0] = list.get(i).getId();
                row[1] = list.get(i).getCustomer_name();
                row[2] = list.get(i).getEmployee_name();
                row[3] = list.get(i).getTotal_to_pay();
                row[4] = list.get(i).getSale_date();
                model.addRow(row);
            }
            views.table_all_sales.setModel(model);
        }
    }

    //MouseListener
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == views.jLabelSales) {
            views.jTabbedPane1.setSelectedIndex(2);
        } else if (e.getSource() == views.jLabelReports) {
            if (rol.equals("Admin")) {
                views.jTabbedPane1.setSelectedIndex(7);
                listAllSales();
            } else {
                views.jTabbedPane1.setEnabledAt(7, false);
                views.jLabelReports.setEnabled(false);
                JOptionPane.showMessageDialog(null, "You do not have administrator privileges to access this view");
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    //KeyListener
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource() == views.txt_sale_product_code) {

            if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                if (!"".equals(views.txt_sale_product_code.getText())) {

                    int code = Integer.parseInt(views.txt_sale_product_code.getText());
                    product = productDao.searchCode(code);

                    if (product.getName() != null) {
                        views.txt_sale_product_name.setText(product.getName());
                        views.txt_sale_product_id.setText("" + product.getId());
                        views.txt_sale_stock.setText("" + product.getProduct_quantity());
                        views.txt_sale_price.setText("" + product.getUnit_price());
                        views.txt_sale_quantity.requestFocus();

                    } else {
                        JOptionPane.showMessageDialog(null, "There is no product with that code");
                        cleanFieldsSales();
                        views.txt_sale_product_code.requestFocus();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Enter the code of the product to sell");
                }
            }
        }
    }else if (e.getSource() == views.txt_sale_customer_id) {
          if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            Customers customer = new Customers();
            CustomersDao customerDao = new CustomersDao();

            if (!"".equals(views.txt_sale_customer_id.getText())) {
                int customer_id = Integer.parseInt(views.txt_sale_customer_id.getText());
                customer = customerDao.searchCustomers(customer_id);

                if (customer.getFull_name() != null) {
                    views.txt_sale_customer_name.setText("" + customer.getFull_name());
                } else {
                    views.txt_sale_customer_id.setText("");
                    JOptionPane.showMessageDialog(null, "The client does not exist");
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == views.txt_sale_quantity) {

            int quantity;
            double price = Double.parseDouble(views.txt_sale_price.getText());

            if (views.txt_sale_quantity.getText().equals("")) {
                quantity = 1;
                views.txt_sale_price.setText("" + price);
            } else {
                quantity = Integer.parseInt(views.txt_sale_quantity.getText());
                price = Double.parseDouble(views.txt_sale_price.getText());
                views.txt_sale_subtotal.setText("" + quantity * price);
            }
        }
    }

    private void insertSale() {

        int customer_id = Integer.parseInt(views.txt_sale_customer_id.getText());
        int employee_id = id_user;
        double total = Double.parseDouble(views.txt_sale_total_to_pay.getText());

        if (saleDao.registerSaleQuery(customer_id, employee_id, total)) {

            Products product = new Products();
            ProductsDao productDao = new ProductsDao();
            int sale_id = saleDao.saleId();

            // registerPurchaseDetailQuery();
            for (int i = 0; i < views.sales_table.getRowCount(); i++) {
                int product_id = Integer.parseInt(views.sales_table.getValueAt(i, 0).toString());
                int sale_quantity = Integer.parseInt(views.sales_table.getValueAt(i, 2).toString());
                double sale_price = Double.parseDouble(views.sales_table.getValueAt(i, 3).toString());
                double sale_subtotal = sale_quantity * sale_price;

                saleDao.registerSaleDetailQuery(product_id, sale_id, sale_quantity, sale_price, sale_subtotal);

                //Bring the quantity of products
                product = productDao.searchId(product_id);
                //Get current quantity and subtract purchased quantity
                int amount = product.getProduct_quantity() - sale_quantity;
                productDao.updateStockQuery(amount, product_id);
            }
            JOptionPane.showMessageDialog(null, "Sale generated");
            cleanTableTemp();
            cleanAllFieldsSales();
        }
    }

    //Calculate total to pay sales table
    private void calculateSales() {
        double total = 0.00;
        int numRow = views.sales_table.getRowCount();
        for (int i = 0; i < numRow; i++) {
            total = total + Double.parseDouble(String.valueOf(views.sales_table.getValueAt(i, 4)));
        }
        views.txt_sale_total_to_pay.setText("" + total);
    }

    //clean table
    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }

    //Clear temporary table
    public void cleanTableTemp() {
        for (int i = 0; i < temp.getRowCount(); i++) {
            temp.removeRow(i);
            i = i - 1;
        }
    }

    // Clear some fields
    public void cleanFieldsSales() {
        views.txt_sale_product_code.setText("");
        views.txt_sale_product_name.setText("");
        views.txt_sale_quantity.setText("");
        views.txt_sale_product_id.setText("");
        views.txt_sale_price.setText("");
        views.txt_sale_subtotal.setText("");
        views.txt_sale_stock.setText("");
    }

    //clean all fields
    public void cleanAllFieldsSales() {
        views.txt_sale_product_code.setText("");
        views.txt_sale_product_name.setText("");
        views.txt_sale_quantity.setText("");
        views.txt_sale_product_id.setText("");
        views.txt_sale_price.setText("");
        views.txt_sale_subtotal.setText("");
        views.txt_sale_customer_id.setText("");
        views.txt_sale_customer_name.setText("");
        views.txt_sale_total_to_pay.setText("");
        views.txt_sale_stock.setText("");
    }
}

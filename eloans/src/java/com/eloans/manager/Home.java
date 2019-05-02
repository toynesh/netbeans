/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eloans.manager;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author coolie
 */
public class Home extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */

            HttpSession session = request.getSession();
            if (null == session.getAttribute("fullname")) {
                String redirectURL = "Login";
                response.sendRedirect(redirectURL);
            }

            String fullname = (String) request.getSession().getAttribute("fullname");
            
            DataStore data = new DataStore();

            String newborrower = "<h4 class='text-center'>New Borrower</h4>"
                    + "                              <form  action='/eloans/SaveNewBorrower' method='post'>"
                    + "                                <div class='form-row'>"
                    + "                                    <div class='form-group col-md-6'>"
                    + "                                        <label for='fname'>First Name</label>"
                    + "                                        <input type='text' class='form-control' id='fname' name='fname' placeholder='First Name'>"
                    + "                                    </div>"
                    + "                                    <div class='form-group col-md-6'>"
                    + "                                        <label for='lname'>Last Name</label>"
                    + "                                        <input type='text' class='form-control' id='lname' name='lname' placeholder='Last Name'>"
                    + "                                    </div>"
                    + "                                </div>"
                    + "                                <div class='form-row'>"
                    + "                                    <div class='form-group col-md-4'>"
                    + "                                        <label for='city'>City</label>"
                    + "                                        <input type='text' class='form-control' id='city' name='city'>"
                    + "                                    </div>"
                    + "                                    <div class='form-group col-md-4'>"
                    + "                                        <label for='idnumber'>ID/Passport</label>"
                    + "                                        <input type='text' class='form-control' id='idnumber' name='idnumber'>"
                    + "                                    </div>"
                    + "                                    <div class='form-group col-md-4'>"
                    + "                                        <label for='phone'>Phone:</label>"
                    + "                                        <input type='text' class='form-control' id='phone' name='phone'>"
                    + "                                    </div>"
                    + "                                </div>"
                    + "                                <div class='form-group'>"
                    + "                                    <label for='address1'>Address</label>"
                    + "                                    <input type='text' class='form-control' id='address1' name='address1' placeholder='1234 Muranga'>"
                    + "                                </div>"
                    + "                                <div class='form-group'>"
                    + "                                    <label for='address2'>Address 2</label>"
                    + "                                    <input type='text' class='form-control' id='address2' name='address2' placeholder='Apartment, studio, or floor'>"
                    + "                                </div>"
                    + "                                <button type='submit' class='btn btn-success'>Add</button>"
                    + "                            </form>";

            String equery = "SELECT `fname` as 'firstname',`lname` as 'lastname',`city`,`idnumber`,`phone`,`address1`,`address2` FROM `borrowers` ORDER BY `id` DESC";
            if (null != request.getParameter("listborrower")) {
                equery = "SELECT `fname` as 'firstname',`lname` as 'lastname',`city`,`idnumber`,`phone`,`address1`,`address2` FROM `borrowers` ORDER BY `id` DESC";
            } else if (null != request.getParameter("listloans")) {
                equery = "SELECT `fullname` as 'borrower',`phone`,`amount`,`netamount` as 'dueamount',`totalpaid` as 'paidamount',`issuedate`,`expecteddate` as 'duedate' FROM  `loans` ORDER BY `id` DESC";
            } else if (null != request.getParameter("blacklist")) {
                equery = "";
            } else if (null != request.getParameter("report")) {
                equery = "SELECT `borrowerid`,`amount` as paidamt,`balance`,`collateral`,`notes`,`paydate` FROM  `paymenttrend`";
            }
            String oquery = URLEncoder.encode(equery, "UTF-8");
            String listborrowers = "<h4 class='text-center'>Borrowers List  <span class='pull-right'><a href='" + request.getContextPath() + "/ExportExcel?query=" + oquery + "'><i class='far fa-file-excel'></i> Excel</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href='" + request.getContextPath() + "/ExportPDF?query=" + oquery + "'><i class='far fa-file-pdf'></i> PDF</a></span></h4>"
                    + "                            <table class='table'>"
                    + "                                <thead>"
                    + "                                    <tr>"
                    + "                                        <th>Firstname</th>"
                    + "                                        <th>Lastname</th>"
                    + "                                        <th>City</th>"
                    + "                                        <th>IDnumber</th>"
                    + "                                        <th>Phone</th>"
                    + "                                        <th>address1</th>"
                    + "                                        <th>address2</th>"
                    + "                                    </tr>"
                    + "                                </thead>"
                    + "                                <tbody style='font-size:12px'>";

            String lb2 = "</tbody></table>";

            String listloans = "<h4 class='text-center'>Loans  <span class='pull-right'><a href='" + request.getContextPath() + "/ExportExcel?query=" + oquery + "'><i class='far fa-file-excel'></i> Excel</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href='" + request.getContextPath() + "/ExportPDF?query=" + oquery + "'><i class='far fa-file-pdf'></i> PDF</a></span></h4>"
                    + "                            <table class='table'>"
                    + "                                <thead>"
                    + "                                    <tr>"
                    + "                                        <th>Borrower</th>"
                    + "                                        <th>Phone</th>"
                    + "                                        <th>Amount</th>"
                    + "                                        <th>DueAmount</th>"
                    + "                                        <th>Payedmount</th>"
                    + "                                        <th>IssueDate</th>"
                    + "                                        <th>DueDate</th>"
                    + "                                    </tr>"
                    + "                                </thead>"
                    + "                                <tbody style='font-size:12px'>";

            String ll2 = "</tbody></table>";
            String listblacklist = "<h4 class='text-center'>Blacklist  <span class='pull-right'><a href='#'><i class='far fa-file-excel'></i> Excel</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href='#'><i class='far fa-file-pdf'></i> PDF</a></span></h4>"
                    + "                            <table class='table'>"
                    + "                                <thead>"
                    + "                                    <tr>"
                    + "                                        <th>Borrower</th>"
                    + "                                        <th>Phone</th>"
                    + "                                        <th>Amount</th>"
                    + "                                        <th>DueAmount</th>"
                    + "                                        <th>Payedmount</th>"
                    + "                                        <th>IssueDate</th>"
                    + "                                        <th>DueDate</th>"
                    + "                                    </tr>"
                    + "                                </thead>"
                    + "                                <tbody style='font-size:12px'>";

            String lblklst2 = "</tbody></table>";
            String listreport = "<h4 class='text-center'>Report  <span class='pull-right'><a href='" + request.getContextPath() + "/ExportExcel?query=" + oquery + "'><i class='far fa-file-excel'></i> Excel</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href='" + request.getContextPath() + "/ExportPDF?query=" + oquery + "'><i class='far fa-file-pdf'></i> PDF</a></span></h4>"
                    + "                            <table class='table'>"
                    + "                                <thead>"
                    + "                                    <tr>"
                    + "                                        <th>Borrower</th>"
                    + "                                        <th>Phone</th>"
                    + "                                        <th>PaidAmt</th>"
                    + "                                        <th>Balance</th>"
                    + "                                        <th>Collateral</th>"
                    + "                                        <th>Notes</th>"
                    + "                                        <th>Paydate</th>"
                    + "                                    </tr>"
                    + "                                </thead>"
                    + "                                <tbody style='font-size:12px'>";

            String lrp2 = "</tbody></table>";

            DateTime dt = new DateTime();
            DateTime now = DateTime.now();
            DateTime oneMonthsLater = now.plusMonths(1);
            DateTimeFormatter fmt = DateTimeFormat.forPattern("dd-MM-yyyy");
            String currDate = fmt.print(dt);
            String epaydate = fmt.print(oneMonthsLater);

            String custname = "";
            String brwid = "";
            if (null != request.getParameter("brw")) {
                brwid = request.getParameter("brw");
                custname = data.getBorrowername(brwid);
            }

            String newloan = "<h4 class='text-center'>New Loan</h4>"
                    + "                            <form action='/eloans/SaveNewLoan' method='post'>"
                    + "                                <div class='form-row'>"
                    + "                                    <div class='form-group col-md-4'>"
                    + "                                        <label for='phone'>Search Phone</label>"
                    + "                                        <select id='phone' onchange=\"document.location.href='" + request.getContextPath() + "/Home?newloan=1&brw='+this.value\" class='form-control'>"
                    + "                                            <option selected>Choose...</option>"
                    + "                                            " + aborrowers() + ""
                    + "                                        </select>"
                    + "                                    </div>"
                    + "                                    <div class='form-group col-md-8'>"
                    + "                                        <label for='fullname'>Borrower</label>"
                    + "                                        <input type='text' class='form-control' id='fullname' name='fullname' value='" + custname + "' readonly='true' required>"
                    + "                                        <input type='text' class='form-control' id='bid' name='bid' value='" + brwid + "' style='display:none'>"
                    + "                                    </div>"
                    + "                                </div>"
                    + "                                <div class='form-row'>"
                    + "                                    <div class='form-group col-md-3'>"
                    + "                                        <label for='amount'>Amount KES</label>"
                    + "                                        <input type='text' class='form-control' id='amount' name='amount' onfocusout='calcintr()' placeholder='0.00' required>"
                    + "                                    </div>"
                    + "                                    <div class='form-group col-md-3'>"
                    + "                                        <label for='idate'>Issue Date</label>"
                    + "                                        <input type='text' class='form-control' id='idate' name='idate' value='" + currDate + "' readonly='true'>"
                    + "                                    </div>"
                    + "                                    <div class='form-group col-md-3'>"
                    + "                                        <label for='epdate'>Expected Pay Date</label>"
                    + "                                        <input type='text' class='form-control' id='epdate' name='epdate' value='" + epaydate + "' readonly='true'>"
                    + "                                    </div>"
                    + "                                    <div class='form-group col-md-3'>"
                    + "                                     <script type='text/javascript'>"
                    + "                                         function calcintr(){"
                    + "                                             var amt = parseFloat(document.getElementById('amount').value);"
                    + "                                             document.getElementById('interest').value=amt*0.2;"
                    + "                                             document.getElementById('netamount').value=amt+(amt*0.2);"
                    + "                                         }"
                    + "                                      </script>"
                    + "                                        <label for='interest'>Interest KES</label>"
                    + "                                        <input type='text' class='form-control' id='interest' name='interest' readonly='true'>"
                    + "                                    </div>"
                    + "                                </div>"
                    + "                                <div class='form-row'>"
                    + "                                    <div class='form-group col-md-12'>"
                    + "                                        <label for='netamount'>Net Amount KES</label>"
                    + "                                        <input type='text' class='form-control' id='netamount' name='netamount' readonly='true'>"
                    + "                                    </div>"
                    + "                                </div>"
                    + "                                <div class='form-row'>"
                    + "                                    <div class='form-group col-md-3'>"
                    + "                                        <button type='submit' class='btn btn-success'>Add</button>"
                    + "                                    </div>"
                    + "                                </div>"
                    + "                            </form>";

            String loanname = "";
            String loanid = "";
            String borrwerid = "";
            Double balance = 0.0;
            String loanamount = "";
            String interest = "";
            String totalpaid = "";
            if (null != request.getParameter("ln")) {
                loanid = request.getParameter("ln");
                String sloan = data.getLoanByID(loanid);
                String sloanarr[] = sloan.split("-");
                loanname = sloanarr[0] + " - " + sloanarr[1];
                balance = Double.parseDouble(sloanarr[2]) - Double.parseDouble(sloanarr[3]);
                loanamount = sloanarr[2];
                totalpaid = sloanarr[3];
                interest = sloanarr[4];
                borrwerid = sloanarr[5];

            }
            String payloan = "<h4 class='text-center'>Pay Loan</h4>"
                    + "                            <form action='/eloans/SavePayLoan' method='post'>"
                    + "                                <div class='form-row'>"
                    + "                                    <div class='form-group col-md-4'>"
                    + "                                        <label for='phone'>Phone:</label>"
                    + "                                        <select id='phone' onchange=\"document.location.href='" + request.getContextPath() + "/Home?payloan=1&ln='+this.value\" class='form-control'>"
                    + "                                            <option selected>Choose...</option>"
                    + "                                            " + aopenloans() + ""
                    + "                                        </select>"
                    + "                                    </div>"
                    + "                                    <div class='form-group col-md-8'>"
                    + "                                        <label for='fullname'>Full Name:</label>"
                    + "                                        <input type='text' class='form-control' id='fullname' name='fullname' value='" + loanname + "' readonly='true'>"
                    + "                                        <input type='text' class='form-control' id='lid' name='lid' value='" + loanid + "' style='display:none'>"
                    + "                                        <input type='text' class='form-control' id='bid' name='bid' value='" + borrwerid + "' style='display:none'>"
                    + "                                        <input type='text' class='form-control' id='totalpaid' name='totalpaid' value='" + totalpaid + "' style='display:none'>"
                    + "                                    </div>"
                    + "                                </div>"
                    + "                                <div class='form-row'>"
                    + "                                    <div class='form-group col-md-3'>"
                    + "                                        <label for='pamount'>Pay Amount</label>"
                    + "                                        <input type='text' class='form-control' id='pamount' name='pamount' placeholder='0.00'>"
                    + "                                    </div>"
                    + "                                    <div class='form-group col-md-3'>"
                    + "                                        <label for='balance'>Balance</label>"
                    + "                                        <input type='text' class='form-control' id='balance' name='balance' value='" + balance + "' readonly='true'>"
                    + "                                    </div>"
                    + "                                    <div class='form-group col-md-3'>"
                    + "                                        <label for='loanamount'>Loan Amount</label>"
                    + "                                        <input type='text' class='form-control' id='loanamount' name='loanamount' value='" + loanamount + "' readonly='true'>"
                    + "                                    </div>"
                    + "                                    <div class='form-group col-md-3'>"
                    + "                                        <label for='interest'>Interest</label>"
                    + "                                        <input type='text' class='form-control' id='interest' name='interest' value='" + interest + "' readonly='true'>"
                    + "                                    </div>"
                    + "                                </div>"
                    + "                                <div class='form-row'>"
                    + "                                    <div class='form-group col-md-3'>"
                    + "                                        <label for='pdate'>Pay Date</label>"
                    + "                                        <input type='text' class='form-control' id='pdate' name='pdate' value='" + currDate + "' readonly='true'>"
                    + "                                    </div>"
                    + "                                    <div class='form-group col-md-6'>"
                    + "                                        <label for='collateral'>Collateral</label>"
                    + "                                        <input type='text' class='form-control' id='collateral' name='collateral'>"
                    + "                                    </div>"
                    + "                                </div>"
                    + "                                <div class='form-row'>"
                    + "                                    <div class='form-group col-md-12'>"
                    + "                                        <label for='notes'>Notes</label>"
                    + "                                        <textarea class='form-control' id='notes' name='notes'></textarea>"
                    + "                                    </div>"
                    + "                                </div>"
                    + "                                <div class='form-row'>"
                    + "                                    <div class='form-group col-md-3'>"
                    + "                                        <button type='submit' class='btn btn-success'>Add</button>"
                    + "                                    </div>"
                    + "                                </div>"
                    + "                            </form>";

            out.println(data.header);
            out.println("<div class='container'>");
            out.println("<div class='row'>");
            out.println("<div class='col-lg-9'>");
            out.println("<div class='panel panel-success'>");
            out.println("<div class='panel-heading' style='color:#fff'><span><i class='fas fa-clipboard-list'></i> #</span><span class='pull-right'>Loggedin As: "+fullname+"&nbsp;&nbsp;&nbsp;&nbsp;<a href='" + request.getContextPath() + "/Login?logout=logout' style='color:red'><i class='fas fa-outdent'></i> Sign Out</a></span></div>");
            out.println("<div class='panel-body'>");

            if (null != request.getParameter("newborrower")) {
                out.println(newborrower);
            } else if (null != request.getParameter("listborrower")) {
                out.println(listborrowers + lb() + lb2);
            } else if (null != request.getParameter("newloan")) {
                out.println(newloan);
            } else if (null != request.getParameter("payloan")) {
                out.println(payloan);
            } else if (null != request.getParameter("listloans")) {
                out.println(listloans + ll() + ll2);
            } else if (null != request.getParameter("blacklist")) {
                out.println(listblacklist + lblacklist() + lblklst2);
            } else if (null != request.getParameter("report")) {
                out.println(listreport + loanstred() + lrp2);
            } else {
                out.println(newborrower);
            }

            out.println("</div>");
            out.println("</div>");
            out.println("</div>");

            out.println("<div class='col-lg-3' >");
            out.println("<div class='panel panel-success'>");
            out.println("<div class='panel-heading  text-center' style='color:#fff'><i class='fas fa-cog'></i> Manage</div>");
            out.println("<div class='panel-body'>");
            out.println("<div class='list-group'>");
            if (null != request.getParameter("newborrower")) {
                out.println("<a href='" + request.getContextPath() + "/Home?newborrower=1' class='list-group-item list-group-item-action active'><i class='fas fa-user'></i> New Borrower</a>");
            } else {
                out.println("<a href='" + request.getContextPath() + "/Home?newborrower=1' class='list-group-item list-group-item-action'><i class='fas fa-user'></i> New Borrower</a>");
            }
            if (null != request.getParameter("listborrower")) {
                out.println("<a href='" + request.getContextPath() + "/Home?listborrower=1' class='list-group-item list-group-item-action active'><i class='fas fa-users'></i> List Borrowers</a>");
            } else {
                out.println("<a href='" + request.getContextPath() + "/Home?listborrower=1' class='list-group-item list-group-item-action'><i class='fas fa-users'></i> List Borrowers</a>");
            }

            if (null != request.getParameter("newloan")) {
                out.println("<a href='" + request.getContextPath() + "/Home?newloan=1' class='list-group-item list-group-item-action active'><i class='fas fa-external-link-alt'></i> New Loan</a>");
            } else {
                out.println("<a href='" + request.getContextPath() + "/Home?newloan=1' class='list-group-item list-group-item-action'><i class='fas fa-external-link-alt'></i> New Loan</a>");
            }
            if (null != request.getParameter("payloan")) {
                out.println("<a href='" + request.getContextPath() + "/Home?payloan=1' class='list-group-item list-group-item-action active'><i class='fas fa-external-link-alt fa-rotate-180'></i> Pay Loan</a>");
            } else {
                out.println("<a href='" + request.getContextPath() + "/Home?payloan=1' class='list-group-item list-group-item-action'><i class='fas fa-external-link-alt fa-rotate-180'></i> Pay Loan</a>");
            }
            if (null != request.getParameter("listloans")) {
                out.println("<a href='" + request.getContextPath() + "/Home?listloans=1' class='list-group-item list-group-item-action active'><i class='far fa-newspaper'></i> List Loans</a>");
            } else {
                out.println("<a href='" + request.getContextPath() + "/Home?listloans=1' class='list-group-item list-group-item-action'><i class='far fa-newspaper'></i> List Loans</a>");
            }
            if (null != request.getParameter("blacklist")) {
                out.println("<a href='" + request.getContextPath() + "/Home?blacklist=1' class='list-group-item list-group-item-action active'><i class='fas fa-th'></i> Blacklisted</a>");
            } else {
                out.println("<a href='" + request.getContextPath() + "/Home?blacklist=1' class='list-group-item list-group-item-action'><i class='fas fa-th'></i> Blacklisted</a>");
            }
            if (null != request.getParameter("report")) {
                out.println("<a href='" + request.getContextPath() + "/Home?report=1' class='list-group-item list-group-item-action active'><i class='fas fa-flag-checkered'></i> Reports</a>");
            } else {
                out.println("<a href='" + request.getContextPath() + "/Home?report=1' class='list-group-item list-group-item-action'><i class='fas fa-flag-checkered'></i> Reports</a>");
            }
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println(data.footer);

        }

    }

    public String lb() {
        String res = "";
        DataStore data = new DataStore();
        try {
            Connection con = data.connect();
            String query = "SELECT `fname`,`lname`,`city`,`idnumber`,`phone`,`address1`,`address2` FROM `borrowers` ORDER BY `id` DESC";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                res = res + "<tr>"
                        + "                         <td>" + rs.getString(1) + "</td>"
                        + "                         <td>" + rs.getString(2) + "</td>"
                        + "                         <td>" + rs.getString(3) + "</td>"
                        + "                         <td>" + rs.getString(4) + "</td>"
                        + "                         <td>" + rs.getString(5) + "</td>"
                        + "                         <td>" + rs.getString(6) + "</td>"
                        + "                         <td>" + rs.getString(7) + "</td>"
                        + "                         </tr>";
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    public String ll() {
        String res = "";
        DataStore data = new DataStore();
        try {
            Connection con = data.connect();
            String query = "SELECT `fullname`,`phone`,`amount`,`netamount`,`totalpaid`,`issuedate`,`expecteddate` FROM  `loans` ORDER BY `id` DESC";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                res = res + "<tr>"
                        + "                         <td>" + rs.getString(1) + "</td>"
                        + "                         <td>" + rs.getString(2) + "</td>"
                        + "                         <td>" + rs.getString(3) + "</td>"
                        + "                         <td>" + rs.getString(4) + "</td>"
                        + "                         <td>" + rs.getString(5) + "</td>"
                        + "                         <td>" + rs.getString(6) + "</td>"
                        + "                         <td>" + rs.getString(7) + "</td>"
                        + "                         </tr>";
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
    public String lblacklist() {
        String res = "";
        /*DataStore data = new DataStore();
        try {
            Connection con = data.connect();
            String query = "SELECT `fullname`,`phone`,`amount`,`netamount`,`totalpaid`,`issuedate`,`expecteddate` FROM  `loans` ORDER BY `id` DESC";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                res = res + "<tr>"
                        + "                         <td>" + rs.getString(1) + "</td>"
                        + "                         <td>" + rs.getString(2) + "</td>"
                        + "                         <td>" + rs.getString(3) + "</td>"
                        + "                         <td>" + rs.getString(4) + "</td>"
                        + "                         <td>" + rs.getString(5) + "</td>"
                        + "                         <td>" + rs.getString(6) + "</td>"
                        + "                         <td>" + rs.getString(7) + "</td>"
                        + "                         </tr>";
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        return res;
    }
    public String loanstred() {
        String res = "";
        DataStore data = new DataStore();
        try {
            Connection con = data.connect();
            String query = "SELECT `borrowerid`,`amount`,`balance`,`collateral`,`notes`,`paydate` FROM  `paymenttrend`";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                String borrower = data.getBorrowername(rs.getString(1));
                String bsplit[] = borrower.split(" - ");
                res = res + "<tr>"
                        + "                         <td>" + bsplit[1] + "</td>"
                        + "                         <td>" + bsplit[0] + "</td>"
                        + "                         <td>" + rs.getString(2) + "</td>"
                        + "                         <td>" + rs.getString(3) + "</td>"
                        + "                         <td>" + rs.getString(4) + "</td>"
                        + "                         <td>" + rs.getString(5) + "</td>"
                        + "                         <td>" + rs.getString(6) + "</td>"
                        + "                         </tr>";
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    public String aborrowers() {
        String res = "";
        try {
            DataStore data = new DataStore();
            Connection con = data.connect();
            String query = "SELECT `id`,`phone` FROM `borrowers` WHERE `flag`=0";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                res = res + "<option value='" + rs.getString(1) + "'>" + rs.getString(2) + "</option>";
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    public String aopenloans() {
        String res = "";
        try {
            DataStore data = new DataStore();
            Connection con = data.connect();
            String query = "SELECT `id`,`phone` FROM `loans` WHERE `flag`=0";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                res = res + "<option value='" + rs.getString(1) + "'>" + rs.getString(2) + "</option>";
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.judiciary.portal;

import java.sql.*;

/**
 *
 * @author julius
 */
public class DataStore {

    public Connection connect() {
        Connection conn = null;
        try {
            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://localhost/judicial";
            Class.forName(myDriver);
            conn = DriverManager.getConnection(myUrl, "root", "juliusroot2");
            //conn = DriverManager.getConnection(myUrl, "root", "1root2");

        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
        return conn;
    }

    String header = "<!DOCTYPE html>"
            + "<html>"
            + "    <head>"
            + "        <meta charset='utf-8'>"
            + "        <meta name='viewport' content='width=device-width, initial-scale=1.0'>"
            + "        <meta http-equiv='X-UA-Compatible' content='IE=edge'>"
            + "        <link rel='shortcut icon' href='img/logo.png' type='image/png'>"
            + "        <title>Judiciary SMS</title>"
            + "        <!-- Bootstrap CSS CDN -->"
            + "        <link rel='stylesheet' href='https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css' integrity='sha384-9gVQ4dYFwwWSjIDZnLEWnxCjeSWFphJiwGPXr1jddIhOegiu1FwO5qRGvFXOdJZ4' crossorigin='anonymous'>"
            + "        <!-- Our Custom CSS -->"
            + "        <link rel='stylesheet' href='css/judiciary.css'>"
            + "        <!-- Font Awesome JS -->"
            + "        <script defer src='https://use.fontawesome.com/releases/v5.0.13/js/solid.js' integrity='sha384-tzzSw1/Vo+0N5UhStP3bvwWPq+uvzCMfrN1fEFe+xBmv1C/AtVX5K0uZtmcHitFZ' crossorigin='anonymous'></script>"
            + "        <script defer src='https://use.fontawesome.com/releases/v5.0.13/js/fontawesome.js' integrity='sha384-6OIrr52G08NpOFSZdxxz1xdNSndlD4vdcf/q2myIUVO0VsqaGHJsB0RaBE01VTOY' crossorigin='anonymous'></script>"
            + "    </head>"
            + "    <body>"
            + "        <div class='wrapper'>"
            + "            <!-- Sidebar  -->"
            + "            <nav id='sidebar'>"
            + "                <div class='sidebar-header text-center'>"
            + "                    <!--h3>Judiciary SMS</h3-->"
            + "                    <img src='img/logo.png' class='img-thumbnail rounded'>"
            + "                </div>"
            + "                <ul class='list-unstyled components'>"
            + "                    <p>SMS Portal</p>"
            + "                    <li class='active'>"
            + "                        <a href='#homeSubmenu' data-toggle='collapse' aria-expanded='false' class='dropdown-toggle'>Bulk</a>"
            + "                        <ul class='collapse list-unstyled' id='homeSubmenu'>"
            + "                            <li>"
            + "                                <a href='?inbox'>Inbox</a>"
            + "                            </li>"
            + "                            <li>"
            + "                                <a href='?outbox'>Outbox</a>"
            + "                            </li>"
            + "                        </ul>"
            + "                    </li>"
            + "                    <!--li>"
            + "                        <a href='#ussd' data-toggle='collapse' aria-expanded='false' class='dropdown-toggle'>USSD</a>"
            + "                        <ul class='collapse list-unstyled' id='ussd'>"
            + "                            <li>"
            + "                                <a href='#'>Inbox</a>"
            + "                            </li>"
            + "                            <li>"
            + "                                <a href='#'>Outbox</a>"
            + "                            </li>"
            + "                        </ul>"
            + "                    </li-->"
            + "                </ul>"
            + "                <ul class='list-unstyled components' style='font-size:12px'>"
            + "                    <li>"
            + "                        <a href='#modal'><i class='fa fa-phone'></i>  Search By Phone</a>"
            + "                    </li>"
            + "                    <li>"
            + "                        <a href='#'><i class='fa fa-link'></i> Search By LinkID</a>"
            + "                    </li>"
            + "                    <li>"
            + "                        <a href='#'><i class='fa fa-users'></i> Search By MNO</a>"
            + "                    </li>"
            + "                    <li>"
            + "                        <a href='#'><i class='fa fa-comments'></i> Search By SessionID</a>"
            + "                    </li>"
            + "                </ul>"
            + "                <ul class='list-unstyled CTAs'>"
            + "                    <li>"
            + "                        <a href='#' class='download'>Download Excel</a>"
            + "                    </li>"
            + "                    <li>"
            + "                        <a href='#' class='article'>Download PDF</a>"
            + "                    </li>"
            + "                </ul>"
            + "            </nav>"
            + "            <!-- Page Content  -->"
            + "            <div id='content'>"
            + "                <nav class='navbar navbar-expand-lg navbar-light bg-light'>"
            + "                    <div class='container-fluid'>"
            + "                        <button type='button' id='sidebarCollapse' class='btn btn-info'>"
            + "                            <i class='fas fa-align-left'></i>"
            + "                            <span>Hide Menu</span>"
            + "                        </button>"
            + "                        <img src='img/kenya.png' width='50'>"
            + "                    </div>"
            + "                </nav>";
    String loginheader = "<!DOCTYPE html>"
            + "<html>"
            + "    <head>"
            + "        <meta charset='utf-8'>"
            + "        <meta name='viewport' content='width=device-width, initial-scale=1.0'>"
            + "        <meta http-equiv='X-UA-Compatible' content='IE=edge'>"
            + "        <link rel='shortcut icon' href='img/logo.png' type='image/png'>"
            + "        <title>Judiciary SMS</title>"
            + "        <!-- Bootstrap CSS CDN -->"
            + "        <link rel='stylesheet' href='https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css' integrity='sha384-9gVQ4dYFwwWSjIDZnLEWnxCjeSWFphJiwGPXr1jddIhOegiu1FwO5qRGvFXOdJZ4' crossorigin='anonymous'>"
            + "        <!-- Our Custom CSS -->"
            + "        <link rel='stylesheet' href='css/judiciary.css'>"
            + "        <!-- Font Awesome JS -->"
            + "        <script defer src='https://use.fontawesome.com/releases/v5.0.13/js/solid.js' integrity='sha384-tzzSw1/Vo+0N5UhStP3bvwWPq+uvzCMfrN1fEFe+xBmv1C/AtVX5K0uZtmcHitFZ' crossorigin='anonymous'></script>"
            + "        <script defer src='https://use.fontawesome.com/releases/v5.0.13/js/fontawesome.js' integrity='sha384-6OIrr52G08NpOFSZdxxz1xdNSndlD4vdcf/q2myIUVO0VsqaGHJsB0RaBE01VTOY' crossorigin='anonymous'></script>"
            + "    </head>"
            + "    <body>";

    String footer = "<!-- jQuery CDN - Slim version (=without AJAX) -->"
            + "        <script src='https://code.jquery.com/jquery-3.3.1.slim.min.js' integrity='sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo' crossorigin='anonymous'></script>"
            + "        <!-- Popper.JS -->"
            + "        <script src='https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js' integrity='sha384-cs/chFZiN24E4KMATLdqdvsezGxaGsi4hLGOzlXwp5UZB1LY//20VyM2taTB4QvJ' crossorigin='anonymous'></script>"
            + "        <!-- Bootstrap JS -->"
            + "        <script src='https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js' integrity='sha384-uefMccjFJAIv6A+rW+L4AHf99KvxDjWSu1z9VI8SKNVmz4sk7buKt/6v9KI65qnm' crossorigin='anonymous'></script>"
            + "        <script type='text/javascript'>"
            + "            $(document).ready(function () {"
            + "                $('#sidebarCollapse').on('click', function () {"
            + "                    $('#sidebar').toggleClass('active');"
            + "                });"
            + "            });"
            + "        </script>"
            + "    </body>"
            + ""
            + "</html>";

    public String checkUser(String uname, String upass) {
        String res = "none";
        if (uname.equals("judiciary") && upass.equals("judiciary111")) {
            res = "found:Pdsl Admin";
        }
        if (uname.equals("admin") && upass.equals("admin")) {
            res = "found:Administrator";
        }

        return res;
    }

}

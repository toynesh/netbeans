<!doctype html>
<html lang="en">
    <head>
        <!-- Required meta tags -->
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <link rel='shortcut icon' href='../images/logo2.png' type='image/png'>

        <script defer src='https://use.fontawesome.com/releases/v5.0.8/js/all.js'></script>
        <link href="https://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet"> 
        <link href="https://fonts.googleapis.com/css?family=Belleza" rel="stylesheet"> 
        <link href="https://fonts.googleapis.com/css?family=Dancing+Script" rel="stylesheet"> 
        <!-- fawsomeanimations -->
        <link href='../css/font-awesome-animation.min.css' rel='stylesheet'>
        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
        <!-- custom -->
        <link href='../css/qaal.css' rel='stylesheet'>
        <?php
        if (strpos($_SERVER['REQUEST_URI'], 'home') !== false) {
            echo '<title>Home | Qaal Designs</title>';
        } elseif (strpos($_SERVER['REQUEST_URI'], 'about') !== false) {
            echo '<title>About | Qaal Designs</title>';
        } elseif (strpos($_SERVER['REQUEST_URI'], 'shop') !== false) {
            echo '<title>Shop | Qaal Designs</title>';
        } elseif (strpos($_SERVER['REQUEST_URI'], 'contact') !== false) {
            echo '<title>Contact | Qaal Designs</title>';
        }
        ?>

    </head>
    <body>
        <div class="container">
            <div class="row">
                <div class="col text-center">
                    <a href="../"><img src="../images/logo2.png" class="logo"></a>
                </div>
            </div>
        </div>
        <!-- Navigation -->
        <div class="navbar navbar-static-top navbar-expand-md text-uppercase navbar-custom">
            <div class="container">
                <button type="button" class="navbar-toggler" data-toggle="collapse" data-target=".navbar-collapse">☰</button>
                <ul class="nav navbar-nav ml-auto">
                    <li class="nav-item"><a href="../" class="nav-link"><span class="navsp">Home</span></a></li>
                </ul>
                <!--div class="navbar navbar-custom">
                    <div class="container">
                        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse" style="font-size:50px;font-family: 'Dancing Script';border:none;background:none;color:#fff;outline:none !important;text-shadow: -1px 0 #484D54, 0 1px #484D54, 1px 0 #484D54, 0 1px #484D54;"><img src="../images/logo.png" class="stitched"><br>.Menu_</button-->
                <div class="navbar-collapse collapse">
                    <ul class="nav navbar-nav">                        
                        <li class="nav-item"><a href="../about/" class="nav-link"><span class="navsp">About</span></a></li>
                        <!--li class="dropdown menu-large nav-item divider-vertical"> <a href="#" class="dropdown-toggle nav-link" data-toggle="dropdown"> New Arrivals </a>
                            <ul class="dropdown-menu megamenu">
                                <li class="dropdown-item">
                                    <div class="row">
                                        <div class="col-md-6 col-lg-3 text-center">
                                            <div class="card">
                                                <a href="#" class="thumbnail">
                        <!--img src="http://placehold.it/150x120">
                        <img src="http://placehold.it/150x120">
                    </a>
                </div></div>
            <div class="col-md-6 col-lg-3 text-center">
                <div class="card">
                    <a href="#" class="thumbnail">
                        <img src="http://placehold.it/150x120">
                    </a>
                </div>
            </div>
            <div class="col-md-6 col-lg-3 text-center">
                <div class="card">
                    <a href="#" class="thumbnail">
                        <img src="http://placehold.it/150x120">
                    </a>
                </div>
            </div>
            <div class="col-md-6 col-lg-3 text-center">
                <div class="card">
                    <a href="#" class="thumbnail">
                        <img src="http://placehold.it/150x120">
                    </a>
                </div>
            </div>
        </div>
    </li>
</ul>
</li-->                        
                        <li class="dropdown menu-large nav-item divider-vertical"> <a href="#" class="dropdown-toggle nav-link" data-toggle="dropdown"><span class="navsp"><i class="fab fa-opencart faa-flash animated"></i> Shop</span></a>
                            <ul class="dropdown-menu megamenu">
                                <div class="row">
                                    <li class="col-md-3 dropdown-item" style="background:none !important;">
                                        <ul>
                                            <li class="dropdown-header">Dresses</li>
                                            <li><a href="../shop/">Dress1</a>
                                            </li>
                                            <li class="disabled"><a href="../shop/">Dress2</a>
                                            </li>
                                            <li><a href="../shop/">Dress3</a>
                                            </li>
                                            <li class="divider"></li>
                                            <li class="dropdown-header">Kaftans</li>
                                            <li><a href="../shop/">Kaftan1</a>
                                            </li>
                                            <li><a href="../shop/">Kaftan2</a>
                                            </li>
                                            <li><a href="../shop/">Kaftan3</a>
                                            </li>
                                            <li><a href="../shop/">Kaftan4</a>
                                            </li>
                                        </ul>
                                    </li>
                                    <li class="col-md-3 dropdown-item" style="background:none !important;">
                                        <ul>
                                            <li class="dropdown-header">Skirts</li>
                                            <li><a href="../shop/">Skirt1</a>
                                            </li>
                                            <li><a href="../shop/">Skirt2</a>
                                            </li>
                                            <li><a href="../shop/">Skirt3</a>
                                            </li>
                                            <li><a href="../shop/">Skirt4</a>
                                            </li>
                                            <li><a href="../shop/">Skirt5</a>
                                            </li>
                                            <li class="divider"></li>
                                            <li class="dropdown-header">Blouses</li>
                                            <li><a href="../shop/">Blouse1</a>
                                            </li>
                                        </ul>
                                    </li>
                                    <li class="col-md-3 dropdown-item" style="background:none !important;">
                                        <ul>
                                            <li class="dropdown-header">Trousers</li>
                                            <li><a href="../shop/">Trouser1</a>
                                            </li>
                                            <li><a href="../shop/">Trouser2</a>
                                            </li>
                                            <li><a href="../shop/">Trouser3</a>
                                            </li>
                                            <li class="divider"></li>
                                            <li class="dropdown-header">Jackets</li>
                                            <li><a href="../shop/">Jacket1</a>
                                            </li>
                                            <li><a href="../shop/">Jacket2</a>
                                            </li>
                                            <li><a href="../shop/">Jacket3</a>
                                            </li>
                                        </ul>
                                    </li>
                                    <li class="col-md-3 dropdown-item" style="background:none !important;">
                                        <ul>
                                            <li class="dropdown-header">Accessories</li>
                                            <li><a href="../shop/">Accessory1</a>
                                            </li>
                                            <li><a href="../shop/">Accessory2</a>
                                            </li>
                                            <li><a href="../shop/">Accessory3</a>
                                            </li>
                                            <li><a href="../shop/">Accessory4</a>
                                            </li>
                                            <li><a href="../shop/">Accessory5</a>
                                            </li>
                                            <li><a href="../shop/">Accessory6</a>
                                            </li>
                                            <li><a href="../shop/">Accessory7</a>
                                            </li>
                                            <li><a href="../shop/">Accessory8</a>
                                            </li>
                                            <li><a href="../shop/">Accessory9</a>
                                            </li>
                                        </ul>
                                    </li>
                                </div>
                            </ul>
                        </li>
                        <li class="nav-item"><a href="#" class="nav-link"><span class="navsp">Media Press</span></a>
                        <li class="nav-item"><a href="#" class="nav-link"><span class="navsp">Events</span></a>
                        <li class="nav-item"><a href="../contact/" class="nav-link"><span class="navsp">Contact</span></a>
                    </ul>
                    <ul class="nav navbar-nav ml-auto">
                        <li class="nav-item"><a onclick="document.getElementById('viewcart').submit();" class="nav-link"><i class="fas fa-shopping-cart"></i> View Cart</a></li>
                        <form target="paypal" id="viewcart" action="https://www.paypal.com/cgi-bin/webscr" method="post">

                            <!-- Identify your business so that you can collect the payments. -->
                            <input type="hidden" name="business" value="kin@kinskards.com">

                            <!-- Specify a PayPal Shopping Cart Add to Cart button. -->
                            <input type="hidden" name="cmd" value="_cart">
                            <input type="hidden" name="add" value="1">

                            <!-- Specify details about the item that buyers will purchase. -->
                            <input type="hidden" name="item_name" value="Birthday - Cake and Candle">
                            <input type="hidden" name="amount" value="3.95">
                            <input type="hidden" name="currency_code" value="USD">

                            <!-- Display the payment button. -->
                            <!--input type="image" name="submit"
                                   src="https://www.paypalobjects.com/en_US/i/btn/btn_cart_LG.gif"
                                   alt="Add to Cart">
                            <img alt="" width="1" height="1"
                                 src="https://www.paypalobjects.com/en_US/i/scr/pixel.gif"-->
                        </form>
                    </ul>
                </div>
            </div>
        </div>
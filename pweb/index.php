<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<html>
    <head>
        <meta charset="UTF-8">
        <link rel='shortcut icon' href='img/passport.jpg' type='image/png'>
        <title>Julius Kariuki</title>
        <script defer src='https://use.fontawesome.com/releases/v5.0.8/js/all.js'></script>
        <link href="https://fonts.googleapis.com/css?family=Fredericka+the+Great" rel="stylesheet"> 
        <link href="https://fonts.googleapis.com/css?family=Roboto" rel="stylesheet"> 
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
        <!-- custom -->
        <link href='css/men.css' rel='stylesheet'>
        <link href='css/p.css' rel='stylesheet'>
        <link href='css/font-awesome-animation.min.css' rel='stylesheet'>
    </head>
    <body>
        <canvas id="c"></canvas>
        <div class="container">
            <div class="row vertical-center">
                <div class="col-lg-10 offset-lg-1 ">
                    <div class="row mt-5">
                        <div class="col-lg-6 offset-lg-3 text-center">
                            <img src="img/passport.jpg" class="rounded-circle img-thumbnail" style="width:250px;height:235px" alt="Julius Kariuki">
                            <h1 class="heading">Julius Kariuki</h1>
                            <div class="divider">
                                <div class="div-round-left"></div>
                                <div class="div-round-left"></div>
                                <div class="div-long"></div>
                                <div class="div-round-right"></div>
                                <div class="div-round-right"></div>
                            </div>
                            <h3 class="sub-heading">Software Engineer | Digital Resume</h3>
                        </div>
                    </div>
                    <div class="row mt-2">
                        <div class="col-lg-10 offset-lg-1">
                            <div class="menu">
                                <div class="item">
                                    <a class="link icon_menu faa-parent animated-hover" style="color: #0077c8 !important;">                                    
                                        <span class="fa-stack fa-2x">
                                            <i class="fas fa-circle fa-stack-2x"></i>
                                            <i class="fas fa-home fa-stack-1x fa-inverse faa-burst animated-hover"></i>
                                        </span>    
                                    </a>
                                    <div class="item_content">
                                        <h2><a href="#" onclick="check();" data-toggle="modal" data-target="#profile" class=" faa-parent animated-hover">Profile <i class="fas fa-arrow-right faa-passing animated-hover"></i></a></h2>
                                    </div>
                                </div>
                                <div class="item">
                                    <a class="link icon_menu faa-parent animated-hover" style="color: #00205b !important;">
                                        <span class="fa-stack fa-2x">
                                            <i class="fas fa-circle fa-stack-2x"></i>
                                            <i class="fas fa-graduation-cap fa-stack-1x fa-inverse faa-burst animated-hover"></i>
                                        </span>                                         
                                    </a>
                                    <div class="item_content">
                                        <h2><a href="#" onclick="check();" data-toggle="modal" data-target="#education" class=" faa-parent animated-hover">Education <i class="fas fa-arrow-right faa-passing animated-hover"></i></a></h2>
                                    </div>
                                </div>
                                <div class="item">
                                    <a class="link icon_menu faa-parent animated-hover" style="color: #7d3f98 !important;">
                                        <span class="fa-stack fa-2x">
                                            <i class="fas fa-circle fa-stack-2x"></i>
                                            <i class="fas fa-laptop fa-stack-1x fa-inverse faa-burst animated-hover"></i>
                                        </span>
                                    </a>
                                    <div class="item_content">
                                        <h2><a href="#" onclick="check();" data-toggle="modal" data-target="#skill" class=" faa-parent animated-hover">Skills <i class="fas fa-arrow-right faa-passing animated-hover"></i></a></h2>
                                    </div>
                                </div>
                                <div class="item">
                                    <a class="link icon_menu link icon_menu faa-parent animated-hover" style="color: #fe5000 !important;">
                                        <span class="fa-stack fa-2x">
                                            <i class="fas fa-circle fa-stack-2x"></i>
                                            <i class="fas fa-history fa-stack-1x fa-inverse faa-burst animated-hover"></i>
                                        </span>
                                    </a>
                                    <div class="item_content">
                                        <h2><a href="#" onclick="check();" data-toggle="modal" data-target="#history" class=" faa-parent animated-hover">History <i class="fas fa-arrow-right faa-passing animated-hover"></i></a></h2>
                                    </div>
                                </div>
                                <div class="item">
                                    <a class="link icon_menu link icon_menu faa-parent animated-hover" style="color: #ff4f81 !important;">
                                        <span class="fa-stack fa-2x">
                                            <i class="fas fa-circle fa-stack-2x"></i>
                                            <i class="fas fa-phone fa-stack-1x fa-inverse faa-burst animated-hover"></i>
                                        </span>
                                    </a>
                                    <div class="item_content">
                                        <h2><a href="#" onclick="check();" data-toggle="modal" data-target="#contact" class=" faa-parent animated-hover">Contact <i class="fas fa-arrow-right faa-passing animated-hover"></i></a></h2>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    
                    
                            <!-- Modal -->
                            <div  class="modal fade" id="profile" tabindex="-1" role="dialog" aria-labelledby="profileLabel" aria-hidden="true">
                                <div class="modal-dialog modal-dialog-centered modal-xl" role="document">
                                    <div class="modal-content" style="background:none;border:none">
                                        <div class="modal-header" style="border-bottom:none">
                                            <a class="close" data-dismiss="modal" aria-label="Close" style="text-shadow: 0 0px 0 #fff;margin-right:-10%;margin-bottom:-7%">
                                                <span aria-hidden="true" style="color:#5AB9EA;">&times;</span>
                                            </a>
                                        </div>
                                        <div id="japan" class="modal-body" style="background:#fff">                                         
                                            <div class="about-card">
                                                <!--div class="about-card-icon">
                                                    <i class="far fa-id-badge sky-blue-txt txt-shadow" aria-hidden="true"></i>
                                            </div-->
                                                <h4 class="main-heading gradient-txt text-uppercase">プロフィール</h4>
                                                <h2 class="text-center txt-shadow fredericka">Let me 自己紹介する。</h2>
                                                <div id="google_translate_element"></div>
                                                <img src="img/monke.png" width="150">
                                                <div class="divider">
                                                    <div class="div-round-left straight-purple"></div>
                                                    <div class="div-round-left light-pink"></div>
                                                    <div class="div-long bg-gradient"></div>
                                                    <div class="div-round-right deep-blue"></div>
                                                    <div class="div-round-right sky-blue"></div>
                                                </div>
                                                <div class="pin top-right sky-blue"></div>
                                                <div class="pin top-left sky-blue"></div>
                                                <div class="pin bottom-right"></div>
                                                <div class="pin bottom-left"></div>
                                                <p class="text-center">I am a Talented Software Engineer with over 6 years experience in 設計、アーキテクチャ、およびデータモデリング.</p>
                                            </div>
                                        </div>
                                        <div id="usa" class="modal-body" style="background:#fff"> 
                                            <div class="about-card">
                                                <h4 class="main-heading gradient-txt text-uppercase">profile</h4>
                                                <h2 class="text-center txt-shadow fredericka">Let me introduce myself.</h2>
                                                <img src="img/monke.png" width="150">
                                                <div class="divider">
                                                    <div class="div-round-left straight-purple"></div>
                                                    <div class="div-round-left light-pink"></div>
                                                    <div class="div-long bg-gradient"></div>
                                                    <div class="div-round-right deep-blue"></div>
                                                    <div class="div-round-right sky-blue"></div>
                                                </div>
                                                <div class="pin top-right sky-blue"></div>
                                                <div class="pin top-left sky-blue"></div>
                                                <div class="pin bottom-right"></div>
                                                <div class="pin bottom-left"></div>
                                                <p class="text-center">I am a Talented Software Engineer with over 6 years experience in design, architecture, and data modeling.</p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div  class="modal fade" id="education" tabindex="-1" role="dialog" aria-labelledby="educationLabel" aria-hidden="true">
                                <div class="modal-dialog modal-dialog-centered modal-xl" role="document">
                                    <div class="modal-content" style="background:none;border:none">
                                        <div class="modal-header" style="border-bottom:none">
                                            <a class="close" data-dismiss="modal" aria-label="Close" style="text-shadow: 0 0px 0 #fff;margin-right:-10%;margin-bottom:-7%">
                                                <span aria-hidden="true" style="color:#5AB9EA;">&times;</span>
                                            </a>
                                        </div>
                                        <div id="japane" class="modal-body" style="background:#fff">
                                            <div class="about-card">
                                                <h2 class="text-center txt-shadow fredericka">私の教育。</h2>
                                                <div id="google_translate_element"></div>
                                                <div class="divider">
                                                    <div class="div-round-left straight-purple"></div>
                                                    <div class="div-round-left light-pink"></div>
                                                    <div class="div-long bg-gradient"></div>
                                                    <div class="div-round-right deep-blue"></div>
                                                    <div class="div-round-right sky-blue"></div>
                                                </div>
                                                <div class="pin top-right deep-blue"></div>
                                                <div class="pin top-left deep-blue"></div>
                                                <div class="pin bottom-right deep-blue"></div>
                                                <div class="pin bottom-left deep-blue"></div>

                                                <p class="text-center text-capitalize">Oracle Database Administration 11G、「New Horizons」によって認証されました。</p>
                                                <p class="text-center text-capitalize">B.S in 情報技術、JKUAT、ナイロビ、2011.</p>
                                                <p class="text-center">K.C.S.E、Baricho High School、Kerugoya、2005</p>
                                                <p class="text-center">K.C.P.E, Rev. Musa Mumai, Kutus, 2001.</p>
                                                <p class="text-center text-capitalize">Microsoft Officeパッケージ、 "Hosanna Institute of Professional Studies"の認定を受けています。</p>
                                                <p class="text-center text-capitalize">Linguistic Course (French), Certified by “Hosanna Institute of Professional Studies”.</p>
                                            </div>                                            
                                        </div>
                                        <div id="usae" class="modal-body" style="background:#fff">
                                            <div class="about-card">
                                                <h2 class="text-center txt-shadow fredericka">My education.</h2>
                                                <div class="divider">
                                                    <div class="div-round-left straight-purple"></div>
                                                    <div class="div-round-left light-pink"></div>
                                                    <div class="div-long bg-gradient"></div>
                                                    <div class="div-round-right deep-blue"></div>
                                                    <div class="div-round-right sky-blue"></div>
                                                </div>
                                                <div class="pin top-right deep-blue"></div>
                                                <div class="pin top-left deep-blue"></div>
                                                <div class="pin bottom-right deep-blue"></div>
                                                <div class="pin bottom-left deep-blue"></div>

                                                <p class="text-center">Oracle Database Administration 11G, Certified by “New Horizons”.</p>
                                                <p class="text-center">B.S in Information Technology, JKUAT, Nairobi, 2011.</p>
                                                <p class="text-center">K.C.S.E, Baricho High School, Kerugoya, 2005.</p>
                                                <p class="text-center">K.C.P.E, Rev. Musa Mumai, Kutus, 2001.</p>
                                                <p class="text-center">Microsoft office packages, Certified by “Hosanna Institute of Professional Studies”.</p>
                                                <p class="text-center">Linguistic Course (French), Certified by “Hosanna Institute of Professional Studies”.</p>
                                            </div>                                            
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div  class="modal fade" id="skill" tabindex="-1" role="dialog" aria-labelledby="skillLabel" aria-hidden="true">
                                <div class="modal-dialog modal-dialog-centered modal-xl  modal-lg" role="document">
                                    <div class="modal-content" style="background:none;border:none">
                                        <div class="modal-header" style="border-bottom:none">
                                            <a class="close" data-dismiss="modal" aria-label="Close" style="text-shadow: 0 0px 0 #fff;margin-right:-7%;margin-bottom:-7%">
                                                <span aria-hidden="true" style="color:#5AB9EA;">&times;</span>
                                            </a>
                                        </div>
                                        <div id="japansk" class="modal-body" style="background:#fff">
                                            <div class="about-card" style="font-size:12px">
                                                <h2 class="text-center txt-shadow text-capitalize fredericka">これらは私です competencies.</h2>
                                                <div class="divider">
                                                    <div class="div-round-left straight-purple"></div>
                                                    <div class="div-round-left light-pink"></div>
                                                    <div class="div-long bg-gradient"></div>
                                                    <div class="div-round-right deep-blue"></div>
                                                    <div class="div-round-right sky-blue"></div>
                                                </div>
                                                <table class="table table-bordered straight-purple text-capitalize">
                                                    <thead>
                                                        <tr>
                                                            <th scope="col">タイトル</th>
                                                            <th width="70%" scope="col">説明</th>
                                                            <th scope="col">評価</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <tr>
                                                            <th scope="row">言語:</th>
                                                            <td>Java, JavaScript, Java Beans, JSP, SQL, C#, C++, HTML, XML, PHP, VB.NET</td>
                                                            <td>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th scope="row">テクノロジー:</th>
                                                            <td>JDBC, Servlets, JSP, Web Services, SOAP, WSDL, Socket Connections.</td>
                                                            <td>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th scope="row">フレームワーク:</th>
                                                            <td>Playsms, WordPress, Joomla, Twitter Bootstrap, JQuery, Laravel, Codeigniter and Humhub.</td>
                                                            <td>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star" aria-hidden="true"></i>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th scope="row">ツール:</th>
                                                            <td>Tomcat, Glashfish, Wildfly, SQLplus, SQL Developer, Phpmyadmin, Adobe Dreamweaver/ Photoshop/Illustrator, NetBeans, Eclipse, Android Studio and Visual Studio.</td>
                                                            <td>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star" aria-hidden="true"></i>
                                                                <i class="fas fa-star" aria-hidden="true"></i>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th scope="row">データベース:</th>
                                                            <td>Mysql, Oracle 9i/ 10g/11g/12c, PostgreSQL, FileMaker pro/Server/Go</td>
                                                            <td>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star" aria-hidden="true"></i>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th scope="row">OS:</th>
                                                            <td>Sun Solaris, Red Hat, Ubuntu, Debian, Kali Linux, CentOS, Arch Linux</td>
                                                            <td>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star" aria-hidden="true"></i>
                                                            </td>
                                                        </tr>
                                                    </tbody>
                                                </table>
                                                <div class="pin top-right straight-purple"></div>
                                                <div class="pin top-left straight-purple"></div>
                                                <div class="pin bottom-right straight-purple "></div>
                                                <div class="pin bottom-left straight-purple"></div>
                                            </div>
                                        </div>
                                        <div id="usask" class="modal-body" style="background:#fff">
                                            <div class="about-card" style="font-size:12px">
                                                <h2 class="text-center txt-shadow fredericka">These are my competencies.</h2>
                                                <div class="divider">
                                                    <div class="div-round-left straight-purple"></div>
                                                    <div class="div-round-left light-pink"></div>
                                                    <div class="div-long bg-gradient"></div>
                                                    <div class="div-round-right  deep-blue"></div>
                                                    <div class="div-round-right sky-blue"></div>
                                                </div>
                                                <table class="table table-bordered straight-purple">
                                                    <thead>
                                                        <tr>
                                                            <th scope="col">Title</th>
                                                            <th width="70%" scope="col">Description</th>
                                                            <th scope="col">Rating</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <tr>
                                                            <th scope="row">Languages:</th>
                                                            <td>Java, JavaScript, Java Beans, JSP, SQL, C#, C++, HTML, XML, PHP, VB.NET</td>
                                                            <td>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th scope="row">Technologies:</th>
                                                            <td>JDBC, Servlets, JSP, Web Services, SOAP, WSDL, Socket Connections.</td>
                                                            <td>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th scope="row">Frameworks:</th>
                                                            <td>Playsms, WordPress, Joomla, Twitter Bootstrap, JQuery, Laravel, Codeigniter and Humhub.</td>
                                                            <td>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star" aria-hidden="true"></i>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th scope="row">Tools:</th>
                                                            <td>Tomcat, Glashfish, Wildfly, SQLplus, SQL Developer, Phpmyadmin, Adobe Dreamweaver/ Photoshop/Illustrator, NetBeans, Eclipse, Android Studio and Visual Studio.</td>
                                                            <td>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star" aria-hidden="true"></i>
                                                                <i class="fas fa-star" aria-hidden="true"></i>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th scope="row">Databases:</th>
                                                            <td>Mysql, Oracle 9i/ 10g/11g/12c, PostgreSQL, FileMaker pro/Server/Go</td>
                                                            <td>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star" aria-hidden="true"></i>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th scope="row">OS:</th>
                                                            <td>Sun Solaris, Red Hat, Ubuntu, Debian, Kali Linux, CentOS, Arch Linux</td>
                                                            <td>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star sky-blue-txt" aria-hidden="true"></i>
                                                                <i class="fas fa-star" aria-hidden="true"></i>
                                                            </td>
                                                        </tr>
                                                    </tbody>
                                                </table>
                                                <div class="pin top-right straight-purple"></div>
                                                <div class="pin top-left straight-purple"></div>
                                                <div class="pin bottom-right straight-purple "></div>
                                                <div class="pin bottom-left straight-purple"></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div  class="modal fade" id="history" tabindex="-1" role="dialog" aria-labelledby="historyLabel" aria-hidden="true">
                                <div class="modal-dialog modal-dialog-centered modal-xl modal-lg" role="document">
                                    <div class="modal-content" style="background:none;border:none">
                                        <div class="modal-header" style="border-bottom:none">
                                            <a class="close" data-dismiss="modal" aria-label="Close" style="text-shadow: 0 0px 0 #fff;margin-right:-8%;margin-bottom:-7%">
                                                <span aria-hidden="true" style="color:#5AB9EA;">&times;</span>
                                            </a>
                                        </div>
                                        <div id="japanh" class="modal-body" style="background:#fff">
                                            <div class="about-card">
                                                <h2 class="text-center txt-shadow fredericka">ここに私の仕事の歴史があります。</h2>
                                                <div class="divider">
                                                    <div class="div-round-left straight-purple"></div>
                                                    <div class="div-round-left light-pink"></div>
                                                    <div class="div-long bg-gradient"></div>
                                                    <div class="div-round-right  deep-blue"></div>
                                                    <div class="div-round-right sky-blue"></div>
                                                </div>
                                                <h5 class="text-center light-pink text-capitalize">ソフトウェアエンジニア</h5>
                                                <h6 class="text-center straight-purple-txt">Professional Digital Systems Ltd</h6>
                                                <p class="text-center font-italic">2015 to date</p>
                                                <p class="text-center">Designed and 現在サポートしています VendIT mobile money 支払いゲートウェイプラットフォーム, Aggregation 支払ゲートウェイ, Bulk SMS プラットフォーム projects for 我々の clients like Kenya Charity Sweepstakes, Teachers Service Commission and Kenya Power and Lighting Company.</p>

                                                <h5 class="text-center straight-purple">データベース Developer</h5>
                                                <h6 class="text-center sky-blue-txt">Malindi Management Strategy Ltd</h6>
                                                <p class="text-center font-italic">2012 – 2015</p>
                                                <p class="text-center">既存のシステムを分析して、FileMakerプラットフォームを使用して変化するユーザーのニーズに対応するための変更を行うことによってデータベース管理を実現します。</p>
                                                <p class="text-center">Writing and maintaining ドキュメンテーション to describe プログラム development, logic, coding, testing, changes, and corrections.</p>
                                                <p class="text-center">Technical リーダー in Designing Graphics and Websites.</p>

                                                <h5 class="text-center deep-blue">Web プログラマー</h5>
                                                <h6 class="text-center whole-orange-txt">Biztech Solutions</h6>
                                                <p class="text-center font-italic">2011 - 2012</p>
                                                <p class="text-center">Participated in the 設計 and development of the company’s website: biasharatech.co.ke and also  in コンピューター hardware ネットワーキング, maintenance and repairs</p>

                                                <h5 class="text-center whole-orange text-capitalize">フリーランス Positions</h5>
                                                <p class="text-center">August 2016: Created the e-commerce ウェブサイト: <a href="http://sushilas.co.ke/" target="_blank">http://sushilas.co.ke/</a></p>
                                                <div class="pin top-right whole-orange"></div>
                                                <div class="pin top-left whole-orange"></div>
                                                <div class="pin bottom-right whole-orange"></div>
                                                <div class="pin bottom-left whole-orange"></div>
                                            </div>
                                        </div>
                                        <div id="usah" class="modal-body" style="background:#fff">
                                            <div class="about-card">
                                                <h2 class="text-center txt-shadow fredericka">Here's my work history.</h2>
                                                <div class="divider">
                                                    <div class="div-round-left straight-purple"></div>
                                                    <div class="div-round-left light-pink"></div>
                                                    <div class="div-long bg-gradient"></div>
                                                    <div class="div-round-right  deep-blue"></div>
                                                    <div class="div-round-right sky-blue"></div>
                                                </div>
                                                <h5 class="text-center light-pink">Software Engineer</h5>
                                                <h6 class="text-center straight-purple-txt">Professional Digital Systems Ltd</h6>
                                                <p class="text-center font-italic">2015 to date</p>
                                                <p class="text-center">Designed and currently support VendIT mobile money payment gateway platform, aggregation payment gateway, Bulk SMS platforms projects for our clients like Kenya Charity Sweepstakes, Teachers Service Commission and Kenya Power and Lighting Company.</p>

                                                <h5 class="text-center straight-purple">Database Developer</h5>
                                                <h6 class="text-center sky-blue-txt">Malindi Management Strategy Ltd</h6>
                                                <p class="text-center font-italic">2012 – 2015</p>
                                                <p class="text-center">Database Administration by analyzing the existing systems for modifications to meet changing user needs using FileMaker Platforms.</p>
                                                <p class="text-center">Writing and maintaining documentation to describe program development, logic, coding, testing, changes, and corrections.</p>
                                                <p class="text-center">Technical leader in Designing Graphics and Websites.</p>

                                                <h5 class="text-center deep-blue">Web Programmer</h5>
                                                <h6 class="text-center whole-orange-txt">Biztech Solutions</h6>
                                                <p class="text-center font-italic">2011 - 2012</p>
                                                <p class="text-center">Participated in the design and development of the company’s website: biasharatech.co.ke and also  in computer hardware networking, maintenance and repairs</p>

                                                <h5 class="text-center whole-orange">Freelance Positions</h5>
                                                <p class="text-center">August 2016: Created the e-commerce website: <a href="http://sushilas.co.ke/" target="_blank">http://sushilas.co.ke/</a></p>
                                                <div class="pin top-right whole-orange"></div>
                                                <div class="pin top-left whole-orange"></div>
                                                <div class="pin bottom-right whole-orange"></div>
                                                <div class="pin bottom-left whole-orange"></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div  class="modal fade" id="contact" tabindex="-1" role="dialog" aria-labelledby="contactLabel" aria-hidden="true">
                                <div class="modal-dialog modal-dialog-centered modal-xl" role="document">
                                    <div class="modal-content" style="background:none;border:none">
                                        <div class="modal-header" style="border-bottom:none">
                                            <a class="close" data-dismiss="modal" aria-label="Close" style="text-shadow: 0 0px 0 #fff;margin-right:-10%;margin-bottom:-7%">
                                                <span aria-hidden="true" style="color:#5AB9EA;">&times;</span>
                                            </a>
                                        </div>
                                        <div id="japanc" class="modal-body" style="background:#fff">
                                            <div class="about-card">
                                                <h2 class="text-center txt-shadow fredericka">Let's トーク.</h2>
                                                <div class="divider">
                                                    <div class="div-round-left straight-purple"></div>
                                                    <div class="div-round-left light-pink"></div>
                                                    <div class="div-long bg-gradient"></div>
                                                    <div class="div-round-right  deep-blue"></div>
                                                    <div class="div-round-right sky-blue"></div>
                                                </div>
                                                <div class="embed-responsive embed-responsive-16by9">
                                                    <video autoplay loop muted poster="img/monke.png" style="margin-top:-1px;">
                                                        <source src="img/pking.mp4" type="video/mp4">
                                                    </video>
                                                </div>
                                                <p class="mt-2"><i class="fas fa-mobile-alt sky-blue-txt txt-shadow"></i> +254 • (728) • 064 • 120</p>
                                                <p><i class="far fa-envelope straight-purple-txt txt-shadow"></i> kariukijulius63@gmail.com</p>
                                                <p><i class="far fa-envelope straight-purple-txt txt-shadow"></i> info@juliuskariuki.com</p>
                                                <div class="social-media-icons-container">
                                                    <div class="social-media-icons">
                                                        <a href="#" title="behance" target="_blank" rel="noopener">
                                                            <i class="fab fa-behance"></i>
                                                        </a>
                                                    </div>
                                                    <div class="social-media-icons">
                                                        <a href="#" title="medium" target="_blank" rel="noopener">
                                                            <i id="medium" class="fab fa-medium-m"></i>
                                                        </a>
                                                    </div>
                                                    <div class="social-media-icons">
                                                        <a href="https://www.facebook.com/Trans4mdJulius" title="facebook" target="_blank" rel="noopener">
                                                            <i class="fab fa-facebook"></i>
                                                        </a>
                                                    </div>
                                                    <div class="social-media-icons">
                                                        <a href="https://plus.google.com/u/0/101268842798281272926" title="google plus" target="_blank" rel="noopener">
                                                            <i class="fab fa-google-plus-g"></i>
                                                        </a>
                                                    </div>
                                                    <div class="social-media-icons">
                                                        <a href="https://twitter.com/JULIUSKARIUKI1" title="twitter" target="_blank" rel="noopener">
                                                            <i class="fab fa-twitter"></i>
                                                        </a>
                                                    </div>
                                                    <div class="social-media-icons">
                                                        <a href="https://www.instagram.com/wwwkariuki/?hl=en" title="instagram" target="_blank" rel="noopener">
                                                            <i class="fab fa-instagram"></i>
                                                        </a>
                                                    </div>
                                                </div>
                                                <div class="pin top-right light-pink"></div>
                                                <div class="pin top-left light-pink"></div>
                                                <div class="pin bottom-right light-pink"></div>
                                                <div class="pin bottom-left light-pink"></div>
                                            </div>
                                        </div>
                                        <div id="usac" class="modal-body" style="background:#fff">
                                            <div class="about-card">
                                                <h2 class="text-center txt-shadow fredericka">Let's talk.</h2>
                                                <div class="divider">
                                                    <div class="div-round-left straight-purple"></div>
                                                    <div class="div-round-left light-pink"></div>
                                                    <div class="div-long bg-gradient"></div>
                                                    <div class="div-round-right  deep-blue"></div>
                                                    <div class="div-round-right sky-blue"></div>
                                                </div>
                                                <div class="embed-responsive embed-responsive-16by9">
                                                    <video autoplay loop muted poster="img/monke.png" style="margin-top:-1px;">
                                                        <source src="img/pking.mp4" type="video/mp4">
                                                    </video>
                                                </div>
                                                <p class="mt-2"><i class="fas fa-mobile-alt sky-blue-txt txt-shadow"></i> +254 • (728) • 064 • 120</p>
                                                <p><i class="far fa-envelope straight-purple-txt txt-shadow"></i> kariukijulius63@gmail.com</p>
                                                <p><i class="far fa-envelope straight-purple-txt txt-shadow"></i> info@juliuskariuki.com</p>
                                                <div class="social-media-icons-container">
                                                    <div class="social-media-icons">
                                                        <a href="#" title="behance" target="_blank" rel="noopener">
                                                            <i class="fab fa-behance"></i>
                                                        </a>
                                                    </div>
                                                    <div class="social-media-icons">
                                                        <a href="#" title="medium" target="_blank" rel="noopener">
                                                            <i id="medium" class="fab fa-medium-m"></i>
                                                        </a>
                                                    </div>
                                                    <div class="social-media-icons">
                                                        <a href="https://www.facebook.com/Trans4mdJulius" title="facebook" target="_blank" rel="noopener">
                                                            <i class="fab fa-facebook"></i>
                                                        </a>
                                                    </div>
                                                    <div class="social-media-icons">
                                                        <a href="https://plus.google.com/u/0/101268842798281272926" title="google plus" target="_blank" rel="noopener">
                                                            <i class="fab fa-google-plus-g"></i>
                                                        </a>
                                                    </div>
                                                    <div class="social-media-icons">
                                                        <a href="https://twitter.com/JULIUSKARIUKI1" title="twitter" target="_blank" rel="noopener">
                                                            <i class="fab fa-twitter"></i>
                                                        </a>
                                                    </div>
                                                    <div class="social-media-icons">
                                                        <a href="https://www.instagram.com/wwwkariuki/?hl=en" title="instagram" target="_blank" rel="noopener">
                                                            <i class="fab fa-instagram"></i>
                                                        </a>
                                                    </div>
                                                </div>
                                                <div class="pin top-right light-pink"></div>
                                                <div class="pin top-left light-pink"></div>
                                                <div class="pin bottom-right light-pink"></div>
                                                <div class="pin bottom-left light-pink"></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                </div>
            </div>
        </div>
        <!-- Optional JavaScript -->
        <!-- jQuery first, then Popper.js, then Bootstrap JS -->
        <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
        <script src='js/p.js'></script>
        <script src="js/jquery.js"></script>
        <script src="js/jquery-css-transform.js" type="text/javascript"></script>
        <script src="js/jquery-animate-css-rotate-scale.js" type="text/javascript"></script>
        <script>
                                            $('.item').hover(
                                                    function () {
                                                        var $this = $(this);
                                                        expand($this);
                                                    },
                                                    function () {
                                                        var $this = $(this);
                                                        collapse($this);
                                                    }
                                            );
                                            function expand($elem) {
                                                var angle = 0;
                                                var t = setInterval(function () {
                                                    if (angle == 1440) {
                                                        clearInterval(t);
                                                        return;
                                                    }
                                                    angle += 40;
                                                    $('.link', $elem).stop().animate({rotate: '+=-40deg'}, 0);
                                                }, 10);
                                                $elem.stop().animate({width: '278px'}, 1000)
                                                        .find('.item_content').fadeIn(400, function () {
                                                    $(this).find('p').stop(true, true).fadeIn(600);
                                                });
                                            }
                                            function collapse($elem) {
                                                var angle = 1440;
                                                var t = setInterval(function () {
                                                    if (angle == 0) {
                                                        clearInterval(t);
                                                        return;
                                                    }
                                                    angle -= 40;
                                                    $('.link', $elem).stop().animate({rotate: '+=40deg'}, 0);
                                                }, 10);
                                                $elem.stop().animate({width: '70px'}, 1000)
                                                        .find('.item_content').stop(true, true).fadeOut().find('p').stop(true, true).fadeOut();
                                            }
        </script>
        <script type="text/javascript">
            function googleTranslateElementInit() {
                new google.translate.TranslateElement({pageLanguage: 'ja', layout: google.translate.TranslateElement.InlineLayout.SIMPLE}, 'google_translate_element');
            }
        </script>
        <!--script type="text/javascript" src="https://translate.google.com/translate_a/element.js?cb=googleTranslateElementInit"></script-->
        <script type="text/javascript">
            function check() {
                if (document.getElementById(':0.targetLanguage'))
                {
                    //alert('yes');
                    document.getElementById("usa").style.display = "none";
                    document.getElementById("usae").style.display = "none";
                    document.getElementById("usask").style.display = "none";
                    document.getElementById("usah").style.display = "none";
                    document.getElementById("usac").style.display = "none";
                } else {
                    //alert('no');                    
                    document.getElementById("japan").style.display = "none";
                    document.getElementById("japane").style.display = "none";
                    document.getElementById("japansk").style.display = "none";
                    document.getElementById("japanh").style.display = "none";
                    document.getElementById("japanc").style.display = "none";
                }
            }
        </script>
    </body>
</html>

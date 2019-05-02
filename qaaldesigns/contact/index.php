<?php include("../bin/header.php") ?>
<section class="hero">
    <div class="container">
        <!-- Breadcrumbs -->
        <ol class="breadcrumb justify-content-center">
            <li class="breadcrumb-item"><a href="../home/">Home</a></li>
            <li class="breadcrumb-item active">Contact</li>
        </ol>
        <!-- Hero Content-->
        <div class="hero-content pb-5 text-center">
            <h1 class="hero-heading" style="font-family: 'HKGrotesk', Fallback, sans-serif;">Contact</h1>
            <div class="row" style="font-family: 'HKGroteskLight', Fallback, sans-serif;">   
                <div class="col-xl-8 offset-xl-2"><p class="lead text-muted">They say you are what you eat but truly you are what you <strong>wear</strong> and fashion has provided us with a little something to define everyone. Are you curious about something?. QAAL designs uses a rich mix of Cultural patterns and vibrant colors in its collections. Talk to us, let's know how you feel.</p></div>
            </div>
        </div>
    </div>
</section>
<section style="background: #fafafa;">   
    <div class="container" style="font-family: 'HKGroteskLight', Fallback, sans-serif;">       
        <div class="row">
            <div class="col-md-4 text-center text-md-left contantmargint">
                <h1 class="text-danger"><i class="fas fa-map-marker-alt"></i></h1>
                <h4 class="ff-base" style="font-family: 'HKGrotesk', Fallback, sans-serif;">Address</h4>
                <p class="text-muted">13/25 New Avenue<br>Adams Arcade, 45Y 73J<br>Nairobi, <strong>Kenya</strong></p>
            </div>
            <div class="col-md-4 text-center text-md-left contantmargint">
                <h1 class="text-danger"><i class="fas fa-phone-square"></i></h1>
                <h4 class="ff-base" style="font-family: 'HKGrotesk', Fallback, sans-serif;">Call Center</h4>
                <p class="text-muted">Call us today. Our agents are taking a cup of coffee waiting for you.</p>
                <p class="text-muted"><strong>+33 555 444 333</strong></p>
            </div>
            <div class="col-md-4 text-center text-md-left contantmargint">
                <h1 class="text-danger"><i class="fas fa-envelope-square"></i></h1>
                <h4 class="ff-base" style="font-family: 'HKGrotesk', Fallback, sans-serif;">Electronic Support</h4>
                <p class="text-muted">Please feel free to write an email to us or to use our electronic ticketing system.</p>
                <ul class="list-unstyled text-muted">
                    <li>info@qaaldesigns.com</li>
                    <li>qaaldesigns@gmail.com</li>
                </ul>
            </div>
        </div>
    </div>
</section>
<section class="py-6">
    <div class="container">
        <div class="row"  style="font-family: 'HKGroteskLight', Fallback, sans-serif;">
            <div class="col-md-7 mb-5 mb-md-0" style="margin-top:2%">
                <header class="mb-5">
                    <h2 class="text-uppercase h5" style="font-family: 'HKGrotesk', Fallback, sans-serif;">Leave a Comment</h2>
                </header>
                <form id="contact-form" method="post" action="contact.php" class="custom-form form">
                    <div class="controls">
                        <div class="row">
                            <div class="col-sm-6">
                                <div class="form-group">
                                    <label for="name" class="form-label">Your firstname *</label>
                                    <input type="text" name="name" id="name" placeholder="Enter your firstname" required="required" class="form-control">
                                </div>
                            </div>
                            <div class="col-sm-6">
                                <div class="form-group">
                                    <label for="surname" class="form-label">Your lastname *</label>
                                    <input type="text" name="surname" id="surname" placeholder="Enter your  lastname" required="required" class="form-control">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="email" class="form-label">Your email *</label>
                            <input type="email" name="email" id="email" placeholder="Enter your  email" required="required" class="form-control">
                        </div>
                        <div class="form-group">
                            <label for="message" class="form-label">Your message for us *</label>
                            <textarea rows="4" name="message" id="message" placeholder="Enter your message" required="required" class="form-control"></textarea>
                        </div>
                        <button type="submit" class="btn btn-outline-dark">Send message</button>
                    </div>
                </form>
            </div>
            <div class="col-md-5">
                <img src="../images/contact.jpg" width="350" class="img-thumbnail mx-auto d-block" style="border-width:10px;border-radius:0px;border-color:#212529;">
            </div>
        </div>
    </div>
</section>

<?php include("../bin/footer.php") ?>


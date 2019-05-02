<?php require_once("../bin/functions.php"); ?>
<?php include("../bin/header.php") ?>
<div class="embed-responsive embed-responsive-16by9">
    <video autoplay loop muted poster="slide2.jpg" style="margin-top:-1px;">
        <source src="qaal.mp4" type="video/mp4">
    </video>
</div>
<!-- Modal -->
<div class="modal fade" id="thankyou" tabindex="-1" role="dialog" aria-labelledby="Thank You" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content"  style="background:rgba(72,77,84,0.1);">
            <div class="modal-header" style="border-bottom:none">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true" style="color:#fff;">&times;</span>
                </button>
            </div>
            <div class="modal-body"><div class="stitched text-center">
                    <h1>Welcome to Qaal Designs</h1>
                    <span style="font-family: 'Dancing Script';">Thank you for Stopping By</span>
                </div>
            </div>
        </div>
    </div>
</div>
<?php include("../bin/footer.php") ?>

<script>
    $(document).ready(function () {
        $('#thankyou').modal('show');
    });
</script>


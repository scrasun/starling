jQuery(document).ready(() => {
	
// consts
	var token = $('#_csrf').attr('content');
var header = $('#_csrf_header').attr('content');

var $modal = jQuery("#modal");
var $img = jQuery("#image-upload");
var $row = jQuery(".container .row");
var $body = jQuery("body");
var imageHtml = '<div class="col-sm-6 col-md-4 col-lg-3 text-center"><img class="thumb" /></div>';
	
	// open big image on click
	jQuery(".container").on("click", ".thumb", function ()
	{
	      jQuery("#image-upload").attr("src", jQuery(this).attr("data-src"));
		  jQuery("#modal").modal("show");
		
	});
	
	// if-editable options:
if (!$body.attr("data-editable"))
{
	return;
}
// Open modal on file upload
	jQuery("#file").on("change", (e) => {
	    var files = e.target.files;
	    if (files && files.length > 0) {
	      var file = files[0];
	      var url = URL.createObjectURL(file);
	      $img.attr("src", url);
		  jQuery("#delete").addClass("d-none");
		  $modal.modal("show");
        }
	    });
	
	// Start cropper in modal
	var cropper;
	$modal.on('shown.bs.modal', function () {
	      cropper = new Cropper($img[0],{
          autoCropArea: 0.8
		});
		});
$modal.on('hidden.bs.modal', function () {
        cropper.destroy();
		  jQuery("#delete").removeClass("d-none");
});
// Save on save button
jQuery("#save").on("click", function ()
{
		cropper.getCroppedCanvas().toBlob((blob) => {
			var fd = new FormData();
			fd.append("file", blob);
			jQuery.ajax({
              url: '/upload/' + jQuery("body").attr("data-gallery"),
              type: 'post',
              contentType: false,
              processData: false,
			beforeSend: (xhr) => {
			        xhr.setRequestHeader(header, token);
			    },
              data: fd,
              success: function(response){
				var $newImg = jQuery(imageHtml);
				$newImg.children("img").attr("src", response.thumbnailUrl).attr("data-src", response.imageUrl);
				$newImg.appendTo($row);
              },
           });
		});
        $modal.modal('hide');
      });

// Delete on delete button
jQuery("#delete").on("click", function ()
{
			jQuery.ajax({
              url: $img.attr("src"),
              type: 'delete',
			beforeSend: (xhr) => {
			        xhr.setRequestHeader(header, token);
			    },
              success: function(response){
				var $oldImg = jQuery("[data-src='" + $img.attr("src") + "']");
				$oldImg.parent().remove();
              },
           });
        $modal.modal('hide');
      });
});


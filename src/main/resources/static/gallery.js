jQuery(document).ready(() => {
	var token = $('#_csrf').attr('content');
var header = $('#_csrf_header').attr('content');

var $modal = jQuery("#modal");
var $img = jQuery("#image-upload")
	
	
	jQuery("#file").on("change", (e) => {
	    var files = e.target.files;
	    if (files && files.length > 0) {
	      var file = files[0];
	      var url = URL.createObjectURL(file);
	      $img.attr("src", url);
		  $modal.modal("show");
	      var cropper = new Cropper(jQuery("#image-upload")[0],{
          autoCropArea: 0.8
        });
$modal.on('hidden.bs.modal', function () {
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
				console.log(response);
              },
           });
		});
        cropper.destroy();
$modal.off('hidden.bs.modal');
      });
	    }
	});
	
	jQuery(".container").on("click", ".thumb", function ()
	{
	      jQuery("#image-upload").attr("src", jQuery(this).attr("data-src"));
		  jQuery("#modal").modal("show");
		
	});
});


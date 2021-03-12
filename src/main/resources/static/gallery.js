jQuery(document).ready(() => {
	var cropBoxData;
      var canvasData;
      var cropper;
	jQuery("#file").on("change", (e) => {
	    var files = e.target.files;
	    if (files && files.length > 0) {
	      var file = files[0];
	      var url = URL.createObjectURL(file);
	      jQuery("#image-upload").attr("src", url);
		  jQuery("#modal").modal("show");
	      cropper = new Cropper(jQuery("#image-upload")[0],{
          autoCropArea: 0.5,
          ready: function () {
            //Should set crop box data first here
            cropper.setCropBoxData(cropBoxData).setCanvasData(canvasData);
          }
        });
	    }
	});
	jQuery('#modal').on('hidden.bs.modal', function () {
        cropBoxData = cropper.getCropBoxData();
        canvasData = cropper.getCanvasData();
        cropper.destroy();
      });
});


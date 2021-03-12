jQuery(documernt).ready(() => {
	jQuery("#file").on("change", (e) => {
	    var files = e.target.files;
	    if (files && files.length > 0) {
	      var file = files[0];
	      var url = URL.createObjectURL(file);
	      jQuery("#upload").attr("src", url);
	      new Cropper(jQuery("#upload")[0]);
	    }
	});
});
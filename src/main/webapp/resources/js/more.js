$("#ShowAll").click(function(){
	$('[name="showall"]').val('true');
	$('form').submit();
});
$('form').submit(function( event ) {
	  if($('#Department').val() == "All Departments"){
		  if($('#Name').val() == ""){
			  $('#ErrorMessage').html("Please provide a name or department.");
			  event.preventDefault();
		  }else if($('#Name').val().trim().length < 2){
			  $('#ErrorMessage').html("Name must be at least 2 characters.");
			  event.preventDefault();
		  }
	  }
});
$(document).ready(function(){
	$('.toggle').click(function(){
    	$(this).closest('div').next().toggleClass('hidden');
    });
	
	$('.person img').each(function() {
		 this.src = $(this).data('src');
		 $(this).removeData('src');
	});	
});
function imgError(image) {
	$(image).after($('<span class="icon-user"></span>'));
	$(image).remove();
    return true;
}
$(document).ready(function() {
	$("form#push_form").submit(function() {
		var majorId = $(this).find('input[name=majorId]').val();
		var minorId = $(this).find('input[name=minorId]').val();
		var accessToken = $(this).find('input[name=accessToken]').val();
		$.get('/content?majorId=' + majorId + '&minorId=' + minorId + '&accessToken=' + accessToken, function(data) {
			console.log(data);
		})
		return false;
	});
});
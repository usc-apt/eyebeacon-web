$(document).ready(function() {
	$("form#push_form").submit(function() {
		$("#push_results").val("");
		var majorId = $(this).find('input[name=majorId]').val();
		var minorId = $(this).find('input[name=minorId]').val();
		var accessToken = $(this).find('input[name=accessToken]').val();
		var email = $(this).find('input[name=email]').val();
		$.get('/content?majorId=' + majorId 
				+ '&minorId=' + minorId 
				+ '&email=' + email 
				+ '&accessToken=' + accessToken, function(data) {
			console.log(data);
			var prettifiedJson = JSON.stringify(data, null, 2);
			$("#push_results").val(prettifiedJson);
		}).fail(function() {
			console.log(arguments);
			$("#push_results").val("error " + arguments[0].status + ": " + arguments[2]);
		})
		return false;
	});
	
	$("form#store_form").submit(function() {
		var majorId = $(this).find('input[name=majorId]').val();
		var minorId = $(this).find('input[name=minorId]').val();
		var template = $(this).find('input[name=template]').val();
		var templateData = $(this).find('textarea[name=templateData]').val();
		$.post('/content', {
			majorId: majorId,
			minorId: minorId,
			template: template,
			templateData: templateData
		}, function(data) {
			console.log(data);
			var prettifiedJson = JSON.stringify(data, null, 2);
			$("#store_results").val(prettifiedJson);
		}).fail(function() {
			console.log(arguments);
			$("#store_results").val("error " + arguments[0].status + ": " + arguments[2]);
		})
		return false;
	});
});
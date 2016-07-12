
function run()
{
/*	while (i) {
		$('#ajaxGetUserServletResponse').text("Đang xử lý .");
		$('#ajaxGetUserServletResponse').text("Đang xử lý ..");
		$('#ajaxGetUserServletResponse').text("Đang xử lý ...");
	}*/
	$('#ajaxGetUserServletResponse').text("Đang xử lý ...");
		$.ajax({
			url : 'Servlet',
			data : {
				userName : $('#userName').val()
			},
			success : function(responseText) {
				$('#ajaxGetUserServletResponse').text(responseText);
			}
		});
}


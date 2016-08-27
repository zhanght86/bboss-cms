$(function() {
	$("input[type='radio'][name='operateType']").bind("change",
			function(event, ui) {
				var operateType = $(this).val();
				
				$('.divSelect1').hide();

				switch (operateType) {
				case 'back':
					$('.backSelect').show();

					break;

				case 'del':
					$('.backSelect').hide();

					break;

				default:
					$('.backSelect').hide();

					break;
				}
			});

	$('#dimissionForm')
			.submit(
					function() {
						var url = $('#dimissionForm').attr('action');

						var nodeMsg = $("textarea[name='nodeMsg']").val();

						if (nodeMsg) {
							nodeMsg = encodeURI(nodeMsg);
						}

						var json = {
							taskId : $("input[name='taskId']").val(),
							operateType : $("input[name='operateType']").val(),
							backTaskName : $("input[name='backTaskName']")
									.val(),
							ignoreTargetCandidateUsers : $(
									"input[name='ignoreTargetCandidateUsers']")
									.val(),
							isBackIgnoreSourceTransition : $(
									"input[name='isBackIgnoreSourceTransition']")
									.val(),
							nodeMsg : nodeMsg
						};

						$.post(url, json, function(data) {
							var obj = jQuery.parseJSON(data);

							if (obj.message) {
								$('#message').html(obj.message).fadeOut(300)
										.delay(3000).fadeIn(400);

								return;
							}

							window.location.href = ctx
									+ '/sanyhrm/workflow/task/index.page';
						});

						return false;
					});

	showIgnoreTargetCandidateUsers();

	$('#backTaskName').change(showIgnoreTargetCandidateUsers);
});

function showIgnoreTargetCandidateUsers() {
	var html = '';

	$('#ignoreTargetCandidateUsers').html(html);

	var backTaskNameValue = $('#backTaskName').val();

	if (!backTaskNameValue) {
		$('.divSelect1').hide();

		return;
	}

	var describing = $(
			'#backTaskName option[value="' + backTaskNameValue + '"]').attr(
			"describing");

	if (!describing) {
		$('.divSelect1').hide();

		return;
	}

	var json = jQuery.parseJSON(describing);

	$('.divSelect1').show();

	for (i = 0; i < json.length; i++) {
		html += '<option value="' + json[i].userCode + '">' + json[i].name
				+ '</option>';
	}

	$('#ignoreTargetCandidateUsers').html(html);

	$('#ignoreTargetCandidateUsers').selectmenu('refresh');
}
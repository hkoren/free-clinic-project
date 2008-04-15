function check_enroll_form() {

	
	if (document.enroll.fn.value == '') {
		alert ('Enter First Name');
	}
	else if (document.enroll.ln.value == '') {
		alert ('Enter Last Name');
	}
	else if (document.enroll.bd_m.value == '' || document.enroll.bd_d.value == '' ||document.enroll.bd_y.value == '') {
		alert ('Enter Date of Birth');
	}
	else if ( ( document.enroll.gender[0].checked == false )  && ( document.enroll.gender[1].checked == false ) ) {
		alert ('Enter Gender');
	}


	else { 
		document.enroll.submit();
		// form processed on patient page
	}
	

	
}

function validate_radio(element) {
	for (i = 0; i < element.length; i++ ) {
		if (element[i].checked != false) {
			return true;
			break;
		}	
	}
	return false;
}

// var dateOfBirth = new CalendarPopup();
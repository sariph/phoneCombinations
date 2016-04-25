$(document).ready(
		function() {
            $('#previousPageButton').click(function(){
                var phoneNumber=$('#phoneNumber').val();
                var pageNumber=$('#pageNumber').val();
                var paginationSize=$('#paginationSize').val();
                var totalSize=$('#totalSize').val();
                console.log("Previous | Page Number: "+Number(pageNumber)+" | Total Size is: "+Number(totalSize));
                if(Number(pageNumber)>0){
                    var updatedPageNumber=Number(pageNumber)-Number(paginationSize);
                    console.log("Previous Page | Page Number is: "+updatedPageNumber+" | Pagination Size is: "+paginationSize);
                    $.ajax({
                        type : "GET",
                        url : "finraAssessment",
                        data : "phoneNumber=" + phoneNumber + "&pageNumber=" + updatedPageNumber,
                        success : function(result) {
                            $('body').html(result);
                        },
                        error: function(){
                            console.log('Error: The get request did not go through')
                        }
                    });
                }
            });
            
            $('#nextPageButton').click(function(){
                var phoneNumber=$('#phoneNumber').val();
                var pageNumber=$('#pageNumber').val();
                var paginationSize=$('#paginationSize').val();
                var totalSize=$('#totalSize').val();
                console.log("Next | Page Number: "+Number(pageNumber)+" | Total Size is: "+Number(totalSize));
                if(Number(pageNumber)<Number(totalSize)){
                    var updatedPageNumber=Number(pageNumber)+Number(paginationSize);
                    console.log("Next Page | Page Number is: "+updatedPageNumber+" | Pagination Size is: "+paginationSize);
                    $.ajax({
                        type : "GET",
                        url : "finraAssessment",
                        data : "phoneNumber=" + phoneNumber + "&pageNumber=" + updatedPageNumber,
                        success : function(result) {
                            $('body').html(result);
                        },
                        error: function(){
                            console.log('Error: The get request did not go through')
                        }
                    });
                }
            });
    });

function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function validatePhoneNumber() {
	var phoneNumber = document.forms["finraForm"]["phoneNumber"].value;	
    if (phoneNumber.length !== 7 && phoneNumber.length !== 10) {
		alert("Validation Error: Phone number should be either 7 digits or 10 digits");
		return false;
	}
    
	if (!/^\d+$/.test(phoneNumber)) {
		alert("Validation Error: Phone Number should always be a number");
		return false;
	}
	
    var pageNumber = 0;
    
	$.ajax({
		type : "GET",
		url : "finraAssessment",
		data : "phoneNumber=" + phoneNumber + "&pageNumber=" + pageNumber,
		success : function(result) {
			$('body').html(result);
            console.log('The first get request went through!')
		},
        error: function(){
            console.log('Error: The get request did not go through')
        }
	});
}
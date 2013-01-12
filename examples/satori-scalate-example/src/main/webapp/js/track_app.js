$(document).ready(function() { 
	
  var editUri = $('#editUri').val();	
	
  $('.editable').editable(editUri, { 
      name     : "txt",
      type     : "text",
      submit   : "Save Changes",
      cancel   : "Cancel",
      cssclass : "light",
      submitdata : function(value, settings) {
        return {date: $(this).attr("date")};
      }
  });
  
  $('.delete').click(function(e) {
      e.preventDefault;
      var id = $(this).attr("id"),
          that = $(this); 
      $.ajax({
        url: id,
        type: 'DELETE', // Not works in all browser... :P
        success: function(result) {
           that.closest(".points").hide("slow",function () {
             $(this).remove();
           });
        },
        statusCode: { 
        	405: function() { 
        		alert("Sorry, your browser does not support DELETE Ajax calls, edit the source and perform GET or POST :P"); 
        		} 
        }
      });
  });
  
});

$(document).ready(function() {
  $("#input").focus(function() {
    $(this).val("");
  });
  $("#input").keydown(function(e) {
    if(e.keyCode == 13) {
      e.preventDefault();
      $(this).parent().submit();
      $(this).val("");
    }
  });
});

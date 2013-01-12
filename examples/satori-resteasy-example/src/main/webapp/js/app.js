var app;

$(function () {

  var App = function () {
    this.init();
  }

  App.prototype = {
    init: function () {
      this.show();
    },
    show: function () {
      $("a.action").click(this.loadPerson);
    },
    loadPerson: function (event) {
      var target = $(event.currentTarget),
        container = target.parents('.personContainer');
      event.preventDefault();
      container.load(target.attr('href') + ' .person',
        function () {
          target.unbind('click');
          container.find('a.action').click(app.loadPerson);
        });
    },
    initSignin: function (success) {
      if (success) {
    	$('#loginBox').toggle();
    	$('#loginButton').toggleClass('active');
        location.reload();
        $("body").fadeOut();
      } else {
        var button = $('#loginButton'),
          box = $('#loginBox'),
          form = $('#loginForm');
        button.removeAttr('href');
        button.unbind('mouseup');
        button.mouseup(function (login) {
          box.toggle();
          button.toggleClass('active');
        });
        form.mouseup(function () {
          return false;
        });
        $(document).unbind('mouseup');
        $(document).mouseup(function (login) {
          if (!($(login.target).parent('#loginButton').length > 0)) {
            button.removeClass('active');
            box.hide();
          }
        });

        $('#loginForm').submit(function (event) {

          event.preventDefault();

          var $form = $(this),
            username = $form.find('input[name="username"]').val(),
            password = $form.find('input[name="password"]').val(),
            rememberMe = $form.find('input[name="rememberMe"]').attr('checked')
            url = $form.attr('action');

          $.post(url, {
            username: username,
            password: password,
            rememberMe: rememberMe
          },

          function (data) {
            $("#loginBox").empty().append($(data));
          });
        });
      }
    }
  }
  
  app = new App;
});

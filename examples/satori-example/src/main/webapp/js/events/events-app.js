// Views
// =====

var ModalView = function () {}

ModalView.prototype = {
  show: function (o) {
    o = $.extend({}, o);
    if (o.completed) {
      this.completed(o);
    } else {
      this.notCompleted(o);
    }
  },
  completed: function (o) {
    $("#cform-dialog").trigger("success", o);
    $("#cform-dialog").dialog2("close");
  },
  notCompleted: function (o) {
    this.autofocus();
    if (o.error) $(".modal").effect("shake", {
      times: 3
    }, 50)
  },
  autofocus: function () {
    // FF needs some ns
    setTimeout(function () {
      $('input[autofocus]').trigger('focus');
    }, 0);
  }
}

var EventModalView = function () {
  this.prototype = $.extend({},
  new ModalView(), {
    notCompleted: function (o) {
      $(".datepicker").datetimepicker();
      $(".modal").addClass("wide-modal");
      $("textarea[limit]").charLimit({
        align: "right",
        valign: "top",
        mode: "side"
      });
      ModalView.prototype.notCompleted(o);
      this.showMap(o);
    },
    showMap: function (o) {
      var that = this;
      $('#map-canvas').gmap({
        'center': (o.loc) ? o.loc : "0,0",
        'zoom': (o.loc) ? 7 : 1
      }).bind('init', function (event, map) {
        if ($('#location').val()) {
          var tmp = $('#location').val().split(',');
          var latLng = new google.maps.LatLng(tmp[0], tmp[1]);
          that.addMarker(o, latLng);
          $("#map_canvas").gmap("option", "center", latLng);
          $("#map_canvas").gmap("option", "zoom", 7);
        }

        $(map).click(function (event) {
          that.addMarker(o, event.latLng);
        });
      });
    },
    findLocation: function (location, marker) {
      $('#map-canvas').gmap('search', {
        'location': location
      }, function (results, status) {
        if (status === 'OK') {
          marker.setTitle(results[0].formatted_address);
          $('#address').val(results[0].formatted_address);
        }
      });
      $('#location').val(marker.getPosition());
    },
    addMarker: function (o, latLng) {
      var that = this;

      if (!o.marked) {
        $('#map-canvas').gmap('addMarker', {
          'position': latLng,
          'draggable': true,
          'bounds': false
        }, function (map, marker) {
          that.findLocation(marker.getPosition(), marker);
        }).dragend(function (event) {
          that.findLocation(event.latLng, this);
        }).click(function () {
          $('#map-canvas').gmap('openInfoWindow', {
            'content': this.title
          }, this);
        });
      }
      o.marked = true;
    }
  });
}

var ReservationModalView = function (o) {
  this.prototype = new ModalView();
}

var SigninModalView = function (o) {
  this.prototype = $.extend({},
  new ModalView(), {
    completed: function () {
      $("#signin-dialog").dialog2("close");
      window.location.reload(true);
    }
  });
}

// Application
// ===========

var EventsApp = function () {
  this.init();
}

EventsApp.prototype = {
  init: function () {
    this.show();
  },
  show: function () {
    $(document).controls();

    $("a.confirm").click(function (e) {
      e.preventDefault();
      // We cannot do click() on the original element 
      // for some incompatibilities with jquery-ui-maps
      var href = this.href;
      bootbox.confirm("Are you sure?", function (confirmed) {
        if (confirmed) {
          window.location = href;
        }
      });
    });

    $('.hint').tooltip();

    $("#loading").ajaxStart(function () {
      $(this).show();
    }).ajaxStop(function () {
      $(this).hide();
    });
  },
  onSuccess: function (selector, f) {
    var that = this,
      cb = jQuery.isFunction(f) ? f : function () {
        $("#main").hide().load(f, function () {
          that.show();
        }).fadeIn();
      };
    $(document).delegate(selector, "success", cb);
  },

  views: {
    eventView: {
      show: function (lat, lng) {
        var latLng = new google.maps.LatLng(lat, lng);
        $('#vevent-map-canvas').gmap({
          'center': latLng,
          'zoom': 12
        }).bind('init', function (event, map) {
          $('#vevent-map-canvas').gmap('addMarker', {
            'position': latLng,
            'draggable': false,
            'bounds': false
          }).click(function () {
            $('#vevent-map-canvas').gmap('openInfoWindow', {
              'content': $(".vevent-content").html()
            }, this);
          })
        });
      }
    }
  }
}

var ModalViews = function () {};
ModalViews.prototype = {
  event: new EventModalView().prototype,
  reservation: new ReservationModalView().prototype,
  signin: new SigninModalView().prototype
}

/*
   Plugin: charLimit

   JQuery plugin that limits input on textareas and handles character counters.

   Options:

      mode    - Supported modes: outer, inner, side.
      attach  - Supply your own element where the counter will write.
      format  - Format of the counter message.
      align   - Horizontal alignment [right, left, center].
      valign  - Vertical alignment [top, bottom, center].
      zindex  - z-index property for inner mode. Defaults to -5

   Events:

      limitExceed  - ...
      limitOk      - ...

   Usage:

      $("textarea[limit]").charLimit();

      <textarea limit="100"></textarea>

*/
(function ($) {

  var stylers = {

    inner: {
      valign: {
        top: function () {
          this.css.top = 0;
        },
        center: function () {
          this.css["line-height"] = this.h + "px";
        },
        bottom: function () {
          this.css.top = (this.h - this.fsize / 2) + "px";
        }
      },
      align: {
        center: function () {
          this.css["text-align"] = "center";
        },
        right: function () {
          this.css["text-align"] = "right";
        },
        left: function () {
          this.css.left = 0;
        }
      },
      prepare: function () {
        this.counter.prependTo(this.wrapper);
        this.ta.css("background-color", "transparent");
        this.css.position = "absolute";
        this.css.overflow = "hidden";
        this.css.top = 0;
        this.css["z-index"] = this.zindex;
        this.css.width = this.w;
      }
    },
    outer: {
      valign: {
        top: function () {
          this.counter.prependTo(this.wrapper);
        },
        center: function () {
          this.counter.prependTo(this.wrapper);
        },
        bottom: function () {
          this.counter.appendTo(this.wrapper);
        }
      },
      align: {
        center: function () {
          this.css["text-align"] = "center";
        },
        right: function () {
          this.css["text-align"] = "right";
        },
        left: function () {
          this.css["text-align"] = "left";
        }
      },
      prepare: function () {
        this.css.width = this.w;
      }
    },
    side: {
      valign: {
        top: function () {
          this.counter.prependTo(this.wrapper);
          this.css.top = 0;
        },
        center: function () {},
        bottom: function () {
          this.counter.appendTo(this.wrapper);
          this.css.bottom = 0;
        }
      },
      align: {
        center: function () {
          this.css["text-align"] = "center";
        },
        right: function () {
          this.css.left = this.css.width + this.fsize / 2 + "px";
        },
        left: function () {
          this.css.left = -(this.fsize * this.len) / 2 + "px";
          this.wrapper.css("overflow", "visible");
        }
      },
      prepare: function () {
        this.css.width = this.w;
        this.css["text-align"] = "left";
        this.css.position = "absolute";
      }
    }

  };

  var methods = {

    init: function (options) {

      // Default options
      var options = $.extend({
        align: "right",
        valign: "bottom",
        zindex: -5,
        mode: "outer",
        format: "{0}",
        events: {
          exceed: true,
          ok: true
        }
      }, options);

      return this.each(function () {
        var $this = $(this),
          charLimit = $this.attr("limit"),
          wrapper = $this.wrap('<div class="counter-wrap" style="overflow:hidden;position:relative;" />').parent(),
          counter = options.attach || $("<div/>", {
            "class": "counter",
            text: methods._format(options.format, charLimit)
          });

        if ($this.attr("id")) {
          wrapper.addClass($this.attr("id"));
        }

        if (!options.attach) {
          methods._computeStyles($this, counter, wrapper, options);
        }

        this.checkLimit = function () {
          var charLength = $this.val().length;

          if (charLength >= charLimit) {
            counter.html(methods._format(options.format, 0));
            if (options.events.exceed) $this.triggerHandler("limitExceed");
            $this.val($this.val().substring(0, charLimit));
          } else {
            var charsLeft = charLimit - charLength;
            if (options.events.ok) $this.triggerHandler("limitOk", charsLeft);
            counter.html(methods._format(options.format, charsLeft));
          }
        }

        this.keydownHandler = function (e) {
          if ($this.val().length >= charLimit && e.keyCode !== 8 && e.keyCode !== 46) {
            e.preventDefault();
          }
        }

        this.pasteHandler = function (e) {
          setTimeout(this.checkLimit, 0); // 0 millis
        }

        this.checkLimit();

        $this.keyup(this.checkLimit);
        $this.keydown(this.keydownHandler);
        $this.on("paste", this.pasteHandler);

      });
    },
    destroy: function () {
      return this.each(function () {
        var $this = $(this);
        $this.unbind('keyup', this.checkLimit);
        $this.unbind('keydown', this.keydownHandler);
        $this.unbind('paste', this.pasteHandler);
      })
    },

    // Hacky, there's a better solution?
    // @TODO pasar a JQuery
    _counterFontSize: function (pa) {

      pa = pa || document.body;
      var who = document.createElement("div");
      who.style.visibility = "hidden";
      who.className = "counter";
      who.appendChild(document.createTextNode('M'));
      pa.appendChild(who);
      var fs = who.offsetHeight;
      pa.removeChild(who);
      return fs;

    },

    _format: function (f, n) {
      return f.replace(new RegExp('\\{0\\}', 'gm'), n);
    },


    _computeStyles: function (textarea, counter, wrapper, options) {

      var ctx = {
        ta: textarea,
        zindex: options.zindex,
        counter: counter,
        wrapper: wrapper,
        fsize: methods._counterFontSize(),
        len: textarea.attr("limit").length,
        h: textarea.height(),
        w: textarea.width(),
        css: {}
      };

      stylers[options.mode].prepare.apply(ctx);
      stylers[options.mode].align[options.align].apply(ctx);
      stylers[options.mode].valign[options.valign].apply(ctx);

      counter.css(ctx.css);

    }

  };


  $.fn.charLimit = function (method) {

    if (methods[method] && method.charAt(0) != '_') {
      return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
    } else if (typeof method === 'object' || !method) {
      return methods.init.apply(this, arguments);
    } else {
      $.error('Method ' + method + ' does not exist on jQuery.charLimit');
    }

  };

})(jQuery);
